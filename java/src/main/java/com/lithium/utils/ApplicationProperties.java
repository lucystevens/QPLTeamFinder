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
public class ApplicationProperties extends Properties{
	
	private static final long serialVersionUID = -5568816764023429024L;
	private static final ApplicationProperties INSTANCE = new ApplicationProperties();
	private static boolean isJar;
	
	/**
	 * Gets the single instance of ApplicationProperties
	 * @return Application properties instance
	 */
	public static ApplicationProperties getInstance(){
		return INSTANCE;
	}
	
	private ApplicationProperties(){
		ExceptionHandler.ignoreAll(()->{
			URI classpath = ApplicationProperties.class.getResource("ApplicationProperties.class").toURI();
			isJar = classpath.getScheme().equals("jar");
			
			Path root = getInternalRoot();
			listPropFilePaths(root);
		});
	}
	
	private Path getInternalRoot() throws URISyntaxException, IOException {
		Path path = null;
		String propertiesRoot = (isJar) ? "/resources/properties" : "/properties";
		URI propertiesUri = ApplicationProperties.class.getResource(propertiesRoot).toURI();
		
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
	
	private void listPropFilePaths(Path propertiesRoot) throws IOException {
		DirectoryStream<Path> stream = Files.newDirectoryStream(propertiesRoot);
		for (Path entry : stream) {
			if (Files.isDirectory(entry)) listPropFilePaths(entry);
			else if(entry.toString().endsWith(".properties")) loadPropertiesFile(entry.toString()); 
		}
	}
	
	private void loadPropertiesFile(String path) throws IOException {
		try (InputStream in = isJar 
				? ApplicationProperties.class.getResourceAsStream(path) 
				: new FileInputStream(new File(path))){
			this.load(in);
		}
	}

}
