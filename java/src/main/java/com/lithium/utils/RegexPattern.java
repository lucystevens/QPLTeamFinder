package com.lithium.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexPattern {

	private Pattern regex;
	
	public RegexPattern(String regex){
		this.regex = Pattern.compile(regex);
	}
	
	/**
	 * Returns all substrings within String that match
	 * this RegexPattern.
	 * @param string The String to match within.
	 * @return List of substrings that match this pattern
	 */
	public List<String> getMatches(String string){
		Matcher m = regex.matcher(string);
		List<String> matches = new ArrayList<>();
		while(m.find()){
			matches.add(m.group());
		}
		return matches;
	}
	
	/**
	 * Returns the first substring within String that
	 * matches this pattern
	 * @param string The String to match within
	 * @return Single matches substring.
	 */
	public String getFirstMatch(String string){
		List<String> matches = getMatches(string);
		return (matches.isEmpty()) ? null : matches.get(0);
	}
	
	/**
	 * Replaces all substrings within a String that
	 * match this regex pattern.
	 * @param string The String to match within.
	 * @param replacement The String to replace matches with.
	 * @return The modified String
	 */
	public String replaceAll(String string, String replacement){
		Matcher m = regex.matcher(string);
		return m.replaceAll(replacement);
	}
	
	/**
	 * Removes all substrings within a String that 
	 * match this regex pattern.
	 * @param string The String to match within.
	 * @return The modified String.
	 */
	public String removeAll(String string){
		return replaceAll(string, "");
	}
}
