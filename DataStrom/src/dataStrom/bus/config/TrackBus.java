/**
 * 
 */
package dataStrom.bus.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import dataStrom.bus.core.TrackHash;
import dataStrom.bus.core.TrackNode;

/**
 * @author jinyu
 *生产者消费者使用
 *程序内部转换
 *外部不能用，
 */
public class TrackBus {
    /*
     * 初始化节点
     */
    public void   busAddress(String[] addressList)
    { 
        TrackNode node=new TrackNode();
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
        TrackConfig.node=node;
        if(addressList.length>1)
        {
            TrackConfig.Istracker=true;
        }
        else
        {
            TrackConfig.Istracker=false;
        }
    }
}
