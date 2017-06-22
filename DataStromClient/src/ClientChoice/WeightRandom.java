/**    
 * �ļ�����WeightRandom.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��23��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package ClientChoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**    
 *     
 * ��Ŀ���ƣ�DataStromClient    
 * �����ƣ�WeightRandom    
 * ��������    
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��23�� ����12:13:51    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��23�� ����12:13:51    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class WeightRandom {
    public WeightRandom(int num,String name)
    {
        if(name==null)
        {
            name="wc";
        }
        for(int i=0;i<num;i++)
        {
           WeightCategory wc= new WeightCategory("wc"+(i+1),1);  
           categorys.add(wc);
        }
        weightSum=num;
    }
    Integer weightSum = 0;    
     List<WeightCategory>  categorys = new ArrayList<WeightCategory>();    
    private static Random random = new Random();    
        
//    public static void initData() {    
//        WeightCategory wc1 = new WeightCategory("A",60);    
//        WeightCategory wc2 = new WeightCategory("B",20);    
//        WeightCategory wc3 = new WeightCategory("C",20);    
//        categorys.add(wc1);    
//        categorys.add(wc2);    
//        categorys.add(wc3);    
//    }    
    public String  getWeightCategory()
    {
        Integer n = random.nextInt(weightSum); // n in [0, weightSum)    
        Integer m = 0;    
        for (WeightCategory wc : categorys) {    
             if (m <= n && n < m + wc.getWeight()) {    
               return      wc.getCategory();
              
             }    
             m += wc.getWeight();    
        }
        return null;    
    }
}