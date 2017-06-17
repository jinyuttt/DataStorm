/**    
 * �ļ�����StromCenterProcess.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��17��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package DataProcess;

import DataStrom.ServerBus;
import StromModel.ServerModel;
import StromModel.StromServerNode;
import hashingAlgorithm.cirALL;


/**    
 *     
 * ��Ŀ���ƣ�DataStrom    
 * �����ƣ�StromCenterProcess    
 * ��������    ����ҵ��
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��17�� ����3:21:43    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��17�� ����3:21:43    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class StromCenterBiil {
  private  cirALL<ServerModel> cir=new cirALL<ServerModel>();
    /**
     * ����ʹ�õķ�����Ϣ
     */
public ServerModel getServerAddr(String name)
{
    StromServerNode node=ServerBus.serverList.get(name);
    ServerModel tmp=null;
    if(node==null)
    {
        return null;
    }
    if(node.master_slave)
    {
       tmp= node.getMaster();
       if(tmp==null)
       {
            tmp=node.getFirst();
            if(tmp==null)
            {
                return null;
            }
            else
            {
                node.setMaster(tmp);
            }
       }
       else
       {
           if(tmp.isAction())
           {
               return tmp;
           }
           else
           {
               tmp=node.getFirst();
               if(tmp==null)
               {
                   return null;
               }
               else
               {
                   node.setMaster(tmp);
               }
           }
       }
    }
    else
    {
         tmp=cir.GetCurNode(node.getServerList());
         if(!tmp.isAction())
         {
             node.remove(tmp);
             getServerAddr(name);
         }
       
    }
    
    return tmp;
    
}
public void DataToServer(byte[]data,boolean isCall)
{
    
}
}
