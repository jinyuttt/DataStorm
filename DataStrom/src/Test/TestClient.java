/**
 * 
 */
package Test;

import java.io.IOException;

import dataStrom.bus.config.BrokerConfig;
import dataStrom.bus.consumer.Consumer;
import dataStrom.bus.consumer.ConsumerHandler;
import dataStrom.bus.mq.MQMessage;
import dataStrom.bus.mq.Message;
import dataStrom.bus.mq.MqMode;

/**
 * @author jinyu
 *
 */
public class TestClient {

    /**
     * @param args
     */
    public static void main(String[] args) {
        BrokerConfig config=new BrokerConfig();
        config.addressList="127.0.0.1:6666";
        config.netType="udp";
        Consumer c=new Consumer(config, MqMode.PubSub)
                ;
        try {
            c.start(new ConsumerHandler() {

                @Override
                public void handle(Message msg, Consumer consumer) throws IOException {
                   MQMessage  qmsg=new MQMessage(msg);
                    System.out .println(qmsg.getMsg());
                    
                }
                
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

    }

}
