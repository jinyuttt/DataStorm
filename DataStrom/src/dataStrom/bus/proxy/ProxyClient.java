/**
 * 
 */
package dataStrom.bus.proxy;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dataStrom.bus.net.Sync.ResultCallback;

/**
 * @author jinyu
 *
 */
public class ProxyClient<REQ, RES> implements Invoker<REQ, RES>{
   // private ConcurrentMap<String, Ticket<REQ, RES>> tickets = new ConcurrentHashMap<String, Ticket<REQ, RES>>();
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();  
    private String host;
    private int port;
    private Ticket<REQ, RES> lastTicket=null;
    public String netType="";
    public void setHost(String host,int port)
    {
        this.host=host;
        this.port=port;
      
    }
    @Override
    public RES invokeSync(REQ req, int timeout) throws IOException, InterruptedException {
        Ticket<REQ, RES> ticket=new Ticket<REQ, RES>(timeout);
        ticket.host=this.host;
        ticket.port=this.port;
        ticket.netType=netType;
      
        lastTicket=ticket;
        cachedThreadPool.execute(new Runnable() {
    
            @Override
            public void run() {
                ticket.add(req);
                ticket.read();//需要有返回值
               // tickets.put(ticket.getid(), ticket);
            }
            
        });
        return ticket.take();
    }

    @Override
    public RES invokeSync(REQ req) throws IOException, InterruptedException {
        Ticket<REQ, RES> ticket=new Ticket<REQ, RES>();
        ticket.host=this.host;
        ticket.port=this.port;
        ticket.netType=netType;
        lastTicket=ticket;
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
              
                ticket.add(req);
                ticket.read();//需要有返回值
              //  tickets.put(ticket.getid(), ticket);
            }
            
        });
        return ticket.take();
    }

    @Override
    public void invokeAsync(REQ req, ResultCallback<RES> callback) throws IOException {
        Ticket<REQ, RES> ticket=new Ticket<REQ, RES>();
        ticket.setCall(callback);
        ticket.host=this.host;
        ticket.port=this.port;
        ticket.netType=netType;
        lastTicket=ticket;
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ticket.add(req);
                ticket.read();//需要有返回值
               // tickets.put(ticket.getid(), ticket);
            }
            
        });
        
    }
 public RES invokeResult()
 {
     if(lastTicket!=null)
     {
         lastTicket.read();
       return  lastTicket.take();
     }
     return null;
 }
}
