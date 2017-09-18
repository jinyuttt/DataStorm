package dataStrom.bus.net;
 
/*
 * 客户端接口
 */
public interface Client {
    
    public void setHost(String host,int port);
    public boolean connect();
    public boolean connect(int timeout);
    public void sendData(byte[]data);
    public void close();
    public int read(byte[]data);
    public byte[]read();
    public boolean join();
}
