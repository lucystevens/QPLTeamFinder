package com.lithium.services;

import java.util.HashMap;
import java.util.Map;

import com.lithium.exceptions.ExceptionHandler;
import com.lithium.exceptions.InvalidPostcodeException;
import com.lithium.utils.FileUtils;

/**
 * A service to perform validation and manipulation
 * of postcodes.
 * 
 * @author Luke Stevens
 *
 */
public class PostcodeService {
	
	private String postcodeAreasFile;
	private Map<String, String> areaMap;
	
	/**
	 * Constructs a new PostcodeService
	 * @param postcodeAreasFile The filepath to the file that maps
	 * outward codes to NUTS codes.
	 */
	public PostcodeService(String postcodeAreasFile){
		this.postcodeAreasFile = postcodeAreasFile;
	}
	
	/**
	 * Normalises a String representing a Postcode to only 
	 * the outward code (e.g. BS1).
	 * @param postcode A user-supplied postcode String
	 * @return A normalised outward postcode
	 * @throws InvalidPostcodeException If the postcode supplied is not valid
	 */
	String normalise(String postcode) throws InvalidPostcodeException{
		// Remove all whitespace 
		postcode = postcode.toUpperCase().replaceAll("\\s", "");
		int length = postcode.length();
		
		// Check if it starts with some letters, then numbers
		if(!postcode.matches("[A-Z]+[0-9]+.*")) throw new InvalidPostcodeException();
		
		// If between 5 and 7 chars long, it is likely a full postcode,
		// so remove the last three characters (the inward code)
		else if (length <= 7 && length >= 5) return postcode.substring(0, length - 3);
		
		else if (length <= 4 && length >= 2) return postcode;
		else throw new InvalidPostcodeException();
	}
	
	/**
	 * Converts a postcode to a UK NUTS code
	 * @param postcode A postcode
	 * @return A UK NUTS code (e.g UKG)
	 * @throws InvalidPostcodeException If the postcode supplied is not valid
	 */
	public String getNUTSCode(String postcode) throws InvalidPostcodeException{
		String region = normalise(postcode);
		return getAreaMap().get(region);
	}
	
	/**
	 * Gets a map of normalised outward postcodes
	 * to UK NUTS area codes.
	 * @return A map of outward codes to NUTS codes.
	 */
	Map<String, String> getAreaMap(){
		if(areaMap == null){
			areaMap = readFile();
		}
		return areaMap;
	}
	
	// TODO this would work way better as a properties file
	/**
	 * Reads the file this service was initialised with
	 * splitting each line on a ';' character and then
	 * storing the data in a Map of String to String.
	 */
	Map<String, String> readFile(){
		Map<String, String> map = new HashMap<>();
		ExceptionHandler.print(()->{
			FileUtils.readFileLines(postcodeAreasFile).forEach(s->{
				String[] data = s.split(";");
				map.put(data[0], data[1]);
			});
		});
		return map;
	}

}
