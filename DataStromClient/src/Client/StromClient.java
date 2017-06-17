package Client;
import EventBus.MessageBus;
import Util.ReqPackaget;

/**    
 * 文件名：StromClient.java    
 *    
 * 版本信息：    
 * 日期：2017年6月11日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */

/**    
 *     
 * 项目名称：DataStromClient    
 * 类名称：StromClient    
 * 类描述：    发送给中心的请求
 * 提交数据或者请求服务地址
 * 创建人：jinyu    
 * 创建时间：2017年6月11日 上午2:15:20    
 * 修改人：jinyu    
 * 修改时间：2017年6月11日 上午2:15:20    
 * 修改备注：    
 * @version     
 *     
 */
public class StromClient {
    ListenerReq listener=null;
    public StromClient()
    {
        listener=new ListenerReq();
        MessageBus.getBus("clientReq").register(listener);
    }
public void  sendRequest(ReqPackaget req)
{
    MessageBus.getBus("clientReq").post(req);
}
}
