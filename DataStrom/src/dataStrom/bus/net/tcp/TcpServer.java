/**
 * 
 */
package dataStrom.bus.net.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.concurrent.SynchronousQueue;

import dataStrom.bus.net.NetType;
import dataStrom.bus.net.Server;
import dataStrom.bus.net.Session;

/**
 * @author jinyu
 *
 */
@NetType("tcp_Server")
public class TcpServer  implements Server {
    SynchronousQueue<TcpSession> queue=new SynchronousQueue<TcpSession>();
    ServerSocketChannel serverSocketChannel=null;
    Selector selector=null;
    int  localport=0;

    // 超时时间，单位毫秒  
    private static final int TimeOut = 3000;  
    
    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void start(int port) throws Exception {
       serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));  
        serverSocketChannel.configureBlocking(false);  
        localport=port;
        // 创建选择器  
         selector = Selector.open();  
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);  
        processSocket();
    }

    @Override
    public void start(String host, int port) throws Exception {
       serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(host,port));  
        serverSocketChannel.configureBlocking(false);  
        localport=port;
        // 创建选择器  
         selector = Selector.open();  
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);  
        processSocket();
    }

    @Override
    public void join() throws InterruptedException {
        // TODO Auto-generated method stub
        
    }
  private void processSocket()
  {
    Thread recSocket=new Thread(new Runnable() {
    

        @Override
        public void run() {
            // 反复循环,等待IO  
            while (true) {  
                // 等待某信道就绪(或超时)  
                try
                {
                if (selector.select(TimeOut) == 0) {// 监听注册通道，当其中有注册的 IO  
                                                    // 操作可以进行时，该函数返回，并将对应的  
                                                    // SelectionKey 加入 selected-key  
                                                    // set  
              
                    continue;  
                }  
                }
                catch(Exception ex)
                {
                    continue;  
                }
                // 取得迭代器.selectedKeys()中包含了每个准备好某一I/O操作的信道的SelectionKey  
                // Selected-key Iterator 代表了所有通过 select() 方法监测到可以进行 IO 操作的 channel  
                // ，这个集合可以通过 selectedKeys() 拿到  
                TcpSession session=new TcpSession();
                try {
                    queue.put(session);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();  
                while (keyIter.hasNext()) {  
                    SelectionKey key = keyIter.next(); 
                    if (key.isAcceptable()) {  
                        // 有客户端连接请求时  
                        session.handleAccept(key);  
                    }  
                    if (key.isReadable()) {// 判断是否有数据发送过来  
                        // 从客户端读取数据  
                        session.handleRead(key);  
                    }  
                    if (key.isValid() && key.isWritable()) {// 判断是否有效及可以发送给客户端  
                        // 客户端可写时  
                        session.handleWrite(key);  
                    }  
                    // 移除处理过的键  
                    keyIter.remove();  
            }  
            }
        }
    });
    recSocket.setDaemon(true);
    recSocket.setName("tcpserver_"+localport);
    recSocket.start();
    
      
  }

@Override
public Session getSession() {
    try {
        return queue.take();
    } catch (InterruptedException e) {
        e.printStackTrace();
        return null;
    }
  
    
}

@Override
public String getlocahost() {
    // TODO Auto-generated method stub
    return null;
}

@Override
public int getLocalPort() {
    // TODO Auto-generated method stub
    return 0;
}


}
