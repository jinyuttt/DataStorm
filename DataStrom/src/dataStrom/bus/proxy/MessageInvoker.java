/**
 * 
 */
package dataStrom.bus.proxy;

import dataStrom.bus.mq.MQMessage;
import dataStrom.bus.mq.Message;


/**
 * @author jinyu
 *
 */
public class MessageInvoker<REQ, RES> {
public  MQMessage convertMsg(REQ req)
{
     if(req instanceof Message)
     {
         return  new MQMessage((Message)req);
     }
     else if(req instanceof MQMessage)
     {
         return (MQMessage) req;
     }
    return null;
}
public  MQMessage convertResult(RES res)
{
    if(res instanceof Message)
    {
        return  new MQMessage((Message)res);
    }
    else if(res instanceof MQMessage)
    {
        return (MQMessage) res;
    }
   return null;
    
}
@SuppressWarnings("unchecked")
public  RES  convertResult(MQMessage msg)
{
   return (RES) msg;
    
}
}
