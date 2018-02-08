package com.lithium.controller;

import static spark.Spark.staticFiles;

import java.util.ArrayList;
import java.util.List;

import com.lithium.rest.RestRedirect;
import com.lithium.rest.RestService;
import com.lithium.rest.SparkRestService;

import spark.Spark;

/**
 * A class to handle the initialisation of
 * the Spark server, aswell as the registering
 * of REST services.
 * 
 * @author Luke Stevens
 */
public class SparkServer {
	
	private int port = 80;
	private String staticFilesLocation;
	private boolean corsEnabled;
	private List<RestService> restServices = new ArrayList<>();
	
	/**
	 * Constructs a new SparkServer to run on a specific port.
	 * @param port The port for the SparkServer to listen on.
	 */
	public SparkServer(int port){
		this.port = port;
	}
	
	/**
	 * Constructs a SparkServer with a specific port and REST
	 * context.
	 * @param port The port for the SparkServer to listen on.
	 * @param context The path context for any rest services
	 * registered to this server.
	 */
	public SparkServer(int port, String context){
		this.port = port;
		SparkRestService.CONTEXT = context;
	}

	/**
	 * Sets the REST context for this server
	 * @param context The path context for any rest services
	 * registered to this server.
	 */
	public void setContext(String context) {
		SparkRestService.CONTEXT = context;
	}

	/**
	 * Sets the external folder location to load static files from.
	 * @param staticFilesLocation The external folder location to serve
	 * static content from.
	 */
	public void setStaticFilesLocation(String staticFilesLocation) {
		this.staticFilesLocation = staticFilesLocation;
	}

	/**
	 * Sets whether Cross-Origin Resource Sharing should be enabled
	 * for REST services on this server.
	 * @param corsEnabled Whether CORS should be enabled.
	 */
	public void setCorsEnabled(boolean corsEnabled) {
		this.corsEnabled = corsEnabled;
	}
	
	/**
	 * Adds RestServices to be registered when this server is started.
	 * @param services A variable array of RestServices.
	 */
	public void addRestServices(RestService...services){
		for(RestService service : services){
			restServices.add(service);
		}
	}
	
	/**
	 * Adds page redirection to be registered when this server is started.
	 * @param redirectPage The page to redirect to.
	 * @param path The path to redirect to the specified page.
	 */
	public void addPageRedirection(String redirectPage, String path){
		restServices.add(new RestRedirect(redirectPage, path));
	}
	
	/**
	 * Intitialises the underlying Spark framework and registers all
	 * the specified REST services, starting the server.
	 */
	public void start(){
		if(staticFilesLocation != null) staticFiles.externalLocation(staticFilesLocation);
		Spark.port(port);
		if(corsEnabled) enableCORS();
		restServices.forEach(RestService::register);
	}
	
	/**
	 * Stops the server and clears all existing REST services.
	 */
	public void stop(){
		Spark.stop();
	}
	
	// Enables CORS on requests. This method is an initialization method and should be called once.
	private void enableCORS() {

	    Spark.options("/*", (request, response) -> {

	        String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
	        if (accessControlRequestHeaders != null) {
	            response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
	        }

	        String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
	        if (accessControlRequestMethod != null) {
	            response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
	        }

	        return "OK";
	    });

	    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
	}

}
