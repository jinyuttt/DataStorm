/**
 * 
 */
package dataStrom.bus.net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import dataStrom.bus.net.Session;

/**
 * @author jinyu
 *
 */
public class udpSession implements Session {
private DatagramSocket srvsocket=null;
private InetAddress host;
private int port;
private byte[] data=null;
private static final Logger log = Logger.getLogger(udpSession.class.getName());
public udpSession(DatagramSocket socket,InetAddress addr,int cport)
{
    srvsocket=socket;
    this.host=addr;
    this.port=cport;
}
public void addData(byte[]buf)
{
     data=buf;
}
    /* (non-Javadoc)
     * @see java.io.Closeable#close()
     */
    @Override
    public void close() throws IOException {
     

    }

    /* (non-Javadoc)
     * @see dataStrom.bus.net.Session#handleAccept(java.lang.Object)
     */
    @Override
    public void handleAccept(Object socket) {
     

    }

    /* (non-Javadoc)
     * @see dataStrom.bus.net.Session#handleRead(java.lang.Object)
     */
    @Override
    public void handleRead(Object socket) {
    

    }

    /* (non-Javadoc)
     * @see dataStrom.bus.net.Session#handleWrite(java.lang.Object)
     */
    @Override
    public void handleWrite(Object socket) {
     this.srvsocket=(DatagramSocket) socket;

    }

    /* (non-Javadoc)
     * @see dataStrom.bus.net.Session#id()
     */
    @Override
    public String id() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see dataStrom.bus.net.Session#getRemoteAddress()
     */
    @Override
    public String getRemoteAddress() {
    return   this.host.getHostAddress();
    }

    /* (non-Javadoc)
     * @see dataStrom.bus.net.Session#getLocalAddress()
     */
    @Override
    public String getLocalAddress() {
     return   srvsocket.getLocalAddress().getHostAddress();
    }

    /* (non-Javadoc)
     * @see dataStrom.bus.net.Session#write(java.lang.Object)
     */
    @Override
    public void write(Object msg) {
        writeAndFlush( msg);

    }

    /* (non-Javadoc)
     * @see dataStrom.bus.net.Session#writeAndFlush(java.lang.Object)
     */
    @Override
    public void writeAndFlush(Object msg) {
        ByteBuffer buf=ByteBuffer.allocate(256);
    if(msg instanceof byte[])
    {
        byte[] data=(byte[]) msg;
        buf=ByteBuffer.wrap(data);
      
    }
    else if(msg instanceof Long)
    {
        buf.putLong((long) msg);
    }
    else if(msg instanceof Integer)
    {
        buf.putInt((int) msg);
    }
    else if(msg instanceof Short)
    {
        buf.putShort((short) msg);
    }
    else if(msg instanceof Byte)
    {
        buf.put((byte) msg);
    }
    else if(msg instanceof String)
    {
        byte[] data=((String) msg).getBytes();
        buf=ByteBuffer.wrap(data);
    }
    else
    {
        //
    }
    //
    byte[]sendBuf=new byte[buf.limit()];
    buf.get(sendBuf);
    DatagramPacket sendPacket 
    = new DatagramPacket(sendBuf , sendBuf.length , host , port );
    try {
        srvsocket.send(sendPacket);
        log.info("»Ø·¢ËÍ"+host+";"+port);
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    /* (non-Javadoc)
     * @see dataStrom.bus.net.Session#flush()
     */
    @Override
    public void flush() {
      

    }

    /* (non-Javadoc)
     * @see dataStrom.bus.net.Session#isActive()
     */
    @Override
    public boolean isActive() {
       
        return false;
    }

    /* (non-Javadoc)
     * @see dataStrom.bus.net.Session#attr(java.lang.String)
     */
    @Override
    public <V> V attr(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see dataStrom.bus.net.Session#attr(java.lang.String, java.lang.Object)
     */
    @Override
    public <V> void attr(String key, V value) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see dataStrom.bus.net.Session#handleReadData()
     */
    @Override
    public byte[] handleReadData() {
    return data;
    }
    @Override
    public void setRemote(String host, int port) {
      try {
        this.host=InetAddress.getByName(host);
    } catch (UnknownHostException e) {
        e.printStackTrace();
    }
      this.port=port;
        
    }
    @Override
    public int getRemotePort() {
     return this.port;
    }

}
