/**
 * 
 */
package dataStrom.bus.topic;

import java.util.ArrayList;

/**
 * @author jinyu
 *
 */
public class PubSubData {
public ArrayList<byte[]> list=new ArrayList<byte[]>();
public void add(byte[]data)
{
	list.add(data);
}
public byte[] remove()
{
	if(list.isEmpty())
	{
		return null;
	}
	else
	{
    return	list.remove(0);
	}
}

public int getSize()
{
	return list.size();
}
}
