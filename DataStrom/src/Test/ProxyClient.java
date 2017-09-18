/**
 * 
 */
package Test;

import java.util.concurrent.TimeUnit;

import dataStrom.bus.config.BrokerConfig;
import dataStrom.bus.proxyFactory.ProxyConsumer;


/**
 * @author jinyu
 *
 */
public class ProxyClient {
    public static void main(String[] args) {
        ProxyConsumer c=new ProxyConsumer();
        BrokerConfig config=new BrokerConfig();
        config.addressList="127.0.0.1:6666";
        config.netType="udp";
        IRPCService obj=c.invokeRPC(config, IRPCService.class);
        while(true)
        {
        obj.sayHello();
        String ss=obj.sayHello();
        System.out.println("·µ»Ø£º"+ss);
         obj.sayWord("mmmmm");
       try {
        TimeUnit.MILLISECONDS.sleep(50);
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    };
        }
    }
}
