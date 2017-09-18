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

    // ��ʱʱ�䣬��λ����  
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
        // ����ѡ����  
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
        // ����ѡ����  
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
            // ����ѭ��,�ȴ�IO  
            while (true) {  
                // �ȴ�ĳ�ŵ�����(��ʱ)  
                try
                {
                if (selector.select(TimeOut) == 0) {// ����ע��ͨ������������ע��� IO  
                                                    // �������Խ���ʱ���ú������أ�������Ӧ��  
                                                    // SelectionKey ���� selected-key  
                                                    // set  
              
                    continue;  
                }  
                }
                catch(Exception ex)
                {
                    continue;  
                }
                // ȡ�õ�����.selectedKeys()�а�����ÿ��׼����ĳһI/O�������ŵ���SelectionKey  
                // Selected-key Iterator ����������ͨ�� select() ������⵽���Խ��� IO ������ channel  
                // ��������Ͽ���ͨ�� selectedKeys() �õ�  
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
                        // �пͻ�����������ʱ  
                        session.handleAccept(key);  
                    }  
                    if (key.isReadable()) {// �ж��Ƿ������ݷ��͹���  
                        // �ӿͻ��˶�ȡ����  
                        session.handleRead(key);  
                    }  
                    if (key.isValid() && key.isWritable()) {// �ж��Ƿ���Ч�����Է��͸��ͻ���  
                        // �ͻ��˿�дʱ  
                        session.handleWrite(key);  
                    }  
                    // �Ƴ�������ļ�  
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
