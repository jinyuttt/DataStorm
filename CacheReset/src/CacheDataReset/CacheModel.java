/**    
 * 文件名：CacheModel.java    
 *    
 * 版本信息：    
 * 日期：2017年6月10日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package CacheDataReset;

import JNetSocket.UDPClient;

/**    
 *     
 * 项目名称：DataStromServer    
 * 类名称：CacheModel    
 * 类描述：   内存缓存数据
 * 创建人：jinyu    
 * 创建时间：2017年6月10日 下午3:34:15    
 * 修改人：jinyu    
 * 修改时间：2017年6月10日 下午3:34:15    
 * 修改备注：    
 * @version     
 *     
 */
public class CacheModel {
public UDPClient client;
public String remoteHost;
public int remotePort;
public byte[] data;
}
