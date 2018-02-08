package com.lithium.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.lithium.exceptions.ExceptionHandler;

/**
 * Helper class for reading files
 * 
 * @author Luke Stevens
 *
 */
public class FileUtils {
	
	private static boolean isJar = false;

	static{
		ExceptionHandler.ignoreAll(()->{
			URI classpath = FileUtils.class.getResource("FileUtils.class").toURI();
			isJar = classpath.getScheme().equals("jar");
		});
	}
	
	public static boolean isJar(){
		return isJar;
	}
	
	public static List<String> readFileLines(InputStream in) throws IOException{
		List<String> result = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String input;
		while((input = reader.readLine()) != null){
			result.add(input);
		}
		return result;
	}
	
	public static List<String> readFileLines(String filepath, boolean isInternal) throws IOException{
		filepath = (isInternal && isJar) ? "/resources" + filepath : filepath;
		List<String> result = new ArrayList<>();
		try(InputStream in = FileUtils.class.getResourceAsStream(filepath)){
			result = readFileLines(in);
		} 
		return result;
	}
	
	public static List<String> readFileLines(String filepath) throws IOException{
		return readFileLines(filepath, filepath.startsWith("/"));
	}
	
}

