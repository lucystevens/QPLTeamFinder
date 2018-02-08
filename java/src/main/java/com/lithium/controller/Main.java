package com.lithium.controller;

import com.lithium.services.LocatorService;
import com.lithium.services.PostcodeService;
import com.lithium.utils.ApplicationProperties;
import com.lithium.exceptions.LocatorException;
import com.lithium.rest.HTTP;
import com.lithium.rest.RestService;
import com.lithium.rest.SparkRestService;

import spark.Request;
import spark.Response;

public class Main {

	private static final String POSTCODE_AREA_FILE = "/files/NORMALISED_DATA.txt";
	private static final String WEBAPP_LOCATION = ApplicationProperties.getInstance().getProperty("webapp.location");
	
	private static LocatorService service;
	private static SparkServer server;
	
	/*
	 * Entry point for the application. Allows starting and stopping
	 * based on command arguments.
	 */
	public static void main (String[] args) throws Exception{
		if(args.length == 0) start();
		else if(args[0].equals("stop")) stop();
		else if(args[0].equals("start")) start();
	}
	
	/**
	 * Starts the application by initalising the server
	 */
	static void start(){
		PostcodeService postcode = new PostcodeService(POSTCODE_AREA_FILE);
		service = new LocatorService(postcode);
		
		server = new SparkServer(80, "/QPL");
		server.setStaticFilesLocation(WEBAPP_LOCATION);
		server.setCorsEnabled(true);
		server.addPageRedirection("index.html", "/");
		
		RestService pc = new SparkRestService(HTTP.GET, "/team/:postcode", Main::verifyPostcode);
		RestService latlng = new SparkRestService(HTTP.GET, "/team/:lat/:lng", Main::verifyLocation);
		
		server.addRestServices(pc, latlng);
		server.start();
	}
	
	/**
	 * Stops the application by stopping the server
	 */
	static void stop(){
		server.stop();
	}
	
	/*
	 * Convienience method for calling the verify postcode method
	 */
	static Object verifyPostcode(Request req, Response res){
		try {
			return service.verifyPostcode(req.params(":postcode"));
		} catch(LocatorException e){
			res.status(201);
			return e.getMessage();
		}
	}
	
	/*
	 * Convienience method for calling the verify location method
	 */
	static Object verifyLocation(Request req, Response res){
		try {
			return service.verifyLocation(req.params(":lat"),req.params(":lng"));
		} catch(LocatorException e){
			res.status(201);
			return e.getMessage();
		}
	}
}
