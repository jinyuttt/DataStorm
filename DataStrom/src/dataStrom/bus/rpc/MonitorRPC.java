/**
 * 
 */
package dataStrom.bus.rpc;

import java.util.ArrayList;

/**
 * @author jinyu
 *udp时监视执行情况
 */
public class MonitorRPC {
    private ArrayList<MonitorSeq> list=new ArrayList<MonitorSeq>();
   public boolean isEmpty()
   {
        return list.isEmpty();
   }
    /*
     * 添加监视
     */
    public void add(MonitorSeq  monitor)
    {
        list.add(monitor);
    }
    
    /*
     * 移除
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
     * 查找
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
     * 移除任务监视
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
