/**
 * 
 */
package dataStrom.bus.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author jinyu
 *��Ⱥ�ڵ�
 *������������ʹ��ʱû�б��ص�ַ
 */
public class TrackNode {
public String localAddress="";
public String nextAddress="";
public String netType="";
public long localHashID;
public long nextHashID;
public HashMap<String,Long> hashKey=null;
public List<Long> list  =  new ArrayList<Long>();
public HashMap<Long,String> hashID=null;
}
