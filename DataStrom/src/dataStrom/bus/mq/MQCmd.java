/**
 * 
 */
package dataStrom.bus.mq;

/**
 * @author jinyu
 *����ͨѶָ��
 *����ָ��ֻ����������
 */
public enum MQCmd {
 MQCreate,
 TopicCreate,
  RPCCreate,//����RPC����������Consumer
 MQMsg,//����������
 TopicMsg,//����������
 Query,//��ҳ��ѯ
 Heart,//RPC����
 MQSub,
 TopicSub,
 ResponseSub,//������Ϣ�ķ��أ���ʾ�Ѿ����ĳɹ�
 RPCMonitor,//
 RequestRPC,//����RPC
 ResponseRPC,//RPC����
 MQCopy,//��Ⱥ����
 MQCopyResponse,//��Ⱥ���ƻظ�
 MQAck;//̽���
}
