package com.lithium.rest;

import spark.Route;
import spark.Spark;

/**
 * A Spark specific Rest Service implementation.
 * 
 * @author Luke Stevens
 */
public class SparkRestService implements RestService {
	
	public static String CONTEXT = "";
	
	private HTTP method;
	private String path;
	private Route route;
	
	/**
	 * Constructs a new SparkRestService given the HTTP method to use,
	 * the path and route.
	 * @param method The HTTP method (e.g. GET, POST)
	 * @param path The path to call this REST service from (e.g. /QPL/rest/teams)
	 * @param route The route to register with Spark.
	 */
	public SparkRestService(HTTP method, String path, Route route){
		this.method = method;
		this.path = CONTEXT + path;
		this.route = route;
	}
	
	@Override
	public void register(){
		if(method == HTTP.GET) Spark.get(path, route);
		else if(method == HTTP.POST) Spark.post(path, route);
		else if(method == HTTP.PUT) Spark.put(path, route);
		else if(method == HTTP.PATCH) Spark.patch(path, route);
		else if(method == HTTP.DELETE) Spark.delete(path, route);
	}

}
