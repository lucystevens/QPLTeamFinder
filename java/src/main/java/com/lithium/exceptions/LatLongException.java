package com.lithium.exceptions;

/**
 * An exception to throw if a user-supplied
 * Latitude and Longitude is invalid.
 * 
 * @author Luke Stevens
 *
 */
public class LatLongException extends LocatorException {

	private static final long serialVersionUID = -2483902373409292799L;

	public LatLongException() {
		super("Invalid latitude and longitude provided.");
	}

}
