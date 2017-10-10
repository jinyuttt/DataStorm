/**
 * 
 */
package dataStrom.bus.net.judp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingQueue;


import dataStrom.bus.net.NetType;
import dataStrom.bus.net.Server;
import dataStrom.bus.net.Session;

/**
 * @author jinyu
 *
 */
@NetType("judp_Server")
public class judpServer implements Server{
    DatagramSocket  server=null;
    private final int recSize=128*1024;
    private final int recBufSize=65535;
    Thread recThread=null;
    private LinkedBlockingQueue<judpSession> sessions=new LinkedBlockingQueue<judpSession>();
    @Override
    public void close() throws IOException {
   if(server!=null)
   {
       server.close();
   }
        
    }

    @Override
    public  synchronized void start(int port) throws Exception {
        server = new DatagramSocket(port);
        server.setReceiveBufferSize(recSize);
        recvice();
    }

    @Override
    public synchronized void start(String host, int port) throws Exception {
        server = new DatagramSocket(port,InetAddress.getByName(host));
        server.setReceiveBufferSize(recSize);
        recvice();
    }

    @Override
    public void join() throws InterruptedException {
        
    }

    @Override
    public Session getSession() {
      try {
        return  sessions.take();
    } catch (InterruptedException e) {
        
        e.printStackTrace();
    }
      return null;
    }
private void recvice()
{
    if(recThread==null)
    {
        recThread=new Thread(new Runnable() {

            @Override
            public void run() {
                while(true)
                {
                    byte[] recvBuf = new byte[recBufSize];
                    DatagramPacket recvPacket 
                        = new DatagramPacket(recvBuf , recvBuf.length);
                    try {
                        server.receive(recvPacket);
                        int port = recvPacket.getPort();
                        InetAddress addr = recvPacket.getAddress();
                        judpSession session=new judpSession(server,addr,port);
                        byte[]recData=new byte[recvPacket.getLength()];
                        System.arraycopy(recvPacket.getData(), 0, recData, 0, recData.length);
                        session.addData(recData);
                        try {
                            sessions.put(session);
                        } catch (InterruptedException e) {
                          
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                     
                        e.printStackTrace();
                    }
                }
            }
            
        });
   
}
    recThread.setDaemon(true);
    recThread.setName("udp_server");
    recThread.start();
    
}

@Override
public String getlocahost() {
 if(server!=null)
 {
     return  server.getLocalAddress().getHostAddress();
 }
return null;
}

@Override
public int getLocalPort() {
    if(server!=null)
    {
     return   server.getLocalPort();
    }
    return 0;
}
}
