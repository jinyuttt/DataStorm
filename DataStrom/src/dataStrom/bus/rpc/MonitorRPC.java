/**
 * 
 */
package dataStrom.bus.rpc;

import java.util.ArrayList;

/**
 * @author jinyu
 *udpʱ����ִ�����
 */
public class MonitorRPC {
    private ArrayList<MonitorSeq> list=new ArrayList<MonitorSeq>();
   public boolean isEmpty()
   {
        return list.isEmpty();
   }
    /*
     * ��Ӽ���
     */
    public void add(MonitorSeq  monitor)
    {
        list.add(monitor);
    }
    
    /*
     * �Ƴ�
     */
    public MonitorSeq remove()
    {
        if(list.isEmpty())
        {
            return null;
        }
        else
        {
           return  list.remove(0);
        }
    }
    
    /*
     * ����
     */
    public MonitorSeq find(long  taskid)
    {
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).id==taskid)
            {
                //
                return list.get(i);
            }
        }
        return null;
    }
    
    /*
     * �Ƴ��������
     */
    public void remove(long taskid)
    {
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).id==taskid)
            {
                //
                 list.remove(i);
                 break;
            }
        }
    }
}
