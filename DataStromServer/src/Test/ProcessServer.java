/**    
 * �ļ�����ProcessServer.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��19��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package Test;

import IServer.IProcessServer;
import Util.IDataPackaget;

/**    
 *     
 * ��Ŀ���ƣ�DataStromServer    
 * �����ƣ�ProcessServer    
 * ��������    
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��19�� ����12:04:02    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��19�� ����12:04:02    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class ProcessServer implements IProcessServer {

    /* (non-Javadoc)    
     * @see IServer.IProcessServer#recRequest(Util.IDataPackaget)    
     */
    @Override
    public void recRequest(IDataPackaget packaget) {
       System.out.println(packaget.serverName+"�յ�����");

    }

    /* (non-Javadoc)    
     * @see IServer.IProcessServer#take()    
     */
    @Override
    public IDataPackaget take() {
        // TODO Auto-generated method stub
        return null;
    }

}