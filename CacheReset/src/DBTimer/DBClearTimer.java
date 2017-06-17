
/**    
 * �ļ�����DBClearTimer.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��14��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package DBTimer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import DBAcess.BerkeleyDB;
import DataJsonSerializer.JsonSerializer;

/**    
 *     
 * ��Ŀ���ƣ�CacheReset    
 * �����ƣ�DBClearTimer    
 * ��������    DB������ݣ���DB���津��
 * ֻ�������10����;��ʱ�������ݿ�����
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��14�� ����12:39:35    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��14�� ����12:39:35    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class DBClearTimer {
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();  
    private static LinkedBlockingDeque<String> queueStr=new LinkedBlockingDeque<String>();
    private static LinkedBlockingDeque<Long> queueLong=new LinkedBlockingDeque<Long>();
    BerkeleyDB dbStr=null;
    BerkeleyDB dbLong=null;
    BerkeleyDB db=null;
    private static int num=10000;
    private static volatile boolean isStart=false;
    private static volatile boolean isStrStart=false;
    private static volatile boolean isLongStart=false;
    private static long savetime=60*1*10;
    public DBClearTimer()
    {
        dbStr=new BerkeleyDB();
        dbStr.setDir("cacheDataKey");
        dbStr.open();
        dbStr.setConfig();
        //
        dbLong=new BerkeleyDB();
        dbLong.setDir("cacheDataKey");
        dbLong.open();
        dbLong.setConfig();
        //
        db=new BerkeleyDB();
        db.setDir("cacheData");
        db.open();
        db.setConfig();
    }
    public static void addString(String key)
    {
        queueStr.offer(key);
    }
    public static void addLong(long key)
    {
        queueLong.offer(key);
    }
    /*
     * �������
     */
    public synchronized void startThread()
    {
        if(isStart)
        {
            return;
        }
        isStart=true;
        this.startLongDBThread();
        this.startStringDBThread();
        Runnable runnable = new Runnable() {  
            public void run() {  
                byte[] bytes=new byte[8];
                ByteBuffer buf=ByteBuffer.wrap(bytes);
                while (true) {  
                    try {  
                        TimeUnit.MINUTES.sleep(5);//ÿ5��������
                        byte[] t=null;
                                try {
                                    t="ʱ��".getBytes("utf-8");
                                } catch (UnsupportedEncodingException e) {
                                    t="ʱ��".getBytes();
                                }
     ;
                       byte[] value=  dbStr.get(t);
                       ByteBuffer f=ByteBuffer.wrap(value);
                       long key = f.getLong();
                       long cur=TimerCount.CountTime();
                       while(key<cur-savetime)
                       {
                           buf.putLong(key);
                           byte[]curkey=buf.array();
                           byte[]  valuesStr= dbStr.get(curkey);
                           byte[]   valueLong=  dbLong.get(curkey);
                           buf.clear();
                           key++;
                           dbStr.delete(curkey);
                           dbLong.delete(curkey);
                           //
                           cachedThreadPool.execute(new Runnable(){

                            @Override
                            public void run() {
                             String longkey=new String(valueLong);
                             String strKey=new String(valuesStr);
                                //
                             String[] longar=longkey.split("#");
                             String[] strar=strKey.split("#");
                             if(longar!=null)
                             {
                             for(int i=0;i<longar.length;i++)
                             {
                                 try
                                 {
                                 long k=Long.valueOf(longar[i]);
                                 byte[] dkey =JsonSerializer.serializerObject(k);
                                 db.delete(dkey);
                                 }
                                 finally
                                 {
                                     
                                 }
                             }
                             }
                             if(strar!=null)
                             {
                                 
                             for(int i=0;i<strar.length;i++)
                             {
                                 try
                                 {
                                 byte[] dkey =JsonSerializer.serializerObject(strar[i]);
                                 db.delete(dkey);
                                 }
                                 finally
                                 {
                                     
                                 }
                             }
                            }
                            }
                           });
                       }
                    
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        };  
        Thread thread = new Thread(runnable);  
        thread.setDaemon(true);
        thread.setName("TimerCount");
        thread.start(); 
    }
    
    
    /*
     * �洢key
     */
    public synchronized void startLongDBThread()
    {
        if(isLongStart)
        {
            return;
        }
        isLongStart=true;
        final long timeInterval = 1000;  
        Runnable runnable = new Runnable() {  
            public void run() {  
                StringBuffer buf=new StringBuffer();
                byte[] bytes=new byte[8];
                ByteBuffer bf=ByteBuffer.wrap(bytes);
                while (true) {  
                    try {  
                     //�洢 
                      Thread.sleep(timeInterval); 
                       Long   key=TimerCount.CountTime();
                       int  curCount=0;
                      
                       while(!queueLong.isEmpty())
                       {
                           curCount++;
                           buf.append(queueLong.removeFirst());
                           buf.append("#");
                           if(curCount>num)
                           {
                              try {
                                curCount=0;
                                byte[] databytes=buf.toString().getBytes("utf-8");
                                bf.putLong(key);
                                dbLong.insert(bf.array(), databytes);
                            } catch (UnsupportedEncodingException e) {
                          
                                e.printStackTrace();
                            }
                              buf.setLength(0);
                              buf.trimToSize();
                               break;
                               
                           }
                       }
                      
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        };  
        Thread thread = new Thread(runnable);  
        thread.setDaemon(true);
        thread.setName("DBLong");
        thread.start(); 
    }

    
    /*
     * �洢key
     */
    public synchronized void startStringDBThread()
    {
        if(isStrStart)
        {
            return;
        }
        isStrStart=true;
        final long timeInterval = 1000;  
        Runnable runnable = new Runnable() {  
            public void run() {  
                StringBuffer buf=new StringBuffer();
                byte[] bytes=new byte[8];
                ByteBuffer bf=ByteBuffer.wrap(bytes);
                while (true) {  
                    try {  
                     //�洢 
                      Thread.sleep(timeInterval); 
                       Long   key=TimerCount.CountTime();
                       int  curCount=0;
                      
                       while(!queueStr.isEmpty())
                       {
                           curCount++;
                           buf.append(queueStr.removeFirst());
                           buf.append("#");
                           if(curCount>num)
                           {
                              try {
                                  curCount=0;
                                byte[] databytes=buf.toString().getBytes("utf-8");
                                bf.putLong(key);
                                dbStr.insert(bf.array(), databytes);
                            } catch (UnsupportedEncodingException e) {
                          
                                e.printStackTrace();
                            }
                              buf.setLength(0);
                              buf.trimToSize();
                              bf.clear();
                               break;
                               
                           }
                       }
                      
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        };  
        Thread thread = new Thread(runnable);  
        thread.setDaemon(true);
        thread.setName("DBString");
        thread.start(); 
    }


}
