/**    
 * 文件名：YWModel.java    
 *    
 * 版本信息：    
 * 日期：2017年6月10日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package ComModel;

import Util.IDataPackaget;

/**    
 *     
 * 项目名称：BillModel    
 * 类名称：YWModel    
 * 类描述：   业务model 功能描述
 * 创建人：jinyu    
 * 创建时间：2017年6月10日 下午6:26:09    
 * 修改人：jinyu    
 * 修改时间：2017年6月10日 下午6:26:09    
 * 修改备注：    
 * @version     
 *     
 */
public class YWModel {
    /*
     * 来源Ip
     */
public String srcIP;
/*
 * 来源端口
 */
public int srcPort;
/*
 * 数据
 */
public IDataPackaget data;

/**
 * 是否需要回传数据
 */
public boolean reqCall=false;
/*
 * 通讯协议类型 0 tcp  1 udp  2 组播 3 广播
 */
public int  netType;

/*
 * 需要socket通讯时保持
 * 一般直接保持服务端的socket或者tcp客户端
 */
public Object socket;//
}
