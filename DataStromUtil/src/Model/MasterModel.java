/**    
 * �ļ�����MasterModel.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��15��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package Model;

import Util.IDataPackaget;

/**    
 *     
 * ��Ŀ���ƣ�DataStromUtil    
 * �����ƣ�MasterModel    
 * ����������������ش���model
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��15�� ����10:09:45    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��15�� ����10:09:45    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class MasterModel extends IDataPackaget{
public MasterModel()
{
    this.packagetType=6;
}
/**
 * ע�����ĵ�ַ
 */
public String  IP="host";

/*
* ע�����Ķ˿�
*/
public int port=3333;

/*
* ע�����Ľ���״̬��IP
*/
public String multIP="224.0.1.21";

/*
* ע������״̬�˿�
*/
public int    multPort=4444;

/**
* ����״̬
* 1 ������
* 2 Ԥ�Ƴ�Ϊ������
* 3. һ��������
* 4  ̽Ѱ�����������
* 
*/
public byte centerByte=0;

/**
* ��ǰ����
*/
public boolean action=true;

/**
* ע�����ı�ʶ
*/
public String flage;
}
