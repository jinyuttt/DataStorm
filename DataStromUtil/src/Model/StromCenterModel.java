/**    
 * �ļ�����StromCenterModel.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��15��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package Model;

import Util.IDataPackaget;
import Util.ServerInfo;

/**    
 *     
 * ��Ŀ���ƣ�DataStromUtil    
 * �����ƣ�StromCenterModel    
 * ��������    ע�����������ͬʱЯ��������Ϣ
 * ���ڸ�ע������ͬ������
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��15�� ����2:33:23    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��15�� ����2:33:23    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class StromCenterModel extends IDataPackaget{
    public StromCenterModel()
    {
        this.packagetType=7;
    }
    /**
     * ���ı�ʶ
     */
public String centerFlage;

/*
 * ������Ϣ
 */
public ServerInfo info;
}
