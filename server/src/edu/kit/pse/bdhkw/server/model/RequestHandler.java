package edu.kit.pse.bdhkw.server.model;

import edu.kit.pse.bdhkw.common.communication.Request;
import edu.kit.pse.bdhkw.common.communication.Response;

/**
 * Handles Request objects generated by the servlet.
 * To do so, it calls the execute() method on the request
 * and hands over the ResourceManager.
 * @author Tarek Wilkening
 *
 */
public class RequestHandler {
	
	public RequestHandler() {
	}
	/**
	 * Calls the request's execute method, which will make
	 * the request execute itself.
	 * @param request object to be handled.
	 * @return response to the input request.
	 */
	public Response handleRequest(Request request) {
		return request.execute();
	}
}