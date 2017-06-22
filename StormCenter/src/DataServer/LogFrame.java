/**    
 * �ļ�����LogFrame.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��21��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package DataServer;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

import StromModel.LogMsg;

/**    
 *     
 * ��Ŀ���ƣ�StormCenter    
 * �����ƣ�LogFrame    
 * ��������    
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��21�� ����10:11:59    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��21�� ����10:11:59    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class LogFrame {
    FrmStrom frmui=null;
    public  LogFrame(FrmStrom frm)
    {
        frmui=frm;
    }
    @Subscribe
    @AllowConcurrentEvents
   public void logShow(LogMsg msg)
   {
        frmui.logShow(msg);
           
  
   }
}