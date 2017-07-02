/**    
 * �ļ�����RecviceBuffer.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��10��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package RecviceData;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**    
 *     
 * ��Ŀ���ƣ�DataStromUtil    
 * �����ƣ�RecviceBuffer    
 * ��������   ���ݴ洢 
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��10�� ����11:10:34    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��10�� ����11:10:34    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class ReceiveBuffer {
    private final AppData[]buffer;

   //��ȡλ��
    private volatile int readPosition=0;
    private long highestReadSequenceNumber=-1;
    //the lowest sequence number stored in this buffer
   // private final long initialSequenceNumber;

    //number of chunks
    private final AtomicInteger numValidChunks=new AtomicInteger(-1);

    //lock and condition for poll() with timeout
    private final Condition notEmpty;
    private final ReentrantLock lock;

    //the size of the buffer
    private final int size;
    public ReceiveBuffer(int size, long initialSequenceNumber){
        this.size=size;
        this.buffer=new AppData[size];
      //  this.initialSequenceNumber=initialSequenceNumber;
        lock=new ReentrantLock(false);
        notEmpty=lock.newCondition();
        highestReadSequenceNumber=-1;
    }
   /**
    * �����ݿ�
    */
    public boolean offer(AppData data){
        if(numValidChunks.get()==size) {
            return false;//�����ȡ�Ĵ�С���ڵ�ǰ��С�����Ѿ����ˣ����ܴ洢
        }
        lock.lock();
        try{
            long seq=data.getSequenceNumber();//��ȡ����seq
            if(highestReadSequenceNumber<seq)
            {
                //���浱ǰ�������seq
                highestReadSequenceNumber=seq;
            }
            int insert=(int) (seq% size);//����洢˳��λ��
            buffer[insert]=data;//��������
            if(numValidChunks.get()<0)
            {
                numValidChunks.set(0);
            }
            numValidChunks.incrementAndGet();//�洢����
            notEmpty.signal();//���Ѷ�ȡ
            return true;
        }finally{
            lock.unlock();
        }
    }
    /**
     * 
       
     * poll(��ʱ��ȡ���ݿ�)      
       
     * @param   name    
       
     * @param  @return    �趨�ļ�    
     * @Exception �쳣����    
     */
    public AppData poll(int timeout, TimeUnit units)throws InterruptedException{
        lock.lockInterruptibly();//��ȡ��
        long nanos = units.toNanos(timeout);//ת������

        try {
            for (;;) {
                //ѭ����ȡ��ֱ��û������
                if (numValidChunks.get() != 0) {
                    return poll();
                }
                if (nanos <= 0)
                    return null;//�ȴ���ʱ�仹��û������
                try {
                    //û������ʱ�ȴ�
                    nanos = notEmpty.awaitNanos(nanos);
                } catch (InterruptedException ie) {
                    notEmpty.signal(); // 
                    throw ie;
                }

            }
        } finally {
            lock.unlock();
        }
    }
    /**
     * �������ݿ�
     */
    public AppData poll(){
        if(numValidChunks.get()==0){
            return null;//û������
        }
        AppData r=buffer[readPosition];
        if(r!=null){
            increment();
           // long thisSeq=r.getSequenceNumber();
//            if(thisSeq<=highestReadSequenceNumber){
//                //������1��˵�����Զ�ȡ��һ��
//                increment();
//                highestReadSequenceNumber=thisSeq;
//            }
//            else return null;
        }
        return r;
    }

    /**
     * ��ǰ�ݻ�
     */
    public int getSize(){
        return size;
    }

    /*
     * ˳���ȡ
     */
    void increment(){
        buffer[readPosition]=null;
        readPosition++;
        if(readPosition==size)readPosition=0;
        numValidChunks.decrementAndGet();
    }

    /**
     * �����Ƿ��Ѿ���ȡ���
     */
    public boolean isEmpty(){
        if(numValidChunks.get()==0&&highestReadSequenceNumber!=-1)
        {
            return true;
        }
        else
        {
            return false;
        }
        //return numValidChunks.get()==0;
    }
    
    /**
     * ��ȡ��һ����ȡseq
     */
   public long waitSequenceNumber()
   {
       //��ȡ��һ��seq,���ܶ���
       return highestReadSequenceNumber+1;
   }
}
