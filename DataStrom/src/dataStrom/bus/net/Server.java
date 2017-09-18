package dataStrom.bus.net;

import java.io.Closeable;

public interface Server extends Closeable{ 
	void start(int port ) throws Exception;
	void start(String host, int port) throws Exception; 
	 void join() throws InterruptedException;
      Session getSession();
      String getlocahost();
      int  getLocalPort();
}
