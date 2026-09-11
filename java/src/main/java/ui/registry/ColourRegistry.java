package ui.registry;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import services.registryService.*;

public enum ColourRegistry {
	// -----------------------------
	IGNORED,
	
	LOCAL_ERROR,
	API_ERROR,
	NEUTRAL,
	SUCCESS,
	TABLE_SELECTED_BACKGROUND,
	TABLE_UNSELECTED_1_BACKGROUND,
	TABLE_UNSELECTED_2_BACKGROUND,
	TABLE_SELECTED_TEXT,
	TABLE_UNSELECTED_1_TEXT,
	TABLE_UNSELECTED_2_TEXT,
	ACCOUNT_BAR_BACKGROUND,
	HEADER_BACKGROUND,
	FORM_BACKGROUND,
	HEADER_TEXT,
	DASHBOARD_ICONS_TEXT,
	DASHBOARD_ICONS_BACKGROUND,
	APP_BACKGROUND,
	APP_BACKGROUND_PALE,
	SMALL_BUTTON_TEXT,
	PROGRESS_BAR,
	TEXT
	;
	
	// -----------------------------
	// The palette data
	Color color;
	
	/**
	 * Constructs a palette item.
	 */
	ColourRegistry() {
		// Get Registry service & Colour map entry
		RegistryService registryService = RegistryServiceSingleton.INSTANCE.get();
		Map<String,String> map = registryService.getMapColours().get(name());
		
		// Null
		if (map == null)
			throw new RuntimeException("ColourRegistry: Couldn't find " + this.name());
		
		// Decode
		int r = Integer.parseInt(map.get("r"));
		int g = Integer.parseInt(map.get("g"));
		int b = Integer.parseInt(map.get("b"));
		
		// Assign fields
		color = new Color(r, g, b);
	}
	
	/**
	 * Returns the colour associated with this item.
	 * @return The colour.
	 */
	public Color getColour() {
		return color;
	}
	
	// Maps each enum instance's name to the instance.
	static Map<String, ColourRegistry> map = new HashMap<>();
	
	/**
	 * 
	 * @param name	
	 * @return		
	 */
	public static ColourRegistry fromName(String name) {
		return map.get(name);
	}
	
	/**
	 * Constructs the map.
	 */
	static {
		for (ColourRegistry colour : values())
			map.put(colour.name(), colour);
	}
}