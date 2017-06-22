/**    
 * 文件名：WeightCategory.java    
 *    
 * 版本信息：    
 * 日期：2017年6月23日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package ClientChoice;

/**    
 *     
 * 项目名称：DataStromClient    
 * 类名称：WeightCategory    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2017年6月23日 上午12:12:43    
 * 修改人：jinyu    
 * 修改时间：2017年6月23日 上午12:12:43    
 * 修改备注：    
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
