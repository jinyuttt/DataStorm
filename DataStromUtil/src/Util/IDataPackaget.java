/**    
 * 文件名：IDataPackaget.java    
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
 * 类名称：IDataPackaget    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2017年6月10日 上午2:05:59    
 * 修改人：jinyu    
 * 修改时间：2017年6月10日 上午2:05:59    
 * 修改备注：    
 * @version     
 *     
 */
public abstract class IDataPackaget {
    /**
     * 包类型
     * 0 服务信息
     * 1 服务状态
     * 2  客户端请求
     * 3 返回包
     * 4 数据包
     * 5 服务信息包
     * 6 注册中心包询问
     * 7 注册中心服务交互
     */
public  byte packagetType;
/**
 * 服务名称
 */
public String serverName;

/**
 * session id 只是一个标识
 */
public long sessionid;

/*
 * 是否明确需要回执
 * 0不需要
 * 1需要
 */
public byte rsp=0;
}
