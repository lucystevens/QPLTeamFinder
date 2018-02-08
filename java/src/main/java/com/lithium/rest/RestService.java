package com.lithium.rest;

/**
 * An interface to be implemented by any
 * RestServices that should be registered 
 * to the Server on startup.
 * 
 * @author Luke Stevens
 */
public interface RestService {
	
	/**
	 * Register this RestService, allowing it to
	 * be called whilst the server is running.
	 */
	public void register();

}
