/**
 * 
 */
package Test;

import java.io.IOException;

import dataStrom.bus.config.BrokerConfig;
import dataStrom.bus.config.ServerConfig;
import dataStrom.bus.producer.PublishRPC;
import dataStrom.bus.rpc.RpcProcessor;

/**
 * @author jinyu
 *
 */
public class RPCServer {

    /**
     * @param args  
     */
    public static void main(String[] args) {
        ServerConfig srvConfig=new ServerConfig();
        srvConfig.address="127.0.0.1";
        srvConfig.port=5555;
        srvConfig.netType="udp";
        PublishRPC server=new PublishRPC();
        BrokerConfig config=new BrokerConfig();
        config.addressList="127.0.0.1:6666";
        config.netType="udp";
        RpcProcessor processor=new RpcProcessor(config, srvConfig);
        RPCServers services=new RPCServers();
        processor.addModule(services);;
        server.publish(processor);
        try {
            System.in.read();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
