/**
 * 
 */
package Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dataStrom.bus.config.BrokerConfig;

import dataStrom.bus.mq.Message;
import dataStrom.bus.mq.MqMode;
import dataStrom.bus.producer.Producer;

/**
 * @author jinyu
 *
 */
public class TestServr {
    /**
     * @param args
     */
    public static void main(String[] args) {
        BrokerConfig config=new BrokerConfig();
        config.addressList="127.0.0.1:6666";
        config.netType="udp";
        Producer p=new Producer(config, MqMode.PubSub);
        while(true)
        {
            Message msg=new Message();
            msg.setMQ("MQ");
            msg.setTopic("MQ");
            msg.setBody("ssssss");
            //msg.cmd=MQCmd.MQMsg;
            try {
                p.sendAsync(msg);
            } catch (IOException e) {
         
                e.printStackTrace();
            }
            //
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        

    }
}
