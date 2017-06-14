/**    
 * 文件名：RspPackaget.java    
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
 * 类名称：RspPackaget    
 * 类描述：   返回数据 ；所有回复，包括分包确认
 * 创建人：jinyu    
 * 创建时间：2017年6月10日 上午1:52:48    
 * 修改人：jinyu    
 * 修改时间：2017年6月10日 上午1:52:48    
 * 修改备注：    
 * @version     
 *     
 */
public class RspPackaget extends IDataPackaget{
    public RspPackaget()
    {
        this.packagetType=3;
    }
    public  String  serverName;
    public  byte[] result;
    public int packagetID;
}
