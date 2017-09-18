/**
 * 
 */
package dataStrom.bus.net.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.SynchronousQueue;

import dataStrom.bus.net.Client;
import dataStrom.bus.net.NetType;

/**
 * @author jinyu
 * 
 *
 */
@NetType("tcp_Client")
public class TcpClient  implements  Client{
    // �ŵ�ѡ����  
    private Selector selector;  
  
    // �������ͨ�ŵ��ŵ�  
    SocketChannel socketChannel;  
  
    // Ҫ���ӵķ�����Ip��ַ  
    private String hostIp;  
    // Ҫ���ӵ�Զ�̷������ڼ����Ķ˿�  
    private int hostListenningPort; 
    SynchronousQueue<byte[]> queue=new SynchronousQueue<byte[]>();
    byte[] curleft=null;
    public TcpClient()
    {
      
    }
    @Override
    public boolean connect() {
        try
        {
        socketChannel = SocketChannel.open(new InetSocketAddress(hostIp,  
                hostListenningPort));  
        socketChannel.configureBlocking(false);  
        // �򿪲�ע��ѡ�������ŵ�  
        selector = Selector.open();  
        socketChannel.register(selector, SelectionKey.OP_READ);  
        readThread();
        return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }

    @Override 
    public void sendData(byte[] data) {
        ByteBuffer writeBuffer = ByteBuffer.wrap(data);  
        try {
            socketChannel.write(writeBuffer);
        } catch (IOException e) {
           
            e.printStackTrace();
        }  
        
    }

    @Override
    public void close() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public int read(byte[] data) {
        int r=0;
        int len=0;
      try {
     
        if(curleft!=null)
        {
            if(curleft.length>=data.length)
            {
                System.arraycopy(curleft, 0, data, 0, data.length);
                curleft=new byte[curleft.length-data.length];
                if(curleft.length==0)
                {
                    curleft=null;
                }
                else
                {
                    System.arraycopy(curleft, data.length, curleft, 0, curleft.length);
                }
                r=data.length;
                return r;
            }
            else
            {
                System.arraycopy(curleft, 0, data, 0,curleft.length);
                len=curleft.length;
                curleft=null;
                
            }
        }
        byte[] buf= queue.take();
        if(buf.length+len>data.length)
        {
            System.arraycopy(buf, 0, data, len, data.length-len);
            curleft=new byte[buf.length-data.length-len];
            System.arraycopy(buf, data.length-len, curleft, 0, curleft.length);
            r=data.length;
        }
        else
        {
            System.arraycopy(buf, 0, data, len, buf.length);
            r=buf.length;
         
        }
        return r;
    } catch (InterruptedException e) {
  
        e.printStackTrace();
    }
    return 0;
    }

    @Override
    public byte[] read() {
        return null;
    }

    @Override
    public void setHost(String host, int port) {
        this.hostIp=host;
        this.hostListenningPort=port;
    }
  private void   readThread()
  {
      Thread rd=new Thread(new Runnable() {

        @Override
        public void run() {
            try {  
                Thread.currentThread().setName("tcpClient");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                 
                    e.printStackTrace();
                }
                while (selector.select() > 0) {//select()����ֻ��ʹ��һ�Σ�����֮��ͻ��Զ�ɾ��,ÿ�����ӵ���������ѡ�������Ƕ�����  
                    // ����ÿ���п���IO����Channel��Ӧ��SelectionKey  
                    for (SelectionKey sk : selector.selectedKeys()) {  
                        // �����SelectionKey��Ӧ��Channel���пɶ�������  
                        if (sk.isReadable()) {  
                            // ʹ��NIO��ȡChannel�е�����  
                            SocketChannel sc = (SocketChannel) sk.channel();//��ȡͨ����Ϣ  
                            ByteBuffer buffer = ByteBuffer.allocate(1024);//���仺������С  
                           int r= sc.read(buffer);//��ȡͨ����������ݷ��ڻ�������  
                            // Ϊ��һ�ζ�ȡ��׼��  
                           if(r==1024)
                           {
                               try {
                                queue.put(buffer.array());
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                           }
                           else
                           {
                               byte[] buf=new byte[r];
                               buffer.get(buf);
                               try {
                                queue.put(buf);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                           }
                            sk.interestOps(SelectionKey.OP_READ);  
                        }  
                        // ɾ�����ڴ����SelectionKey  
                        selector.selectedKeys().remove(sk);  
                    }  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
            
        }
          
      });
      rd.setDaemon(true);
      rd.setName("tcpclient");
      rd.start();
  }
@Override
public boolean connect(int timeout) {
    // TODO Auto-generated method stub
    return false;
}
@Override
public boolean join() {
    // TODO Auto-generated method stub
    return false;
}
}
