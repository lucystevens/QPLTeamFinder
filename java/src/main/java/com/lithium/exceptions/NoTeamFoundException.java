package com.lithium.exceptions;

/**
 * Exception to be thrown if no team can be found
 * within the UK or if another country was supplied.
 * 
 * @author Luke Stevens
 *
 */
public class NoTeamFoundException extends LocatorException {

	private static final long serialVersionUID = 5986533257663540674L;
	private static final String MESSAGE = "Unfortunately there are currently no QPL teams operating in %s. If you believe you are receiving this message in error, try entering your postcode manually.";

	/**
	 * Constructs the exception, supplying the country
	 * the user is located in.
	 * @param country The country the user is located in (e.g. not UK)
	 */
	public NoTeamFoundException(String country) {
		super(String.format(MESSAGE, country));
	}
	
	/**
	 * Constructs the exception generically, when the team
	 * cannot be found.
	 */
	public NoTeamFoundException() {
		super("No team found for your area.");
	}

}
