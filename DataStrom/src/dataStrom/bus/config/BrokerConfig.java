/**
 * 
 */
package dataStrom.bus.config;

/**
 * @author jinyu
 * �����ַ��Ϣ
 */
public class BrokerConfig {
public String addressList="127.0.0.1:6666";
public String netType="udp";
public String mqname="MQ";//mq���ƣ�topic���ƣ�rpc��������)
public int speed=1000;//ÿ��
public int timeOut=20;

/**
 * �Ƿ�������
 * ������е�������udpʱ��ʵʱ��������̽���
 * �Ƕ���ֱ�ӷ��ͣ���udpʱ���ܻᶪ
 * ֻ������һ��
 */
public boolean isDirect=false;

/**
 * �Ƿ�ȴ����Ӱ������ٴη���
 * ֻ�� isDirect=false��Ч
 */
public boolean iswaitAck=false;
}
