package hashingAlgorithm;

import java.util.LinkedList;

public class Node {  
    private String name;  
    private int start;  
    private int end;  
    private String flage;
    private LinkedList<String> datas;  
    private Object objData;

    public Node(String name) {  
        this.name = name;  
        datas = new LinkedList<String>();  
    }  

    public String getName() {  
        return name;  
    }  

    public void setName(String name) {  
        this.name = name;  
    }  

    public int getStart() {  
        return start;  
    }  

    public void setStart(int start) {  
        this.start = start;  
    }  

    public int getEnd() {  
        return end;  
    }  

    public void setEnd(int end) {  
        this.end = end;  
    }  

    public LinkedList<String> getDatas() {  
        return datas;  
    }  

    public void setDatas(LinkedList<String> datas) {  
        this.datas = datas;  
    }  
    public String getFlage() {  
        return flage;  
    }  

    public void setFlage(String flage) {  
        this.flage = flage;  
    } 
    public Object getObjData() {  
        return objData;  
    }  

    public void setObjData(Object obj) {  
        this.objData = obj;  
    } 
}  
