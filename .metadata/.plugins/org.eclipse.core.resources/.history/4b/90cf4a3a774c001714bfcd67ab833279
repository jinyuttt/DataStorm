package JNetSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ISocket.ISocketServer;

public class UDPServerSocket implements  ISocketServer {
    private DatagramSocket socket;
    private IRecData recInstance=null;
    SocketAddress sendAddress=null;
    private boolean isRuning=true;
    private String srcAddr="";
    private int srcPort=0;
    /*
     * 初始化监听
     */
    public boolean InitServer(IRecData rec,String host,int port)
    {
        recInstance=rec;
      //创建DatagramSocket对象  
        if(host==null||host.isEmpty())
        {
            try {
                socket = new DatagramSocket(port);
            } catch (SocketException e) {
               
                e.printStackTrace();
                return false;
            }  
        }
        else
        {
            InetAddress addr = null;
            try {
                addr = InetAddress.getByName(host);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return false;
            }
            try {
                socket = new DatagramSocket(port,addr);
            } catch (SocketException e) {
                e.printStackTrace();
                return false;
            }  
        }
        try {
            socket.setReceiveBufferSize(128);
            socket.setSendBufferSize(128);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Thread recData=new Thread(new Runnable(){
            @Override
            public void run() {
                byte[] buf = new byte[1024];  //定义byte数组  
                DatagramPacket packet = new DatagramPacket(buf, buf.length);  //创建DatagramPacket对象  
                while(isRuning)
                {
                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                } 
                //从服务器返回给客户端数据  
                String clientAddress = packet.getAddress().getHostName(); //获得客户端的IP地址  
                int clientPort = packet.getPort(); //获得客户端的端口号 
                srcAddr=clientAddress;
                srcPort=clientPort;
                if(recInstance!=null)
                {
                    byte[]data=packet.getData();
                    //构造可以发送数据的对象
                    UDPClient client=new UDPClient();
                    String[]args=new String[]{clientAddress, String.valueOf(clientPort)};
                    client.setSocket(packet, args);
                    recInstance.setServer(socket);
                    recInstance.recviceData(clientAddress+":"+clientPort, data);
                }
            }
                socket.close();//跳出循环则关闭
            }
            
        });
        recData.setDaemon(true);
        recData.setName("UDPServer");
        recData.start();
        return false;
     
    }
   
    public void sendData(String host,int port,byte[]data)
    {
        if(socket!=null)
        {
        InetAddress address = null;
            try {
                address = InetAddress.getByName(host);
            } catch (UnknownHostException e) {
               
                e.printStackTrace();
                return;
            }
            DatagramPacket dataGramPacket = new DatagramPacket(data, data.length, address, port);
            try {
                socket.send(dataGramPacket);
            } catch (IOException e) {
               
                e.printStackTrace();
            }
        }
    }
  
   public void sendData(SocketAddress addr,byte[]data)
 {
   if(socket!=null)
   {
     //创建发送方的数据报信息  
     DatagramPacket dataGramPacket = new DatagramPacket(data, data.length,addr);  
     try {
         socket.send(dataGramPacket);
     } catch (IOException e) {
         e.printStackTrace();
     }  
   }
 }
 
   public void close()
    {
        isRuning=false;
        socket.close();
    }

@Override
public void sendCall(byte[] data) {
    InetAddress address = null;
    try {
        address = InetAddress.getByName(srcAddr);
    } catch (UnknownHostException e1) {
        e1.printStackTrace();
    }  
    DatagramPacket dataGramPacket = new DatagramPacket(data, data.length, address, srcPort);  
    try {
        socket.send(dataGramPacket);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
