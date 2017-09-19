/**
 * 
 */
package dataStrom.bus.mq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import dataStrom.bus.config.TrackConfig;

/**
 * @author jinyu
 * ���ڵ�ʱ������������Ϣ
 */
public class SingleStrom {
    private String file="Consumer/Consumer.dat";
    private String dir="Consumer";
    protected String MQStrom="MQ";
    
    /**
     * ��ȡ����
     * @return
     */
public String[] read()
{
    File f=new File(file);
    ArrayList<String> list=new ArrayList<String>();
    if(!f.exists())
    {
       return null;
    }
    InputStreamReader isr = null;
    try {
        isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
    BufferedReader read = new BufferedReader(isr);
    String line="";
    while((line=read.readLine())!=null){
        if(line.startsWith(MQStrom))
        {
           list.add(line);
        }
    }
    read.close();
    }
    catch(Exception ex)
    {
        
    }
    String[]data=new String[list.size()];
    return list.toArray(data);
}

/**
 * д������
 * @param info
 */
public void write(String info) {
    if(TrackConfig.Istracker)
    {
        return;//�ǵ��ڵ㲻����
    }
    File fdir=new File(dir);
    if(!fdir.exists())
    {
        if(!fdir.mkdir())
        {
            return;
        }
    }
   
    File f = new File(file);  
    try
    {
        BufferedWriter writer = new BufferedWriter(  
            new OutputStreamWriter(  
                    new FileOutputStream(f), "UTF-8"));  
    writer.write(MQStrom+info);
    writer.newLine();
    writer.flush();  
    writer.close();  
    }
    catch(Exception ex)
    {
        
    }
}
}
