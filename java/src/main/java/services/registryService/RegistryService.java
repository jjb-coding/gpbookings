package services.registryService;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import services.configService.ConfigService;
import services.configService.ConfigServiceSingleton;
import services.fileService.*;
import util.XMLUtil;

public class RegistryService {
	// Static constants
	static final String XML_LANGUAGE_ENTRY = "language";
	static final String XML_COLOURS_ENTRY = "colours";
	static final String XML_TEXT_STYLES_ENTRY = "styles";
	static final String XML_STATUS_ENTRY = "statuses";
	
	public static final String XML_COLOURS_R = "r";
	public static final String XML_COLOURS_G = "g";
	public static final String XML_COLOURS_B = "b";
	
	public static final String XML_TEXT_STYLES_SIZE = "size";
	public static final String XML_TEXT_STYLES_COLOUR = "colour";
	public static final String XML_TEXT_STYLES_FONT = "font";
	public static final String XML_TEXT_STYLES_IS_ITALIC = "isItalic";
	public static final String XML_TEXT_STYLES_IS_BOLD = "isBold";
	
	public static final String XML_STATUS_COLOUR = "colour";
	public static final String XML_STATUS_TEXT = "text";	
		
	// Fields
	Map<String,String> mapLanguage;
	Map<String,Map<String,String>> mapColours;
	Map<String,Map<String,String>> mapTextStyles;
	Map<String,Map<String,String>> mapStatus;
		
	/**
	 * Lazy getter for the language map.
	 * @return
	 */
	public Map<String, String> getMapLanguage() {
		if (mapLanguage == null)
			constructMapLanguage();
		return mapLanguage;
	}
	
	/**
	 * Constructs the language map.
	 */
	private void constructMapLanguage() {
		ConfigService _configService = ConfigServiceSingleton.INSTANCE.get();
		FileService _fileService = FileServiceSingleton.INSTANCE.get();
		
		// LANGUAGE
		VirtualisationPair<Element> pair;
		String language = _configService.getLanguage();
		mapLanguage = new HashMap<>();
		try {
			pair = _fileService.getVirtualDocumentBoth(Paths.LANGUAGE, language);
			constructLanguage(pair.a());
			constructLanguage(pair.b());
		}
		catch (Exception e) {
			throw new RuntimeException("REGISTRY:init: Failed to parse language | " + e.toString());
		}
	}
	
	/**
	 * Parses one file for the language map.
	 * @throws Exception
	 */
	private void constructLanguage(Element entry) throws Exception {
		// Validate: only 1 child node of name 'language'
		XMLUtil.validateEntry(entry, XML_LANGUAGE_ENTRY);
		
		// Get all
		XMLUtil.getAllTags(entry, mapLanguage);
	}
	
	/**
	 * Lazy getter for the colours map.
	 * @return
	 */
	public Map<String, Map<String, String>> getMapColours() {
		if (mapColours == null)
			constructMapColours();
		return mapColours;
	}
	
	/**
	 * Constructs the colours map.
	 */
	private void constructMapColours() {
		FileService _fileService = FileServiceSingleton.INSTANCE.get();
		// COLOURS
		VirtualisationPair<Element> pair;
		mapColours = new HashMap<>();
		try {
			pair = _fileService.getVirtualDocumentBoth(Paths.COLOURS, null);
			constructColours(pair.a());
			constructColours(pair.b());
		}
		catch (Exception e) {
			throw new RuntimeException("REGISTRY:init: Failed to parse colours | " + e.toString());
		}
	}
	

	/**
	 * Parses one file for the colours map.
	 * @throws Exception
	 */
	public void constructColours(Element entry) throws Exception {
		// Validate: only 1 child node of name 'colours'
		XMLUtil.validateEntry(entry, XML_COLOURS_ENTRY);
		
		// Get all
		Map<String,String> reference = new HashMap<>();
		reference.put(XML_COLOURS_R, null);
		reference.put(XML_COLOURS_G, null);
		reference.put(XML_COLOURS_B, null);
		XMLUtil.getNestedTags(entry, mapColours, reference);
	}

	/**
	 * Lazy getter for the colours map.
	 * @return
	 */
	public Map<String, Map<String, String>> getMapTextStyles() {
		if (mapTextStyles == null)
			constructMapTextStyles();
		return mapTextStyles;
	}
	
	
	/**
	 * Constructs the text styles map.
	 */
	private void constructMapTextStyles() {
		FileService _fileService = FileServiceSingleton.INSTANCE.get();
		// TEXT STYLES
		VirtualisationPair<Element> pair;
		mapTextStyles = new HashMap<>();
		try {
			pair = _fileService.getVirtualDocumentBoth(Paths.TEXT_STYLES, null);
			constructTextStyles(pair.a());
			constructTextStyles(pair.b());
		}
		catch (Exception e) {
			throw new RuntimeException("REGISTRY:init: Failed to parse text styles | " + e.toString());
		}
	}
	
	
	/**
	 * Parses one file for the text styles map.
	 * @throws Exception
	 */
	public void constructTextStyles(Element entry) throws Exception {
		// Validate: only 1 child node of name 'styles'
		XMLUtil.validateEntry(entry, XML_TEXT_STYLES_ENTRY);
		
		// Get all
		Map<String,String> reference = new HashMap<>();
		reference.put(XML_TEXT_STYLES_SIZE, null);
		reference.put(XML_TEXT_STYLES_COLOUR, null);
		reference.put(XML_TEXT_STYLES_FONT, null);
		reference.put(XML_TEXT_STYLES_IS_ITALIC, null);
		reference.put(XML_TEXT_STYLES_IS_BOLD, null);
		XMLUtil.getNestedTags(entry, mapTextStyles, reference);
	}
	
	/**
	 * Lazy getter for the colours map.
	 * @return
	 */
	public Map<String, Map<String, String>> getMapStatus() {
		if (mapStatus == null)
			constructMapStatus();
		return mapStatus;
	}
	
	/**
	 * Constructs the text styles map.
	 */
	private void constructMapStatus() {
		FileService _fileService = FileServiceSingleton.INSTANCE.get();
		// STATUS
		VirtualisationPair<Element> pair;
		mapStatus = new HashMap<>();
		try {
			pair = _fileService.getVirtualDocumentBoth(Paths.STATUSES, null);
			constructStatus(pair.a());
			constructStatus(pair.b());
		}
		catch (Exception e) {
			throw new RuntimeException("REGISTRY:init: Failed to parse statuses | " + e.toString());
		}
	}
	
	/**
	 * Parses one file for the status map.
	 * @throws Exception
	 */
	public void constructStatus(Element entry) throws Exception {
		// Validate: only 1 child node of name 'styles'
		XMLUtil.validateEntry(entry, XML_STATUS_ENTRY);
		
		// Get all
		Map<String,String> reference = new HashMap<>();
		reference.put(XML_STATUS_COLOUR, null);
		reference.put(XML_STATUS_TEXT, null);
		XMLUtil.getNestedTags(entry, mapStatus, reference);
	}
	
	/**
	 * Constructs a RegistryService instance.
	 */
	public RegistryService() {}
	
	/**
	 * 
	 */
	public void dispose() {
		mapLanguage = null;
		mapColours = null;
		mapTextStyles = null;
		mapStatus = null;
	}
}
