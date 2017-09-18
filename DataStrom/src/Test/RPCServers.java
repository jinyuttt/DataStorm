/**
 * 
 */
package Test;

/**
 * @author jinyu
 *
 */
public class RPCServers implements IRPCService {

    /* (non-Javadoc)
     * @see Test.IRPCService#sayHello()
     */
    @Override
    public String sayHello() {
   return "ssssss";
    }

    /* (non-Javadoc)
     * @see Test.IRPCService#sayWord(java.lang.String)
     */
    @Override
    public void sayWord(String name) {
        System.out.println(name);

    }

}
