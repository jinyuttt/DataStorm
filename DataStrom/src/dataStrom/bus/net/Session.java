package dataStrom.bus.net;



import java.io.Closeable;

public interface Session extends Closeable {
    int bufsize=1472;
   /**  
    * 接收一个SocketChannel的处理  
    * @param key  
    * @throws IOException  
    */  
   void handleAccept(Object socket) ;  
     
   /**  
    * 从一个SocketChannel读取信息的处理  
    * @param key  
    * @throws IOException  
    */  
   void handleRead(Object socket) ;  
     
   /**  
    * 向一个SocketChannel写入信息的处理  
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
