package com.lithium.utils;

import java.util.Arrays;

import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;

/**
 * A convenience wrapper for Google geocoding results,
 * containing method to easily get the country and postcode.
 * 
 * @author Luke Stevens
 *
 */
public class GeoLocation {
	
	private GeocodingResult[] results;
	private String country;
	private String postcode;

	/**
	 * Constructs a new GeoLocation using a set
	 * of Google GeocodingResults
	 * @param results An array of Geocoding results obtained
	 * from the Google Geocoding API
	 */
	public GeoLocation(GeocodingResult[] results) {
		this.results = results;
	}
	
	/**
	 * @return Gets the String representation of the country of this location
	 */
	public String getCountry(){
		if(country==null){
			country = getComponent(AddressComponentType.COUNTRY);
		}
		return country;
	}
	
	/**
	 * @return Gets the String representation of the postcode of this location
	 */
	public String getPostcode(){
		if(postcode==null){
			postcode = getComponent(AddressComponentType.POSTAL_CODE);
		}
		return postcode;
	}
	
	/**
	 * @return Gets a String representation of an AddressComponentType of this location
	 */
	public String getComponent(AddressComponentType type){
		String result = null;
		// Loops through all results until one with the required component is found
		for(int i = 0; i<results.length && result==null; i++){
			AddressComponent comp = getComponent(type, results[i].addressComponents);
			if(comp!=null) result = comp.longName;
		}
		return result;
	}
	
	AddressComponent getComponent(AddressComponentType type, AddressComponent[] components){
		// Loops thorugh all the address components
		for(AddressComponent address : components){
			
			// Return this component if the type is one of this components types
			if(Arrays.asList(address.types).contains(type)) return address;
		}
		return null;
	}

}
