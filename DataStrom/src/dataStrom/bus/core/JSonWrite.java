/**
 * 
 */
package dataStrom.bus.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author jinyu
 *
 */
public class JSonWrite {
    private static final Logger log = Logger.getLogger(JSonWrite.class.getName());
    public static void  jsonRecord(Object obj,String filename)
    {
      String content=HTTPRequest.getData(obj);
      record(content,filename);
    }
    private static void record(String content,String filename)
    {
        if(content==null)
        {
            return;
        }
         String file="web/jsondata/"+filename;
         File f=new File(file);
        byte[] bt=content.getBytes(); 
          FileOutputStream in;
          try {
              in = new FileOutputStream(f);
         
              in.write(bt);
              in.close();
          } catch (IOException e) {
              log.warning(e.getMessage());
          
          }  
         
    }
}
