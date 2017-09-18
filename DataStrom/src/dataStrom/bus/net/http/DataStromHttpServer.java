/**
 * 
 */
package dataStrom.bus.net.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Random;
import java.util.logging.Logger;

import dataStrom.bus.config.TrackConfig;
import dataStrom.bus.mq.MQCmd;
import dataStrom.bus.mq.MQMessage;
import dataStrom.bus.mq.Message;
import dataStrom.bus.proxy.ProxyClient;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

/**
 * @author jinyu
 *
 */
public class DataStromHttpServer  extends NanoHTTPD {
    public DataStromHttpServer(int port) {
        super(port);
    
    }
    Status    HTTP_OK = Status.OK;
    public static final String
    MIME_PLAINTEXT = "text/plain",
    MIME_HTML = "text/html",
    MIME_JS = "application/javascript",
    MIME_CSS = "text/css",
    MIME_PNG = "image/png",
     MIME_JPG= "image/jpg",
    MIME_DEFAULT_BINARY = "application/octet-stream",
    MIME_XML = "text/xml";
    
    private static final Logger log = Logger.getLogger(DataStromHttpServer.class.getName());
    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri ="wweb"+ session.getUri();
        DataStromHttpServer.log.info(method + " '" + uri + "' ");
        FileInputStream fis = null;
        //
        try { 
            if(uri!=null){

               if(uri.contains(".js")){
                       fis = new FileInputStream(uri.substring(1));
                       return new NanoHTTPD.Response(HTTP_OK, MIME_JS, fis);
               }else if(uri.contains(".css")){
                       fis = new FileInputStream(uri.substring(1));
                       return new NanoHTTPD.Response(HTTP_OK, MIME_CSS, fis);

               }else if(uri.contains(".png")){
                       fis = new FileInputStream(uri.substring(1));      
                       // HTTP_OK = "200 OK" or HTTP_OK = Status.OK;(check comments)
                       return new NanoHTTPD.Response(HTTP_OK, MIME_PNG, fis);
               }else if(uri.contains(".jpg")){
                   fis = new FileInputStream(uri.substring(1));      
                   // HTTP_OK = "200 OK" or HTTP_OK = Status.OK;(check comments)
                   return new NanoHTTPD.Response(HTTP_OK, MIME_JPG, fis);
           }
               else if (uri.contains("/mnt/sdcard")){
                     //  Log.d(TAG,"request for media on sdCard "+uri);
                       File request = new File(uri);
                       fis = new FileInputStream(request);
                       FileNameMap fileNameMap = URLConnection.getFileNameMap();
                       String mimeType = fileNameMap.getContentTypeFor(uri);
                       Response streamResponse = new Response(HTTP_OK, mimeType, fis);
                       Random rnd = new Random();
        String etag = Integer.toHexString( rnd.nextInt() );
        streamResponse.addHeader( "ETag", etag);
                       streamResponse.addHeader( "Connection", "Keep-alive");

                       return streamResponse;
               }else{
                       fis = new FileInputStream("web/Monitor.html");
                       //·µ»ØÍøÒ³Ö®Ç°
                       request("MQ");
                       request("TOPIC");
                       request("RPC");
                       return new NanoHTTPD.Response(HTTP_OK, MIME_HTML, fis);
               }
            }

    } catch (IOException e) {
            e.printStackTrace();
    }

        return null;
    }
   private void request(String name)
   {
       ProxyClient<Message,Message> client=new ProxyClient<Message,Message>();
       String[]addrs=TrackConfig.node.localAddress.split(":");
       String host=addrs[0];
       int port=Integer.valueOf(addrs[1]);
       client.netType=TrackConfig.node.netType;
      client.setHost(host, port);
      Message req=new Message();
      req.cmd=MQCmd.Query;
      req.setMQ(name);
      req.setBody("MQ");
      
    try {
      Message msg=  client.invokeSync(req,5);
      if(msg!=null)
      {
      MQMessage qmsg=new MQMessage(msg);
      record(qmsg.getMsg(),name+".json");
      }
    
    } catch (IOException e) {

        e.printStackTrace();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
   }
  private void record(String content,String filename)
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
       
  }
}
