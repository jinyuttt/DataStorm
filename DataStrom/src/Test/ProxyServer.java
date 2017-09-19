/**
 * 
 */
package Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dataStrom.bus.config.BrokerConfig;
import dataStrom.bus.config.ServerConfig;
import dataStrom.bus.mq.MqMode;

import dataStrom.bus.proxyFactory.ProxyProducer;
import dataStrom.bus.rpc.RpcProcessor;

/**
 * @author jinyu
 *
 */
public class ProxyServer {
    public static void main(String[] args) {
        MQ();
    }
    private void  RPC()
    {
        BrokerConfig config=new BrokerConfig();
        config.addressList="127.0.0.1:6666";
        config.netType="udp";
        ServerConfig srvConfig=new ServerConfig();
        srvConfig.address="127.0.0.1";
        srvConfig.port=5555;
        srvConfig.netType="udp";
        ProxyProducer p=new ProxyProducer(config, MqMode.RPC);
    
        RpcProcessor processor=new RpcProcessor(config, srvConfig);
        RPCServers services=new RPCServers();
        processor.addModule(services);;
       p.publishRPC(processor);
        try {
            System.in.read();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private static void MQ()
    {
        BrokerConfig config=new BrokerConfig();
        config.addressList="127.0.0.1:6666";
        config.netType="udp";
        ServerConfig srvConfig=new ServerConfig();
        srvConfig.address="127.0.0.1";
        srvConfig.port=5555;
        srvConfig.netType="udp";
        ProxyProducer p=new ProxyProducer(config, MqMode.MQ);
        while(true)
        {
       byte[] data="hello".getBytes();
       p.publish("jinyu", data);
          try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        }
    }
}
