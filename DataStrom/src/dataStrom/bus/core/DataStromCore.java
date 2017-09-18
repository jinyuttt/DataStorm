/**
 * 
 */
package dataStrom.bus.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import dataStrom.bus.config.TrackConfig;
import dataStrom.bus.mq.DateTime;
import dataStrom.bus.net.http.DataStromHttpServer;

/**
 * @author jinyu
 * 服务启动
 * http与MQ
 */
public class DataStromCore {
  static  TrackConfig config=new TrackConfig();
    private static final Logger log = Logger.getLogger(DataStromCore.class.getName());
    /**
     * @param args
     */
    public static void main(String[] args) {
        //
        try
        {
        FixOption start=new FixOption();
        start.optionKey="startTime";
        start.optionValue=new DateTime().getDate();
        ArrayList<FixOption> list=new ArrayList<FixOption>();
        list.add(start);
        JSonWrite.jsonRecord(list, "startTime.json");
        }
        catch(Exception ex)
        {
            log.warning(ex.getMessage());
        }
        //
     
        String xmlConfigFile=  option(args, "-conf", "conf/tracker.xml");
        try {
            loadFromXml(xmlConfigFile);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        
        DataStromHttpServer httpServer=new DataStromHttpServer(8080);
        try {
            httpServer.start();
            log.info("启动http服务");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        TrackServer trackserver=new TrackServer();
        trackserver.start(config);
  try {
    System.in.read();
} catch (IOException e) {
    e.printStackTrace();
}
    }
    public static String option(String[] args, String opt, String defaultValue){
        for(int i=0; i<args.length;i++){
            if(args[i].equals(opt)){
                if(i<args.length-1) return args[i+1];
                else return null;
            } 
        }
        return defaultValue;
    }
    public static void loadFromXml(InputSource source) throws Exception{
        XPath xpath = XPathFactory.newInstance().newXPath();    
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(source); 
        
        String prefix = "//server"; 
        config.addressList= valueOf(xeval(xpath, doc, prefix, "hostList"), "0.0.0.0:6666");  
        config.netType=valueOf(xeval(xpath, doc, prefix, "netType"), "udp"); 
        TrackConfig.netJar=valueOf(xeval(xpath, doc, prefix, "netJar"), "net/net.jar"); 
    }
    
    public static void loadFromXml(String xmlConfigSourceFile) throws Exception{ 
        InputSource source = new InputSource(xmlConfigSourceFile);  
        loadFromXml(source);
    } 
    public static String xeval(XPath xpath, Object item, String prefix, String key) throws XPathExpressionException{
        return xpath.evaluate(prefix + "/" + key,  item);
    }
    public static String valueOf(String value, String defaultValue){
        if(value == null) return defaultValue;
        return value;
    }
}
