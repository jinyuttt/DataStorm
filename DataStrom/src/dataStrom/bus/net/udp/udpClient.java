/**
 * 
 */
package dataStrom.bus.net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import dataStrom.bus.net.Client;
import dataStrom.bus.net.NetType;

/**
 * @author jinyu
 *
 */
@NetType("udp_Client")
public class udpClient implements Client{
    private static final Logger log = Logger.getLogger(udpClient.class.getName());
    private  String host;
   private int port;
   private  DatagramSocket client = null;
   public udpClient()
   {
       try {
        client = new DatagramSocket();
    } catch (SocketException e) {
        e.printStackTrace();
    }
   }
    @Override
    public void setHost(String host, int port) {
      this.host=host;
      this.port=port;
        
    }

    @Override
    public boolean connect() {
       
        return true;
    }

    @Override
    public boolean connect(int timeout) {
     
        return true;
    }

    @Override
    public void sendData(byte[] data) { 
        if(data==null)
        {
            return;
        }
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        DatagramPacket sendPacket 
            = new DatagramPacket(data ,data.length , addr , port);
        try {
            client.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void close() {
        client.close();
        
    }

    @Override
    public int read(byte[] data) {
     
        DatagramPacket recvPacket
            = new DatagramPacket(data , data.length);
        try {
            client.receive(recvPacket);
            return recvPacket.getLength();
        } catch (IOException e) {
            log.warning("发送端读取数据失败："+e.getMessage());
        }
        return -1;
    }

    @Override
    public byte[] read() {
     byte[] recBuf=new byte[65535];
    byte[] sum=new byte[65535];
    int sumLen=0;
      while(true)
      {
          int r=   read(recBuf);
          if(r<0)
          {
              break;
          }
          if(r<65535)
          {
              if(sumLen+r<sum.length)
              {
                  System.arraycopy(recBuf, 0, sum, sumLen, r);
                  sumLen+=r;
                  break;
              }
              else
              {
                  byte[]tmp=sum;
                  sum=new byte[sumLen+r];
                  System.arraycopy(tmp, 0, sum, 0, tmp.length);
                  System.arraycopy(recBuf, 0, sum, tmp.length, r);
                  sumLen+=r;
                  tmp=null;
                  break;
              }
          }
          else
          {
              if(sumLen+r<sum.length)
              {
                  System.arraycopy(recBuf, 0, sum, sumLen, r);
               
              }
              else
              {
                  byte[]tmp=sum;
                  sum=new byte[sumLen+2*r];
                  System.arraycopy(tmp, 0, sum, 0, tmp.length);
                  System.arraycopy(recBuf, 0, sum, tmp.length, r);
                  tmp=null;
               
              }
              sumLen+=r;
          }
      }
      if(sumLen==sum.length)
      {
          return sum;
      }
      else
      {
          byte[]data=new byte[sumLen];
          System.arraycopy(sum, 0, data,0, sumLen);
          sum=null;
          return data;
      }
    }
    @Override
    public boolean join() {
        return false;
    }

}
