/**
 * 
 */
package dataStrom.bus.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import dataStrom.bus.Tools.CommTools;
import dataStrom.bus.config.MqConfig;
import dataStrom.bus.config.TrackConfig;


/**
 * @author jinyu
 *  集群控制
 */
public class TrackServer {
   private String localAddress="";
   private  String nettype="";
   private   TrackNode node=new TrackNode();
    public void start(TrackConfig config)
    {
        String[] addressList=config.addressList.split(";");
        boolean isfind=false;
        if(addressList.length==1)
        {
            localAddress=addressList[0];
            nettype=config.netType;
        }
        else
        {
            //
            ArrayList<String> list=   CommTools.getLocalIp();
            for(int i=0;i<addressList.length;i++)
            {
             
                if(addressList[i].indexOf("127.0.0.1")>-1)
                {
                    isfind=true;
                   
                }
                else if(addressList[i].indexOf("*")>-1)
                {
                    isfind=true;
                }
                else
                {
                    String[] ip=addressList[i].split(":");
                    if(list.get(i).indexOf(ip[0])>-1)
                    {
                        isfind=true;
                    }
                }
                //
                if(isfind)
                {
                    localAddress=addressList[i];
                    nettype=config.netType;
                    break;
                }
            }
        }
       //
        if(isfind)
        {
            TrackConfig.Istracker=true;
            //说明是集群
            copyMQ(addressList);
            TrackConfig.node=this.node;
        }
        if(TrackConfig.node==null)
        {
            TrackConfig.node=this.node; 
        }
        TrackConfig.node.localAddress=localAddress;
        TrackConfig.node.netType=nettype;
        mqStart();
    }
    
    /*
     * 启动本机节点
     */
    private void mqStart()
    {
        MQServer server=new MQServer();
        MqConfig mqconfig=new MqConfig();
        mqconfig.address=localAddress;
        mqconfig.netType=nettype;
        server.start(mqconfig);
    }
    
    /*
     * 初始化节点
     */
    private void copyMQ(String[] addressList)
    { 
        HashMap<String,Long> trackKey=new   HashMap<String,Long>();
        HashMap<Long,String> trackID=new   HashMap<Long,String>();
        LinkedList<Long> list=   new  LinkedList<Long>();
        for(int i=0;i<addressList.length;i++)
        {
            trackKey.put(addressList[i], TrackHash.hash(addressList[i]));
            trackID.put(TrackHash.hash(addressList[i]), addressList[i]);
            list.add(TrackHash.hash(addressList[i]));
        }
        Collections.sort(list);
        node.hashID=trackID;
        node.hashKey=trackKey;
        node.list=list;
        node.localAddress=localAddress;
        node.netType=nettype;
        node.localHashID=trackKey.get(localAddress);
        //node.nextAddress=
        boolean isfind=false;
        for(Long hashid:list)
        {
            if(isfind)
            {
                node.nextHashID=hashid;
                node.nextAddress=trackID.get(hashid);
                break;
            }
            if(hashid== node.localHashID)
            {
                //找到本机
                isfind=true;
            }
        }
    }
}
