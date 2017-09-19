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
        // 返回创建此键的通道，接受客户端建立连接的请求，并返回 SocketChannel 对象  
        SelectionKey key=(SelectionKey)socket;
        try
        {
        SocketChannel clientChannel = ((ServerSocketChannel) key.channel())  .accept();  
        // 非阻塞式  
        clientChannel.configureBlocking(false);  
        // 注册到selector  
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
           //  获得与客户端通信的信道  
        SelectionKey key=(SelectionKey)socket;
      SocketChannel clientChannel = (SocketChannel) key.channel();  
      // 得到并清空缓冲区  
      ByteBuffer buffer = (ByteBuffer) key.attachment();  
      buffer.clear();  
      try
      {
      // 读取信息获得读取的字节数  
      long bytesRead = clientChannel.read(buffer);  
      if (bytesRead == -1) {  
          // 没有读取到内容的情况  
          clientChannel.close();  
      } else {  
          // 将缓冲区准备为数据传出状态  
          buffer.flip();  
      }
      }
      catch(Exception ex)
      {
          log.warning(ex.getMessage());
        
      }
          // 设置为下一次读取或是写入做准备  
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
