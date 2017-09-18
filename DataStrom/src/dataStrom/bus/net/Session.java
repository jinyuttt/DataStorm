package dataStrom.bus.net;



import java.io.Closeable;

public interface Session extends Closeable {
    int bufsize=1472;
   /**  
    * ����һ��SocketChannel�Ĵ���  
    * @param key  
    * @throws IOException  
    */  
   void handleAccept(Object socket) ;  
     
   /**  
    * ��һ��SocketChannel��ȡ��Ϣ�Ĵ���  
    * @param key  
    * @throws IOException  
    */  
   void handleRead(Object socket) ;  
     
   /**  
    * ��һ��SocketChannelд����Ϣ�Ĵ���  
    * @param key  
    * @throws IOException  
    */  
   void handleWrite(Object socket) ;  
	String id(); 
	String getRemoteAddress();
	
	String getLocalAddress();
	
	void write(Object msg);
	
	void writeAndFlush(Object msg);
	
	void flush();
	
	boolean isActive(); 
	
	<V> V attr(String key);
	
	<V> void attr(String key, V value);
	  byte[] handleReadData() ;  
}
