package dataStrom.bus.net.judp;


import java.util.logging.Logger;

import dataStrom.bus.net.Client;
import dataStrom.bus.net.NetType;
import dataStrom.bus.net.udp.udpClient;

@NetType("judp_Client")
public class judpClient implements Client {
    
    private static final Logger log = Logger.getLogger(udpClient.class.getName());
    udpClient client=null;
   public judpClient()
   {
      client = new udpClient();
   }
    @Override
    public void setHost(String host, int port) {
        client.setHost(host, port);

    }

    @Override
    public boolean connect() {
     return   client.connect();
    }

    @Override
    public boolean connect(int timeout) {
       return client.connect(timeout);
    }

    @Override
    public void sendData(byte[] data) {
       client.sendData(data);
    }

    @Override
    public void close() {
        client.close();

    }

    @Override
    public int read(byte[] data) {
    return    client.read(data);

    }

    @Override
    public byte[] read() {
       return client.read();
    }

    @Override
    public boolean join() {
       return client.join();
    }

}
