/**
 * 
 */
package dataStrom.bus.rpc;

import dataStrom.bus.mq.RPCCode;
import dataStrom.bus.net.Session;

/**
 * @author jinyu
 * 任务信息
 */
public class MonitorSeq {
   public  Session session;
    public long id;//rpcid
    public long taskid=-1;
    public long time;
    public String name="";
    public String address="";
    public RPCCode result=RPCCode.request;
}
