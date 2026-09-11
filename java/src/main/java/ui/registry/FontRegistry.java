package ui.registry;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import services.fileService.*;

public enum FontRegistry {
	// -----------------------------
	VERA,
	CALIBRI,
	UBUNTU;

	// -----------------------------
	// Fields
	Font font;
	
	/**
	 * Constructs a palette item.
	 */
	FontRegistry() {
		FileService fileService = FileServiceSingleton.INSTANCE.get();
		try {
			this.font = Font.createFont(
				Font.TRUETYPE_FONT,
				fileService.getVirtualFileOne(
					Paths.FONTS,
					this.name()
				)
			);
		}
		catch (Exception e) {
			throw new RuntimeException("FontRegistry: Couldn't find " + this.name());
		}
	}
	
	public Font getFont() {
		return font;
	}
	

	// Reverse
	static FontRegistry fromName(String name) {
		return map.get(name.toLowerCase());
	}
	static Map<String, FontRegistry> map = new HashMap<>();
	static {
		for (FontRegistry font : values())
			map.put(font.name().toLowerCase(), font);
	}
}
