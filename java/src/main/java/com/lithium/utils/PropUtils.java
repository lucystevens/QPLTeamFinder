package com.lithium.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;

import com.lithium.exceptions.ExceptionHandler;

/**
 * Helper class for getting internal properties
 * 
 * @author Luke Stevens
 *
 */
public class PropUtils {
	
	private static Properties props;
	private static boolean isJar;
	
	public static String getProperty(String key) {
		if(props==null) loadInternal();
		return props.getProperty(key);
	}
	
	public static String getProperty(String key, String defaultValue){
		if(props==null) loadInternal();
		return props.getProperty(key, defaultValue);
	}
	
	public static Properties getAllProperties(){
		if(props==null) loadInternal();
		return props;
	}
	
	private static void loadInternal(){
		props = new Properties();
		ExceptionHandler.doOrDie(()->{
			isJar = FileUtils.isJar();
			Path root = getInternalRoot();
			listPropFilePaths(root, root.toString());
		});
	}
	
	private static Path getInternalRoot() throws URISyntaxException, IOException {
		Path path = null;
		String propertiesRoot = (isJar) ? "/resources/properties" : "/properties";
		URI propertiesUri = PropUtils.class.getResource(propertiesRoot).toURI();
		
		if (isJar) {
			FileSystem fileSystem = FileSystems.newFileSystem(propertiesUri, Collections.emptyMap());
			path = fileSystem.getPath(propertiesRoot);
		} 
		else {
			String propertiesPath = propertiesUri.getPath();
			if (propertiesPath.startsWith("/")) propertiesPath = propertiesPath.substring(1);
			path = Paths.get(propertiesPath);
		}
		return path;
	}
	
	private static void listPropFilePaths(Path propertiesPath, String beginPath) throws IOException {
		DirectoryStream<Path> stream = Files.newDirectoryStream(propertiesPath);
		for (Path entry : stream) {
			if (Files.isDirectory(entry)) listPropFilePaths(entry, beginPath);
			else if(isPropertiesFile(entry.toString())) loadPropertiesFile(entry.toString()); 
		}
	}
	
	public static void loadPropertiesFile(String path) throws IOException {
		try (InputStream in = isJar 
				? PropUtils.class.getResourceAsStream(path) 
				: new FileInputStream(new File(path))){
			props.load(in);
		}
	}
	
	private static boolean isPropertiesFile(String propFilePath){
		return propFilePath.endsWith(".properties");
	}

}
