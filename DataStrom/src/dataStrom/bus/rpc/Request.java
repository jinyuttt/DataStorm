/**
 * 
 */
package dataStrom.bus.rpc;

/**
 * @author jinyu
 * RPC请求
 */
public class Request {

    private String module = ""; //名称
    private String method;      //方法
    private Object[] params;    //参数
    private String[] paramTypes;//参数类型
    private String encoding = "UTF-8";
    
    public String getModule() {
        return module;
    }
    public void setModule(String module) {
        this.module = module;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public Object[] getParams() {
        return params;
    }
    public void setParams(Object[] params) {
        this.params = params;
    }
    public String[] getParamTypes() {
        return paramTypes;
    }
    public void setParamTypes(String[] paramTypes) {
        this.paramTypes = paramTypes;
    }
    public String getEncoding() {
        return encoding;
    }
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Request method(String method){
        this.method = method;
        return this;
    }
    public Request module(String module){
        this.module = module;
        return this;
    }
    public Request params(Object... params){
        this.params = params;
        return this;
    }
    public Request encoding(String encoding){
        this.encoding = encoding;
        return this;
    }
    public Request paramTypes(Class<?>... types){
        if(types == null) return this;
        this.paramTypes = new String[types.length];
        for(int i=0; i<types.length; i++){
            this.paramTypes[i]= types[i].getCanonicalName(); 
        }
        return this;
    }
    
    
    public static void normalize(Request req){
        if(req.module == null){
            req.module = "";
        }
        if(req.params == null){
            req.params = new Object[0];
        }
    }  

}
