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

       //������־��Ϣ
        frmui.logTXTShow(msg);
        if(msg.level!=0)
        {
            frmui.logtable(msg);
        }
           
   }
    @Subscribe
    @AllowConcurrentEvents
   public void logMsgShow(String msg)
   {
        //������Ϣ���ض�)
        if(msg!=null)
        {
            String[] all=msg.split("#");
            if(msg.startsWith("master"))
            {
               boolean isAction=false;
                if(all[3].equals("action"))
                {
                    isAction=true;
                }
              frmui.logCenter(all[1], all[2], all[0], isAction);
            }   
            else if(msg.startsWith("��ǰ�ڵ�"))
            {
             frmui.logCureentNode(all[1], all[2], all[0]);
            }
            else
            {
                frmui.logServer(all[0], all[1], all[2], all[4], Boolean.valueOf(all[3]), Boolean.valueOf(all[5]));
                
            }
        }
        //
        LogMsg info=new LogMsg();
        info.msg=msg;
        frmui.logtable(info);
   }
}
