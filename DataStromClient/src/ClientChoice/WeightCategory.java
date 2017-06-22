/**    
 * �ļ�����WeightCategory.java    
 *    
 * �汾��Ϣ��    
 * ���ڣ�2017��6��23��    
 * Copyright ���� Corporation 2017     
 * ��Ȩ����    
 *    
 */
package ClientChoice;

/**    
 *     
 * ��Ŀ���ƣ�DataStromClient    
 * �����ƣ�WeightCategory    
 * ��������    
 * �����ˣ�jinyu    
 * ����ʱ�䣺2017��6��23�� ����12:12:43    
 * �޸��ˣ�jinyu    
 * �޸�ʱ�䣺2017��6��23�� ����12:12:43    
 * �޸ı�ע��    
 * @version     
 *     
 */
public class WeightCategory {
    private String category;    
    private Integer weight;    
        
    
    public WeightCategory() {    
        super();    
    }    
    
    public WeightCategory(String category, Integer weight) {    
        super();    
        this.setCategory(category);    
        this.setWeight(weight);    
    }    
    
    
    public Integer getWeight() {    
        return weight;    
    }    
    
    public void setWeight(Integer weight) {    
        this.weight = weight;    
    }    
    
    public String getCategory() {    
        return category;    
    }    
    
    public void setCategory(String category) {    
        this.category = category;    
    }    
}
