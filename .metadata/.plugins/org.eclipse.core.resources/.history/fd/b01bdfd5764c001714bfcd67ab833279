/**    
 * 文件名：MulticastServerSocket.java    
 *    
 * 版本信息：    
 * 日期：2017年5月27日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package JNetSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ISocket.ISocketServer;

/**    
 *     
 * 项目名称：JNetSocket    
 * 类名称：MulticastServerSocket    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2017年5月27日 上午1:22:38    
 * 修改人：jinyu    
 * 修改时间：2017年5月27日 上午1:22:38    
 * 修改备注：    
 * @version     
 *     
 */
public class MulticastServerSocket implements ISocketServer{
    private IRecData recInstance=null;
    SocketAddress sendAddress=null;
    private boolean isRuning=true;
    MulticastSocket msr = null;//创建组播套接字  
    public boolean InitServer(IRecData rec,String addr,int port)
    {
        recInstance=rec;
      //创建DatagramSocket对象  
        InetAddress maddr = null;
        try {
            maddr = InetAddress.getByName(addr);
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            msr = new MulticastSocket(port);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } 
        try {
            msr.setReceiveBufferSize(128);
        } catch (SocketException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            msr.setReuseAddress(true);
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        try {
            msr.joinGroup(maddr);
        } catch (IOException e1) {
            e1.printStackTrace();
        }//加入连接  
        byte[] buffer = new byte[8192];  
        Thread recData=new Thread(new Runnable(){
            @Override
            public void run() {
               while(isRuning)
               {
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);  
                try {
                    msr.receive(dp);
                } catch (IOException e) {
                    e.printStackTrace();
                }  
                //从服务器返回给客户端数据  
                String clientAddress = dp.getAddress().getHostName(); //获得客户端的IP地址  
                int clientPort = dp.getPort(); //获得客户端的端口号  
                if(recInstance!=null)
                {
                    byte[]data=dp.getData();
                    UDPClient client=new UDPClient();
                    //
                    DatagramSocket sockettmp = null;
                    try {
                        sockettmp = new DatagramSocket();
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                    String[]args=new String[]{clientAddress, String.valueOf(clientPort)};
                    client.setSocket(sockettmp, args);
                    recInstance.setServer(client);
                    recInstance.recviceData(clientAddress+":"+clientPort, data);
                }
               }
               msr.close();
            }
        });
        recData.setDaemon(true);
        recData.setName("UDPServer");
        recData.start();
        return false;
     
    }
    public void close()
    {
        isRuning=false;
    }
    @Override
    public void sendData(String host, int port, byte[] data) {
      
    }
    @Override
    public void sendData(SocketAddress addr, byte[] data) {
        
    }
    @Override
    public void sendCall(byte[] data) {
        
    }
}
