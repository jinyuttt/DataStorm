package hashingAlgorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class cirALL {
	static Random ran = new Random();
	/** key's count */
	private static final Integer EXE_TIMES = 100000;
	
	public static  Integer NODE_COUNT = 5;
	
	private static final Integer VIRTUAL_NODE_COUNT = 160;
	/**
	 * @return 返回所有节点
	 */
	public List<Node> GetNodes()
	{
		List<Node> allNodes =getNodes(NODE_COUNT);
		return allNodes;
	}
	/**
	 * @param listData
	 * @return 获取当前数据
	 */
	public Object GetCurNodeInfo(CopyOnWriteArrayList<Object> listData)
	{
		int i=0;
		NODE_COUNT=listData.size();
		List<Node> allNodes =getNodes(NODE_COUNT);
		Iterator<Node> it= allNodes.iterator();
		while(it.hasNext())
		{
			it.next().setObjData(listData.get(i));
			i++;
		}
		//
      	Object obj=	GetCurNode(allNodes);
      	if(obj==null)
      	{
       	return	listData.get(0);
      	}
      	else
      	{
      	return  obj;
      	}
		
	}
	/**
	 * @param allNodes
	 * @return  返回数据
	 */
	public LinkedList<String> GetHashNode(List<Node> allNodes)
	{
	
		KetamaNodeLocator locator = new KetamaNodeLocator(allNodes, HashAlgorithm.KETAMA_HASH, VIRTUAL_NODE_COUNT);
		List<String> allKeys =getAllStrings();
		for (String key : allKeys) {
			Node node = locator.getPrimary(key);
			return node.getDatas();
			
		}
		return null;
	}
	/**
	 * @param allNodes
	 * @return 返回数据
	 */
	public Object GetCurNode(List<Node> allNodes)
	{
	
		KetamaNodeLocator locator = new KetamaNodeLocator(allNodes, HashAlgorithm.KETAMA_HASH, VIRTUAL_NODE_COUNT);
		List<String> allKeys =getAllStrings();
		for (String key : allKeys) {
			Node node = locator.getPrimary(key);
			return node.getObjData();
			
		}
		return null;
	}
	
	/**
	 * @param nodeCount
	 * @return
	 */
	private List<Node> getNodes(int nodeCount) {
		List<Node> nodes = new ArrayList<Node>();
		
		for (int k = 1; k <= nodeCount; k++) {
			Node node = new Node("node" + k);
			nodes.add(node);
		}
		
		return nodes;
	}
	/**
	 *	All the keys	
	 */
	private List<String> getAllStrings() {
		List<String> allStrings = new ArrayList<String>(EXE_TIMES);
		
		for (int i = 0; i < EXE_TIMES; i++) {
			allStrings.add(generateRandomString(ran.nextInt(50)));
		}
		
		return allStrings;
	}
	
	/**
	 * To generate the random string by the random algorithm
	 * <br>
	 * The char between 32 and 127 is normal char
	 * 
	 * @param length
	 * @return
	 */
	private String generateRandomString(int length) {
		StringBuffer sb = new StringBuffer(length);
		
		for (int i = 0; i < length; i++) {
			sb.append((char) (ran.nextInt(95) + 32));
		}
		
		return sb.toString();
	}
}
