/**
 * 
 */
package dataStrom.bus.net.tcp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import java.util.logging.Logger;

import dataStrom.bus.net.Session;

/**
 * @author jinyu
 *
 */
public class TcpSession implements Session{
    private int bufferSize; 
  final   Logger log=Logger.getLogger(TcpSession.class.getName());
    public TcpSession()
    {
        bufferSize=bufsize;
    }
    
    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String id() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRemoteAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getLocalAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void write(Object msg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void writeAndFlush(Object msg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isActive() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <V> V attr(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <V> void attr(String key, V value) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void handleAccept(Object socket) {
        // ���ش����˼���ͨ�������ܿͻ��˽������ӵ����󣬲����� SocketChannel ����  
        SelectionKey key=(SelectionKey)socket;
        try
        {
        SocketChannel clientChannel = ((ServerSocketChannel) key.channel())  .accept();  
        // ������ʽ  
        clientChannel.configureBlocking(false);  
        // ע�ᵽselector  
        clientChannel.register(key.selector(), SelectionKey.OP_READ,  
                ByteBuffer.allocate(bufferSize)); 
        }
        catch(Exception ex)
        {
            log.warning(ex.getMessage());
        }
        
    }
    @Override
    public void handleRead(Object socket) {
           //  �����ͻ���ͨ�ŵ��ŵ�  
        SelectionKey key=(SelectionKey)socket;
      SocketChannel clientChannel = (SocketChannel) key.channel();  
      // �õ�����ջ�����  
      ByteBuffer buffer = (ByteBuffer) key.attachment();  
      buffer.clear();  
      try
      {
      // ��ȡ��Ϣ��ö�ȡ���ֽ���  
      long bytesRead = clientChannel.read(buffer);  
      if (bytesRead == -1) {  
          // û�ж�ȡ�����ݵ����  
          clientChannel.close();  
      } else {  
          // ��������׼��Ϊ���ݴ���״̬  
          buffer.flip();  
      }
      }
      catch(Exception ex)
      {
          log.warning(ex.getMessage());
        
      }
          // ����Ϊ��һ�ζ�ȡ����д����׼��  
          key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);  
      
        
    }

    @Override
    public void handleWrite(Object socket) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public byte[] handleReadData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setRemote(String host, int port) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getRemotePort() {
        // TODO Auto-generated method stub
        return 0;
    }

}
