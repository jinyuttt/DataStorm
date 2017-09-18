package dataStrom.bus.rpc;


import dataStrom.bus.net.Sync.ResultCallback;

 
public interface Invoker<REQ, RES> { 


	RES invokeSync(REQ req, int timeout);
	
	
	RES invokeSync(REQ req) ;


	void invokeAsync(REQ req, ResultCallback<RES> callback) ;
}
