/**    
 * �ļ�����ConfigModel.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��14��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package StromModel;



/**    
 *     
 * ��Ŀ���ƣ�DataStromServer    
 * �����ƣ�ConfigModel    
 * ��������    ����ע���ַ
 * ���Ľ��յ�IP,�˿�
 * ����ͳһʹ�õ��鲥��ַ
 * �鲥�Լ�����״̬�����䵱ע�ᣩ
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��14�� ����10:46:45    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��14�� ����10:46:45    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class ConfigModel {
  
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
 * 4. ̽Ѱ��
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

/**
 * ���ֱ�ʶ��ͬfalge)
 */
public int intflage=0;
public boolean equals(Object obj)
{
    if (this == obj)      //����Ķ���������Լ�����s.equals(s)���϶�����ȵģ�  
        return true;   
    if (obj == null)     //�������Ķ����ǿգ��϶������  
        return false;  
    if (getClass() != obj.getClass())  //�������ͬһ�����͵ģ���Studnet���Animal�࣬  
                                       //Ҳ���ñȽ��ˣ��϶��ǲ���ȵ�  
        return false;  
    ConfigModel other = (ConfigModel) obj;       
    if (flage == null) {  
        if (other.flage != null)  
            return false;  
    } else if (!flage.equals(other.flage))   //���name������ȣ������  
        return false;  
    if(other.IP.equals(IP)&&other.port==port)
    {
        return true;
    }
    else
    {
        return false;  
    }
}
}