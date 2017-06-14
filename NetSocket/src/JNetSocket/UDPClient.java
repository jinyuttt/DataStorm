/**    
 * 文件名：UDPClient.java    
 *    
 * 版本信息：    
 * 日期：2017年5月26日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package JNetSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


/**    
 *     
 * 项目名称：JNetSocket    
 * 类名称：UDPClient    
 * 类描述：    udp客户端
 * 创建人：jinyu    
 * 创建时间：2017年5月26日 下午11:49:16    
 * 修改人：jinyu    
 * 修改时间：2017年5月26日 下午11:49:16    
 * 修改备注：    
 * @version     
 *     
 */
public class UDPClient  {
    private  DatagramSocket socket =null;  //创建套接字
    private boolean isColse=false;
    private String localAddr;
    private int localPort;
    private String remoteAddr;
    private int remotePort;
    private boolean isBind=false;
    /**
     * 为外层封装使用的
     * 暂时不真正关闭
     */
    private boolean isProtocolClose=false;
    
public void bindLocal(String host,int port)
    {
        isBind=true;
        localAddr=host;
        localPort=port;
    }
    
public void sendData(String host,int port,byte[]data)
    {
        InetAddress address = null;
        try {
            if(socket==null)
            {
                socket =new DatagramSocket();
              if(isBind)
            {
                  SocketAddress local=new InetSocketAddress(localAddr,localPort);
                   socket.bind(local);
            }
            }
        } catch (SocketException e2) {
            e2.printStackTrace();
        } 
        try {
            address = InetAddress.getByName(host);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }  
        //服务器地址  
        //创建发送方的数据报信息  
        DatagramPacket dataGramPacket = new DatagramPacket(data, data.length, address, port);  
        try {
            socket.setSendBufferSize(128);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            socket.send(dataGramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }  //通过套接字发送数据  
        remoteAddr=host;
        remotePort=port;
        localAddr=socket.getLocalAddress().getHostAddress();
        localPort=socket.getLocalPort();
    }
     public void sendData(SocketAddress addr,byte[]data)
    {
        try {
            if(socket==null)
            {
            socket =new DatagramSocket();
            if(isBind)
            {
                  SocketAddress local=new InetSocketAddress(localAddr,localPort);
                   socket.bind(local);
            }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }  //创建套接字
        //创建发送方的数据报信息  
        DatagramPacket dataGramPacket = new DatagramPacket(data, data.length,addr);  
        try {
            socket.send(dataGramPacket);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  //通过套接字发送数据 
        remoteAddr=dataGramPacket.getAddress().getHostAddress();
        remotePort=dataGramPacket.getPort();
        localAddr=socket.getLocalAddress().getHostAddress();
        localPort=socket.getLocalPort();
    }
  
    public byte[]  getCallBackData()
      {
         if(isColse)
         {
             return null;
         }
         //接收服务器反馈数据  
         byte[] backbuf = new byte[65535];  
         DatagramPacket backPacket = new DatagramPacket(backbuf, backbuf.length);  
         try {
            socket.receive(backPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }  //接收返回数据  
        return backPacket.getData();
   }
    
    /*
     * 关闭socket
     */
    public void close()
   {
       isColse=true;
       socket.close();
   }
    /*
     * 是否关闭socket;
     */
    public boolean isClose()
    {
        return isColse;
    }
  
public String getLocalHost()
  {
      return this.localAddr;
  }
  
public int getLocalPort()
  {
      return this.localPort;
  }
 
public String getRemoteHost()
 {
     return this.remoteAddr;
 }
  
public int getRemotePort()
  {
      return this.remotePort;
  }

  /**
   * 外层封装关闭（逻辑上的）
   */
 public  void protocolClose()
{
      isProtocolClose=true;
}
 /*
  * 返回外层是否关闭
  */
public  boolean isProtocolClose()
{
    return isProtocolClose;
}
}
