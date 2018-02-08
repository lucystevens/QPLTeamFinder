package com.lithium.services;

import java.io.IOException;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.lithium.exceptions.GoogleAPIException;
import com.lithium.exceptions.InvalidPostcodeException;
import com.lithium.exceptions.LatLongException;
import com.lithium.exceptions.NoTeamFoundException;
import com.lithium.utils.ApplicationProperties;
import com.lithium.utils.GeoLocation;
import com.lithium.utils.PropUtils;

/**
 * Service to handle the location verification
 * called via the REST services.<br>
 * This uses the UK NUTS codes to determine
 * which areas of the UK certain postcodes are in.
 * 
 * @author Luke Stevens
 */
public class LocatorService {
	
	private static final String GOOGLE_API_KEY = ApplicationProperties.getInstance().getProperty("google.api.key");

	private PostcodeService service;

	public LocatorService(PostcodeService service) {
		this.service = service;
	}
	
	/**
	 * Given a postcode, this determines the team whose catchment area
	 * the postcode is in.
	 * @param postcode The postcode to check for
	 * @return The team whose catchment area the postcode is in
	 * @throws InvalidPostcodeException If the postcode supplied is invalid
	 * @throws NoTeamFoundException If there is no team for the supplied postcode
	 */
	public String verifyPostcode(String postcode) throws InvalidPostcodeException, NoTeamFoundException {
		
		// Gets the NUTS area code mapped to the region
		String areaCode = service.getNUTSCode(postcode);
		if(areaCode == null) throw new NoTeamFoundException();
		
		// Gets the team mapped to the NUTS area code
		String team = PropUtils.getProperty(areaCode);
		if(team == null) throw new NoTeamFoundException();
		
		return team;
	}
	
	/**
	 * Given a latitude and longitude, this queries the google geocoding
	 * API to get the co-ordinate location. If the location is within the
	 * UK and has a valid postcode then this uses the <code>verfiyPostcode</code>
	 * method to retrieve the team.
	 * @param lat The location latitude
	 * @param lng The location longitude
	 * @return The team whose catchment area the location is in
	 * @throws NoTeamFoundException If the location could not be determined or is not within the UK
	 * @throws InvalidPostcodeException If the location is within the UK but has an invalid postcode
	 * @throws GoogleAPIException If there is an issue connecting to the Google geocoding API
	 * @throws LatLongException If the supplied latitude and longitude is not valid.
	 */
	public String verifyLocation(String lat, String lng) throws NoTeamFoundException, InvalidPostcodeException, GoogleAPIException, LatLongException {
		String area = null;
		try {
			// Uses the Google geocoding API to reverse geocode the latitude
			// and longitude. Creates a new GeoLocation object from the results.
			GeoApiContext context = new GeoApiContext.Builder().apiKey(GOOGLE_API_KEY).build();
			LatLng location = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
			GeocodingResult[] results = GeocodingApi.reverseGeocode(context, location).await();
			GeoLocation gl = new GeoLocation(results);
			
			// Retrieves the country and postcode from the Geolocation
			String country = gl.getCountry();
			String postcode = gl.getPostcode();
			
			// If country is not UK or postcode is null, throw exception
			if(country==null || postcode==null) throw new NoTeamFoundException();
			else if(country.equals("United Kingdom")) area = verifyPostcode(postcode);
			else throw new NoTeamFoundException(country);
			
		} catch (IOException | ApiException | InterruptedException e){
			throw new GoogleAPIException();
		} catch(NumberFormatException e){
			throw new LatLongException();
		}
		return area;
	}

}
