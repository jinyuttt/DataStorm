/**
 * 
 */
package Test;



import dataStrom.bus.rpc.ProxyFactory;

/**
 * @author jinyu
 *
 */
public class RPCClient {
    /**
     * @param args
     */
    public static void main(String[] args) {
        ProxyFactory factory =null;
         factory =new ProxyFactory();
         factory.setRPCHost("127.0.0.1", 5555, "udp");
         try {
             IRPCService cl=factory.getService(IRPCService.class, null);
            String ss=cl.sayHello();
            System.out.println("·µ»Ø£º"+ss);
             cl.sayWord("mmmmm");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
