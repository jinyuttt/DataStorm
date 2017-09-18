/**
 * 
 */
package Test;

import java.io.IOException;

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
}
