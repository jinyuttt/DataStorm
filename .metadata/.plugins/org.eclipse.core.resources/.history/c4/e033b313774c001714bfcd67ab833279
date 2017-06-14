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
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ISocket.ISocketClient;

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
public class UDPClient  implements ISocketClient{
    private  DatagramSocket socket =null;  //创建套接字
    private boolean isColse=false;
    private String addr;
    private int port;
    public void sendData(String host,int port,byte[]data)
    {
        InetAddress address = null;
        try {
            socket =new DatagramSocket();
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
    }
   
     public void sendData(SocketAddress addr,byte[]data)
    {
        try {
            socket =new DatagramSocket();
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
    }
  
    public byte[]  getCallBackData()
      {
         if(isColse)
         {
             return null;
         }
         //接收服务器反馈数据  
         byte[] backbuf = new byte[1024];  
         DatagramPacket backPacket = new DatagramPacket(backbuf, backbuf.length);  
         try {
            socket.receive(backPacket);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  //接收返回数据  
        return backPacket.getData();
   }
    
    public void close()
   {
       isColse=true;
       socket.close();
   }

@Override
  public void sendData(SocketAddress local, String sIP, int sPort, byte[] data) {
    
}

@Override
public void sendData(byte[] data) {
    InetAddress address = null;
    try {
        address = InetAddress.getByName(addr);
    } catch (UnknownHostException e1) {
        e1.printStackTrace();
    }  
    DatagramPacket backPacket = new DatagramPacket(data, data.length, address, port);  
    try {
        this.socket.send(backPacket);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

@Override
public void setSocket(Object socket, String[] args) {
this.socket=(DatagramSocket) socket;
if(args!=null&&args.length==2)
{
this.addr=args[0];
this.port=Integer.parseInt(args[1]);
}
}


  
}
