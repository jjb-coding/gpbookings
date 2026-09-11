
package ui.registry;

import java.awt.Color;
import java.awt.Font;
import java.util.Map;

import services.registryService.RegistryService;
import services.registryService.RegistryServiceSingleton;

public enum TextStyleRegistry {
	HEADER,
	DASHBOARD_ICONS,
	SMALL_BUTTON,
	INSTRUCTION,
	FIELD_LABEL,
	STATUS_LABEL_DEFAULT,
	EMAIL_ADDRESS
	;
	
	// ----------------------
	// Fields
	Font font;
	Color colour;
	
	/**
	 * 
	 */
	TextStyleRegistry() {
		// Get Registry service & Text Style map entry
		RegistryService registryService = RegistryServiceSingleton.INSTANCE.get();
		Map<String,String> map = registryService.getMapTextStyles().get(name());

		// Null
		if (map == null)
			throw new RuntimeException("TextStyleRegistry: Couldn't find " + this.name());
		
		// Decode
		ColourRegistry colour = ColourRegistry.fromName(map.get("colour"));
		FontRegistry font = FontRegistry.fromName(map.get("font")); 
		float size = Float.parseFloat(map.get("size"));
		boolean isItalic = map.get("isItalic") == "true" ? true : false;
		boolean isBold = map.get("isBold") == "true" ? true : false;
		
		// Assign fields
		int style = (isItalic ? 2 : 0) + (isBold ? 1 : 0);
		this.font = font.getFont().deriveFont(style, size);
		this.colour = colour.getColour(); 
	}
	
	/**
	 * 
	 * @return
	 */
	public Font getFont() {
		return font;
	}
	
	/**
	 * 
	 * @return
	 */
	public Color getColour() {
		return colour;
	}
}
