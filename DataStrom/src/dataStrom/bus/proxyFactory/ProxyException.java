/**
 * 
 */
package dataStrom.bus.proxyFactory;

/**
 * @author jinyu
 * �쳣��
 * �쳣���� 0x1000*****(3λ��16���ƣ�
 */
public class ProxyException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 0x1000001;
    
    public ProxyException(String errorMsg)
    {
      
        super(errorMsg);
        message=errorMsg;
    }
   private String message; 
    public String getMessage() {  
        return message;  
    }  
    public void setMessage(String message) {  
        this.message = message;  
    } 
}
