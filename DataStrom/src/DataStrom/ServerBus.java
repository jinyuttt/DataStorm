/**
 * 
 */
package DataStrom;

import java.util.HashMap;

import com.google.common.collect.HashMultimap;

import DataBus.CacheData;
import NetModel.NetAddress;
import StromModel.ConfigModel;
import StromModel.ServerModel;
import StromModel.StromServerNode;

/**
 * @author jinyu
 *
 */
public class ServerBus {
	/*
	 * 保存服务信息
	 * 当前主要用户节点间服务同步
	 */
public static  HashMultimap<String, ServerModel> map = HashMultimap.create();

/**
 * 保存所有注册中心；暂时不要
 */
public static  HashMultimap<String, ConfigModel> center = HashMultimap.create();

/**
 * 使用服务
 * 
 */
public static HashMap<String,StromServerNode> serverList=new HashMap<String,StromServerNode>();
public static CacheData<String,NetAddress> objSocket=new CacheData<String,NetAddress>(1000, 120, false);
}
