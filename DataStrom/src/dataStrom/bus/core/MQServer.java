/**
 * 
 */
package dataStrom.bus.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import dataStrom.bus.config.MqConfig;
import dataStrom.bus.mq.MQMessage;
import dataStrom.bus.net.NetFactory;
import dataStrom.bus.net.Server;
import dataStrom.bus.net.Session;

/**
 * @author jinyu
 * MQ服务器
 */
public class MQServer {
    ExecutorService cachedPool = Executors.newCachedThreadPool();  
   // private static final Logger log = Logger.getLogger(MQServer.class.getName());
public void start(MqConfig config)
{
    String[]addrs=config.address.split(":");
    String address="";
    int port=0;
    if(addrs.length==0)
    {
        port=Integer.valueOf(addrs[0]);
    }
    else if(addrs.length==2)
    {
        address=addrs[0];
        port=Integer.valueOf(addrs[1]);
        if(address.equals("*"))
        {
            address="";
        }
    }
  Server netServer=  NetFactory.createServer(config.netType);
  boolean isSucess=true;
  if(address.isEmpty())
  {
      try {
        netServer.start(port);
      
    } catch (Exception e) {
        isSucess=false;
        e.printStackTrace();
    }
  }
  else
  {
      try {
        netServer.start(address, port);
    } catch (Exception e) {
        isSucess=false;
        e.printStackTrace();
    }
  }
  if(isSucess)
  {
   while(true)
   {
      Session session=netServer.getSession();
      byte[] data=session.handleReadData();
      MQMessage mqmsg=new MQMessage(data,true);
      cachedPool.execute(new Runnable() {

        @Override
        public void run() {
            MQHander hand=new MQHander();
            hand.process(mqmsg, session);
//            log.info("收到信息");
        }
          
      });
   }
  }
}
}
