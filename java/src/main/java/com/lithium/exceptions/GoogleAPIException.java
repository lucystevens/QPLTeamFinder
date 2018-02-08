package com.lithium.exceptions;

/**
 * An exception to represent a failure
 * to contact the Google geocoding API.
 * 
 * @author Luke Stevens
 *
 */
public class GoogleAPIException extends LocatorException {

	private static final long serialVersionUID = 5382477817713661115L;

	public GoogleAPIException() {
		super("Error contacting the Google Maps server. Please enter postcode manually or try again later.");
	}

}
