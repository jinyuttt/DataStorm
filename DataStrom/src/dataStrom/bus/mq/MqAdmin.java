/**
 * 
 */
package dataStrom.bus.mq;

/**
 * @author jinyu
 *
 */
public interface MqAdmin {
    public void add(String name,byte[]data);
    public byte[] getMQMsg(String name);
    public int queryLen(String name);
    public String[] queryMQ();
    public String[] queryCustomer(String name);
    void  addCustomer(String name,DataStromConsumer customer);
    DataStromConsumer[]   queryClientCustomer(String name);
    void readSingle();
}
