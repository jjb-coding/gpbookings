package services.configService;

import org.w3c.dom.*;

import java.util.HashMap;
import java.util.Map;

import services.fileService.*;
import util.XMLUtil;

public class ConfigService {
	// Static constants
	static final String XML_ENTRY = "config";
	static final String XML_LANGUAGE = "language";
	static final String XML_DATABASE_USERNAME = "database_username";
	static final String XML_DATABASE_PASSWORD = "database_password";
	static final String XML_DATABASE_LOCATION = "database_location";
	static final String XML_VERSION = "version";
	
	// Fields
	final String language;
	final String databaseUsername;
	final String databasePassword;
	final String databaseLocation;
	final String version;

	/**
	 * 
	 * over the document structure in valid XML.
	 */
	public ConfigService() {
		FileService fileService = FileServiceSingleton.INSTANCE.get();
		
		// Make kvp object
		Map<String,String> properties = new HashMap<>();
		properties.put(XML_LANGUAGE, null);
		properties.put(XML_DATABASE_USERNAME, null);
		properties.put(XML_DATABASE_PASSWORD, null);
		properties.put(XML_DATABASE_LOCATION, null);
		properties.put(XML_VERSION, null);
		
		// Get the document root
		Element root;
		try {
			root = fileService.getDocumentByRoot(Paths.CONFIG, null);
		}
		catch (Exception e) {
			throw new RuntimeException("CONFIG:init: Couldn't load config | " + e.toString());
		}
		
		// Validate
		XMLUtil.validateEntry(root, XML_ENTRY);
		
		// Get each property
		try {
			XMLUtil.getTagsMasked(root, properties);
		}
		catch (RuntimeException e) {
			throw new RuntimeException("CONFIG:init: Parsing failure | " + e.toString());
		}
		
		// Extract
		language = properties.get(XML_LANGUAGE);
		databaseUsername = properties.get(XML_DATABASE_USERNAME);
		databasePassword = properties.get(XML_DATABASE_PASSWORD);
		databaseLocation = properties.get(XML_DATABASE_LOCATION);
		version = properties.get(XML_VERSION);
	}
	
	/**
	 * Gets the language.
	 * @return The language.
	 */
	public String getLanguage() {
		return language;
	}
	
	/**
	 * Gets the database username.
	 * @return The database username.
	 */
	public String getDatabaseUsername() {
		return databaseUsername;
	}
	
	/**
	 * Gets the database password.
	 * @return The database password.
	 */
	public String getDatabasePassword() {
		return databasePassword;
	}
	
	/**
	 * Gets the database location.
	 * @return The database location.
	 */
	public String getDatabaseLocation() {
		return databaseLocation;
	}
	
}
