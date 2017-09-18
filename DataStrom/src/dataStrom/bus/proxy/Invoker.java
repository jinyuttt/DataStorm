package dataStrom.bus.proxy;

import java.io.IOException;

import dataStrom.bus.net.Sync.ResultCallback;

 

/**
 * The abstraction of remote invocation:
 * 1) invoke synchronously
 * 2) invoke asynchronously with a callback
 * 
 * @author rushmore (洪磊�?)
 *
 * @param <REQ> request type
 * @param <RES> response type
 */
public interface Invoker<REQ, RES> { 
	/**
	 * invoke synchronously with a timeout specified
	 * 
	 * @param req request message/object
	 * @param timeout waiting timeout in milliseconds
	 * @return response message/object
	 * @throws IOException if network failure happens
	 * @throws InterruptedException if invocation is interrupted
	 */
	RES invokeSync(REQ req, int timeout) throws IOException, InterruptedException;
	
	/**
	 * invoke synchronously 
	 * 
	 * @param req request message/object 
	 * @return response message/object
	 * @throws IOException if network failure happens
	 * @throws InterruptedException if invocation is interrupted
	 */
	RES invokeSync(REQ req) throws IOException, InterruptedException;

	/**
	 * invoke asynchronously 
	 * @param req request message/object
	 * @param callback called when the response arrive
	 * @throws IOException if network failure happens
	 */
	void invokeAsync(REQ req, ResultCallback<RES> callback) throws IOException;
}
