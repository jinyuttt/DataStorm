/**    
 * 文件名：LogMsg.java    
 *    
 * 版本信息：    
 * 日期：2017年6月18日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package StromModel;

/**    
 *     
 * 项目名称：DataStrom    
 * 类名称：LogMsg    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2017年6月18日 下午11:37:57    
 * 修改人：jinyu    
 * 修改时间：2017年6月18日 下午11:37:57    
 * 修改备注：    
 * @version     
 *     
 */
public class LogMsg {
 public LogMsg()
 {
     buf=new StringBuffer();
 }
/**
 * 消息类型
 */
public  int level;

/*
 * 消息内容
 */
public String msg="";
public Object objMsg=null;
private StringBuffer buf=null;
public void addmsg(String msg)
{
    buf.append(msg);
    buf.append(",");
}
public String toString()
{
 return   buf.toString();
}
}
