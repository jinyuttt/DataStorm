/**    
 * 文件名：ConfigModel.java    
 *    
 * 版本信息：    
 * 日期：2017年6月14日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package Config;

/**    
 *     
 * 项目名称：DataStromServer    
 * 类名称：ConfigModel    
 * 类描述：    中心注册地址
 * 中心接收的IP,端口
 * 中心统一使用的组播地址
 * 组播自己接收状态包（充当注册）
 * 创建人：jinyu    
 * 创建时间：2017年6月14日 下午10:46:45    
 * 修改人：jinyu    
 * 修改时间：2017年6月14日 下午10:46:45    
 * 修改备注：    
 * @version     
 *     
 */
public class ConfigModel {
  
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
 * 
 */
public byte centerByte=0;

/**
 * 当前可用
 */
public boolean action=true;

public String flage;
}
