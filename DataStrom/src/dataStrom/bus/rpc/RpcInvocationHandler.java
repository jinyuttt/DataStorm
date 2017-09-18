/**
 * 
 */
package dataStrom.bus.rpc;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.TypeUtils;

import dataStrom.bus.mq.MQMessage;
import dataStrom.bus.mq.Message;
import dataStrom.bus.net.Sync.ResultCallback;
import dataStrom.bus.proxy.ProxyClient;

/**
 * @author jinyu
 *同时直接封装转换
 */
public class RpcInvocationHandler implements InvocationHandler {
    private static final String DEFAULT_ENCODING = "UTF-8"; 
    private static final Object REMOTE_METHOD_CALL = new Object();
  private String host;
  private String netType;
  private int port;
  private String module = ""; 
  private String encoding = "UTF-8";
  private int timeout = 10000;  
  ProxyClient<Message,Message> client=null;
  
  /**
   * 创建转换对象
   * @param config 配置
   * @param address 服务地址 IP+:+port;
   * @param netType 通讯类型
   */
    public RpcInvocationHandler(RpcConfig config,String address,String netType) {
        if(config==null)
        {
            config=new RpcConfig();
        }
   
      this.netType=netType;
      if(address!=null)
      {
      String[] addrs=address.split(":");
      host=addrs[0];
      port=Integer.valueOf(addrs[1]);
      }
      //
      if(config != null){
          this.module = config.getModule();
          this.timeout = config.getTimeout(); 
          this.encoding = config.getEncoding();
      }
    }
    /**
     * 调用服务
     * 超时10s
     * @param req 请求
     * @return  回会发的信息
     * @throws IOException 错误
     * @throws InterruptedException 信息
     */
    public Message invokeSync(Message req) throws IOException, InterruptedException {
        return invokeSync(req, 10000);//default 10s
    }
    
    /**
     *  调用，网络调用
     * @param req
     * @param timeout 超时
     * @return
     * @throws IOException
     */
    public Message invokeSync(Message req, int timeout) throws IOException {
      
        client=new ProxyClient<Message,Message>();
        client.netType=this.netType;
        client.setHost(host, port);
        try {
            return   client.invokeSync(req, timeout);
        } catch (InterruptedException e) {
        return null;
           
        }
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
     
            if(args == null){
                args = new Object[0];
            }
            Object value = handleLocalMethod(proxy, method, args);
            if (value != REMOTE_METHOD_CALL) return value; 
            Class<?> returnType = method.getReturnType(); 
            Object result= invokeSync(returnType, method.getName(),method.getParameterTypes(), args);
            return result;
    }
    /**
     * 远程调用 转换为request请求
     * @param resultClass
     * @param method
     * @param paramTypes
     * @param args
     * @return
     */
    public <T> T invokeSync(Class<T> resultClass, String method, Class<?>[] paramTypes, Object... args){
        Request request = new Request()
            .module(module)
            .method(method) 
            .paramTypes(paramTypes)
            .params(args)
            .encoding(encoding);
    
        return invokeSync(resultClass, request);
    } 
    
    /**
     * 远程调用
     * @param resultClass
     * @param request
     * @return
     */
    public <T> T invokeSync(Class<T> resultClass, Request request){
        Response resp = invokeSync(request);
        try {
            @SuppressWarnings("unchecked")
            T res = (T)convert(extractResult(resp), resultClass);
            return res;
        } catch (ClassNotFoundException e) { 
            throw new RpcException(e.getMessage(), e.getCause());
        }
    }
    
    /**
     *request 远程调用
     * @param request
     * @return
     */
    public Response invokeSync(Request request){
        Message msgReq= null, msgRes = null;
        try {
            msgReq =encodeRequest(request); 
            
            msgRes =invokeSync(msgReq, this.timeout); 
           
        } catch (IOException e) {
            throw new RpcException(e.getMessage(), e);
        }
        
        if (msgRes == null) { 
            String errorMsg = String.format("module(%s)-method(%s) request timeout\n%s", 
                    module, request.getMethod(), msgReq.toString());
            throw new RpcException(errorMsg);
        }
        
        return decodeResponse(msgRes);
    }
    /**
     *   调用
     * @param clazz
     * @param request
     * @param callback
     */
    public <T> void invokeAsync(final Class<T> clazz, Request request,  final ResultCallback<T> callback){
        invokeAsync(request, new ResultCallback<Response>() { 
            @Override
            public void onReturn(Response resp) {  
                Object netObj = extractResult(resp);
                try {
                    @SuppressWarnings("unchecked")
                    T target = (T) convert(netObj, clazz);
                    callback.onReturn(target);
                } catch (ClassNotFoundException e) { 
                    throw new RpcException(e.getMessage(), e.getCause());
                }
            }
        });
    }
    
    public void invokeAsync(Request request, final ResultCallback<Response> callback){ 
   
        final Message msgReq =encodeRequest(request); 
        try {
            client.invokeAsync(msgReq, new ResultCallback<Message>() {
                @Override
                public void onReturn(Message result) { 
                  
                    Response resp =decodeResponse(result);
                    if(callback != null){
                        callback.onReturn(resp);
                    }
                }
            });
        } catch (IOException e) {
            throw new RpcException(e.getMessage(), e);
        }  
    }

    
    private Object extractResult(Response resp){
        if(resp.getStackTrace() != null){
            Throwable error = resp.getError();
            if(error != null){
                if(error instanceof RuntimeException){
                    throw (RuntimeException)error;
                }
                throw new RpcException(error.getMessage(), error.getCause()); 
            } else {
                throw new RpcException(resp.getStackTrace());
            }
        } 
        return resp.getResult();
    }
    protected Object handleLocalMethod(Object proxy, Method method,
            Object[] args) throws Throwable {
        String methodName = method.getName();
        Class<?>[] params = method.getParameterTypes();

        if (methodName.equals("equals") && params.length == 1
                && params[0].equals(Object.class)) {
            Object value0 = args[0];
            if (value0 == null || !Proxy.isProxyClass(value0.getClass()))
                return new Boolean(false);
            RpcInvocationHandler handler = (RpcInvocationHandler) Proxy.getInvocationHandler(value0);
            return new  Boolean(this.equals(handler));
        } else if (methodName.equals("hashCode") && params.length == 0) {
            return new Integer(this.hashCode());
        } else if (methodName.equals("toString") && params.length == 0) {
            return "RpcInvocationHandler[" +this+ "]";
        }
        return REMOTE_METHOD_CALL;
    } 
//转换
    public Message encodeRequest(Request request) {
        Message msg = new Message(); 
        String encoding = request.getEncoding();
        if(encoding == null) encoding = DEFAULT_ENCODING;  
        msg.setBody(toJSONBytes(request, encoding,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteClassName));
        return msg;
    }
    
    private static final byte[] toJSONBytes(Object object, String charsetName,
            SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter();

        try {
            JSONSerializer serializer = new JSONSerializer(out);
            for (SerializerFeature feature : features) {
                serializer.config(feature, true);
            }

            serializer.write(object);

            return out.toBytes(charsetName);
        } finally {
            out.close();
        }
    }
    public Request decodeRequest(MQMessage msg) {
        String encoding = msg.getEncoding();
        if(encoding == null){
            encoding = DEFAULT_ENCODING;
        }
        String jsonString = msg.getBodyString(encoding);
        Request req = JSON.parseObject(jsonString, Request.class);
        Request.normalize(req); 
        return req;
    }

    public Object convert(Object param, Class<?> targetType) throws ClassNotFoundException {
        if(targetType.getName().equals("java.lang.Class")){
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            return classLoader.loadClass(param.toString());
        }
        return TypeUtils.castToJavaBean(param, targetType);
    }
    
    public Message encodeResponse(Response response) {
        Message msg = new Message(); 
        msg.setStatus("200"); 
        if(response.getError() != null){
            Throwable error = response.getError(); 
            if(error instanceof IllegalArgumentException){
                msg.setStatus("400");
            } else {
                msg.setStatus("500");
            } 
        }  
        String encoding = response.getEncoding();
        if(encoding == null) encoding = DEFAULT_ENCODING;  
        msg.setBody(toJSONBytes(response, encoding,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteClassName)); 
        return msg; 
    }
    

    public Response decodeResponse(Message msg){ 
        MQMessage message=new MQMessage(msg);
        String encoding = message.getEncoding();
        if(encoding == null){
            encoding = DEFAULT_ENCODING;
        }
        String jsonString = message.getBodyString(encoding);
        Response res = null;
        try{
            res = JSON.parseObject(jsonString, Response.class);
        } catch (Exception e){ //probably error can not be instantiated
            res = new Response(); 
            JSONObject json = null;
            try{
                jsonString = jsonString.replace("@type", "unknown-class"); //绂佹鎺夊疄渚嬪寲
                json = JSON.parseObject(jsonString); 
            } catch(Exception ex){
                String prefix = "";
                if(msg.isStatus200()){
                    prefix = "JSON format invalid: ";
                }
                throw new RpcException(prefix + jsonString);
            } 
            if(json != null){
                final String stackTrace = Response.KEY_STACK_TRACE;
                final String error = Response.KEY_ERROR;
                if(json.containsKey(stackTrace) &&
                        json.get(stackTrace) != null){ 
                    throw new RpcException(json.getString(stackTrace));
                }
                if(json.containsKey(error) &&
                        json.get(error) != null){ 
                    throw new RpcException(json.getString(error));
                }
                res.setResult(json.get(Response.KEY_RESULT));
            }
        } 
        return res;
    } 

}
