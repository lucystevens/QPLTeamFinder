package com.lithium.rest;

import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * A specific RestService implementation to redirect
 * from a given path to a given page.
 * 
 * @author Luke Stevens
 */
public class RestRedirect implements RestService {
	
	private String redirectPage;
	private String path;

	/**
	 * Constructs the RestRedirect with the page to redirect to.<br>
	 * This will redirect the root (e.g. localhost) to the page.
	 * @param redirectPage The page to redirect to.
	 */
	public RestRedirect(String redirectPage) {
		this(redirectPage, "/");
	}
	
	/**
	 * Constructs the RestRedirect with a given page and path<br>
	 * This will redirect to the page given, when the path specified is visited.
	 * @param redirectPage The page to redirect to.
	 * @param path The path to redirect from.
	 */
	public RestRedirect(String redirectPage, String path) {
		this.redirectPage = redirectPage;
		this.path = path;
	}
	
	@Override
	public void register(){
		Spark.get(path, this::route);
	}
	
	/*
	 * Convenience method for redirecting a request
	 */
	Object route(Request req, Response res){
		res.redirect(redirectPage);
		return null;
	}

}
