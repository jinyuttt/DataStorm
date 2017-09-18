/**
 * 
 */
package dataStrom.bus.rpc;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jinyu
 * 创建对象
 */
public class ProxyFactory {
    private String rpcHost="127.0.0.1";
    private int rpcport=5555;
    private String netType="udp";
    private Map<String,RpcInvocationHandler> rpcInvokerCache = new ConcurrentHashMap<String, RpcInvocationHandler>();
    public void setRPCHost(String ip,int port,String netType)
    {
        this.rpcHost=ip;
        this.rpcport=port;
        this.netType=netType;
    }
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> api, RpcConfig config) throws Exception {   
        if(config==null)
        {
            config=new RpcConfig();
        }
        String module = config.getModule();
        if(module == null ||module.trim().length()==0){
            module = api.getSimpleName();
            config.setModule(module);
        }
            
        String encoding = config.getEncoding();
        int timeout = config.getTimeout(); 
        
        String cacheKey = String.format("module=%s&&encoding=%s&&timeout=%d", module, encoding, timeout);
        
        RpcInvocationHandler rpcInvoker = rpcInvokerCache.get(cacheKey);
        Class<T>[] interfaces = new Class[] { api };
        if(rpcInvoker == null){
            rpcInvoker= new RpcInvocationHandler(config, this.rpcHost+":"+this.rpcport, netType);
            rpcInvokerCache.put(cacheKey, rpcInvoker); 
        }
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return (T) Proxy.newProxyInstance(classLoader, interfaces, rpcInvoker);
    } }

     
