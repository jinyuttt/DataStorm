/**
 * 
 */
package dataStrom.bus.mq;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

/**
 * @author jinyu
 *
 */
public class Shared<T> {

    // ��ʵ�ڵ��Ӧ������ڵ�����
    private int length = 100;
    // ����ڵ���Ϣ
    private TreeMap<Long, T> virtualNodes;
    // ��ʵ�ڵ���Ϣ
    private List<T> realNodes;

    public Shared(List<T> realNodes) {
        this.realNodes = realNodes;
        init();
    }

    public List<T> getReal() {
        return realNodes;
    }

    /**
     * ��ʼ������ڵ�
     */
    private void init() {
        virtualNodes = new TreeMap<Long, T>();
        for (int i = 0; i < realNodes.size(); i++) {
            for (int j = 0; j < length; j++) {
                virtualNodes.put(hash("aa" + i + j), realNodes.get(i));
            }
        }
    }

    /**
     * ��ȡһ�����
     * 
     * @param key
     * @return
     */
   
    public T getNode(String key) {
        Long hashedKey = hash(key);
        // TODO judge null
        Entry<Long, T> en = virtualNodes.ceilingEntry(hashedKey);
        if (en == null) {
            return (T) virtualNodes.firstEntry().getValue();
        }
        return (T) en.getValue();
    }

    /**
     * MurMurHash�㷨���ǷǼ���HASH�㷨�����ܸܺߣ�
     * �ȴ�ͳ��CRC32,MD5��SHA-1���������㷨���Ǽ���HASH�㷨�����Ӷȱ���ͺܸߣ������������ϵ���Ҳ���ɱ��⣩
     * ��HASH�㷨Ҫ��ܶ࣬���Ҿ�˵����㷨����ײ�ʺܵ�. http://murmurhash.googlepages.com/
     */
    private Long hash(String key) {
        ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
        int seed = 0x1234ABCD;

        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            // for big-endian version, do this first:
            // finish.position(8-buf.remaining());
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return h;
    }
    public T getOne()
    {
        if(realNodes!=null&&realNodes.size()==1)
        {
            return realNodes.get(0);
        }
        else
        {
               Random rdm=new Random();
               int max=0;
               String all="0"+realNodes.size()+length;
              max=Integer.valueOf(all);
             String value="aa"+   rdm.nextInt(max);
            return  getNode(value);
        }
    }
}
