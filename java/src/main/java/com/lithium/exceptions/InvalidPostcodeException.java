package com.lithium.exceptions;

/**
 * An exception to throw if a user-supplied
 * postcode is not valid.
 * 
 * @author Luke Stevens
 *
 */
public class InvalidPostcodeException extends LocatorException{

	private static final long serialVersionUID = 7053998846827445307L;
	
	public InvalidPostcodeException(){
		super("Error: Invalid postcode");
	}

}
