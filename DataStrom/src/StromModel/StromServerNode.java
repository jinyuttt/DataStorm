/**    
 * �ļ�����StromServerNode.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��17��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package StromModel;


import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**    
 *     
 * ��Ŀ���ƣ�DataStrom    
 * �����ƣ�StromServerNode    
 * ��������    ������Ϣ�����ڷ���ʹ��
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��17�� ����3:28:06    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��17�� ����3:28:06    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class StromServerNode {
    public StromServerNode()
    {
        serverList=new CopyOnWriteArraySet<ServerModel>();
    }
  /**
   * ��������
   */
public String  serverName;
/**
 * �Ƿ������Ӹ��Ʒ���
 */
 public boolean master_slave=false;
 
/**
 * ���Ӹ��Ʒ���ʱ��master
 */
 private  ServerModel master=null;
 //CopyOnWriteArrayList
 CopyOnWriteArraySet<ServerModel> serverList;
 
 /**
  * ���ӽ������
  */
 public void addServer(ServerModel server)
 {
     serverList.add(server);
 }
 
 /**
  * ��ȡ������
  * �����б������ӷ���ʱ
  */
 public ServerModel getMaster()
 {
     if(master_slave)
     {
         return master;
     }
     return null;
 }
 
 /**
  * ���ӷ��񣻷���һ��action�ķ���
  */
 public ServerModel getFirst()
 {
     //�ر𷵻�һ��action����
     if(master_slave)
     {
      
         Iterator<ServerModel>  it=serverList.iterator();
          while(it.hasNext())
          {
              ServerModel tmp=it.next();
              if(tmp.isAction())
              {
                  return tmp;
          }
         
     }
     }
    return null;
 }
 
/**
 * ֱ������������
 */
 public boolean setMaster(ServerModel server)
 {
       if(master_slave)
     {
           master=server;
           return true;
     }
       else
       {
           return false;
       }
 }
 
 /**
  * ���ӷ���
  * ���Ҹ���isMaster�Ƿ����server�������ӷ���
  */
 public void addServer(ServerModel server,boolean  isMaster)
 {
      //
     serverList.add(server);
     if(isMaster)
     {
         master_slave=server.master_slave;
         if(server.isMaster)
         {
             master=server;
         }
     }
     
 }
 
 /**
  * ֱ�����÷����б��Ƿ������ӷ���
  */
 public void setServerType(boolean isMaster)
 {
     master_slave=isMaster;
 }
 
 /**
  * ���ط����б�
  */
public  CopyOnWriteArrayList<ServerModel> getServerList()
{

    CopyOnWriteArrayList<ServerModel> list=new CopyOnWriteArrayList<ServerModel>();
     list.addAll(serverList);
     return list;
  
}
/*
 * ����״̬�����ʱ��
 */
public void update(ServerModel server)
{
   boolean isFind=false;
    Iterator<ServerModel>  it=serverList.iterator();
     while(it.hasNext())
     {
         ServerModel tmp=it.next();
         if(tmp.equals(server))
         {
             tmp.update();
             isFind=true;
             break;
         }
     }
     if(!isFind)
     {
         serverList.add(server);
         server.update();
     }
}

/*
 * ɾ������
 */
public void remove(ServerModel server)
{
    Iterator<ServerModel>  it=serverList.iterator();
    while(it.hasNext())
    {
        ServerModel tmp=it.next();
        if(tmp.equals(server))
        {
              it.remove();
              break;
        }
    }
}
}