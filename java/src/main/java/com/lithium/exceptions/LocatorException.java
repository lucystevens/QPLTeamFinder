package com.lithium.exceptions;

/**
 * Superclass for exceptions thrown by the 
 * LocatorService.
 * 
 * @author Luke Stevens
 *
 */
public class LocatorException extends Exception {

	private static final long serialVersionUID = 2800415799874499514L;
	
	public LocatorException(String msg){
		super(msg);
	}

}
