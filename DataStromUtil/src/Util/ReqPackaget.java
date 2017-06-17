/**    
 * 文件名：ReqPackaget.java    
 *    
 * 版本信息：    
 * 日期：2017年6月10日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package Util;

/**    
 *     
 * 项目名称：DataStromUtil    
 * 类名称：ReqPackaget    
 * 类描述：   客户端请求包 (向注册中心发送的包）
 * 创建人：jinyu    
 * 创建时间：2017年6月10日 上午1:52:33    
 * 修改人：jinyu    
 * 修改时间：2017年6月10日 上午1:52:33    
 * 修改备注：    
 * @version     
 *     
 */
public class ReqPackaget  extends IDataPackaget{
    public ReqPackaget()
    {
        this.packagetType=2;
    }
public  String  serverName;
/*
 * 0 由中心转服务
 * 1 获取服务地址
 */
public  byte  reqType;

/*
 * 转发时的数据
 */
public  byte[] args;

/*
 * 数据包ID
 */
public int packagetID;

/**
 * 转发时保留源地址
 */
public String srcAddr="";
}
