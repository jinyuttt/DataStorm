/**    
 * 文件名：ServerState.java    
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
 * 类名称：ServerState    
 * 类描述：    服务状态；服务端发送给中心
 * 创建人：jinyu    
 * 创建时间：2017年6月10日 上午1:49:43    
 * 修改人：jinyu    
 * 修改时间：2017年6月10日 上午1:49:43    
 * 修改备注：    
 * @version     
 *     
 */
public class ServerState extends IDataPackaget{
    public ServerState()
    {
        this.packagetType=1;
    }
    public  String  serverName;
    public  String  srcIP;
    public int srcPort;
    public String flage;
    /*
     * 是否是主从服务
     */
    public boolean master_slave;

    /*
     * 是否是主服务
     */
    public boolean isMaster;
}
