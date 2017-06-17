/**    
 * �ļ�����MasterProcess.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��15��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package DataProcess;

import com.google.common.eventbus.AllowConcurrentEvents;

import Config.CenterConfig;
import Model.MasterModel;
import RecServer.CenterTimer;
import StromModel.ConfigModel;


/**    
 *     
 * ��Ŀ���ƣ�DataStrom    
 * �����ƣ�MasterProcess    
 * ��������   ������ע��������Ϣ 
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��15�� ����10:38:55    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��15�� ����10:38:55    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class MasterProcess {
    @AllowConcurrentEvents
    public void recMasterAsk(MasterModel req)
    {
        ConfigModel model=new ConfigModel();
        model.action=req.action;
        model.centerByte=req.centerByte;
        model.flage=req.flage;
        model.intflage=Integer.valueOf(req.flage);
        model.IP=req.IP;
        model.port=req.port;
        model.multIP=req.multIP;
        model.multPort=req.multPort;
        if(model.centerByte==4||model.centerByte==2)
        {
            //�����̽Ѱ������Ƚϲ鿴�Ƿ��Լ���master;
            //��ǰ������Լ���ѡ�ٻ���̽Ѱ�������Ѿ�ȡ��
           //�Լ���ѡ���򲻼���
            if(CenterConfig.localCenter.centerByte==1)
            {
                //����������������
                CenterTimer.addMaster(CenterConfig.localCenter);
            }
            else
            {
                if(CenterConfig.localCenter.centerByte==2&&model.centerByte==2)
                {
                    //ͬʱ����ѡ������
                    if(model.intflage>CenterConfig.localCenter.intflage)
                    {
                        //
                        CenterTimer.addMaster(model);
                    }
                }
            }
        }
        else
        {
            //���������������������
          //  if(model.equals(CenterConfig.localCenter))
                CenterTimer.addMaster(model);
        }
    }
}