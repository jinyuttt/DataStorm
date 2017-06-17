/**    
 * 文件名：MasterModel.java    
 *    
 * 版本信息：    
 * 日期：2017年6月15日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package Model;

import Util.IDataPackaget;

/**    
 *     
 * 项目名称：DataStromUtil    
 * 类名称：MasterModel    
 * 类描述：主中心相关处理model
 * 创建人：jinyu    
 * 创建时间：2017年6月15日 下午10:09:45    
 * 修改人：jinyu    
 * 修改时间：2017年6月15日 下午10:09:45    
 * 修改备注：    
 * @version     
 *     
 */
public class MasterModel extends IDataPackaget{
public MasterModel()
{
    this.packagetType=6;
}
/**
 * 注册中心地址
 */
public String  IP="host";

/*
* 注册中心端口
*/
public int port=3333;

/*
* 注册中心接收状态包IP
*/
public String multIP="224.0.1.21";

/*
* 注册中心状态端口
*/
public int    multPort=4444;

/**
* 中心状态
* 1 主中心
* 2 预计成为主中心
* 3. 一般存活中心
* 4  探寻主服务请求包
* 
*/
public byte centerByte=0;

/**
* 当前可用
*/
public boolean action=true;

/**
* 注册中心标识
*/
public String flage;
}
