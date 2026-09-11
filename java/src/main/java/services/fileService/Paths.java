package services.fileService;

import java.nio.file.Path;

public enum Paths {
	// -----------------------
	LOGS			(Directories.USER, "logs/", ".xml"),
	// SAVED_ACCOUNTS	(Directories.USER, null, "accounts.xml"),
	CONFIG			(Directories.INSTALLATION, null, "config.xml"),
	IMAGES			(Directories.INTERFACE, "images/", ".png"),
	FONTS			(Directories.INTERFACE, "fonts/", ".ttf"),
	COLOURS			(Directories.INTERFACE, "format/", "colours.xml"),
	TEXT_STYLES		(Directories.INTERFACE, "format/", "text_styles.xml"),
	STATUSES		(Directories.INTERFACE, "format/", "statuses.xml"),
	LANGUAGE		(Directories.INTERFACE, "lang/", ".xml"),
	;
	
	// -----------------------
	// Fields
	Directories directory;
	Path relativePath;
	String suffix;
	
	/**
	 * 
	 * @param directory
	 * @param relativePath
	 * @param suffix
	 */
	Paths(Directories directory, String relativePath, String suffix) {
		this.directory = directory;
		if (relativePath != null)
			this.relativePath = Path.of(relativePath);
		else
			this.relativePath = null;
		this.suffix = suffix;
	}
	
	/**
	 * 
	 * @return
	 */
	public Path getRelativePath() {
		return relativePath;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSuffix() {
		return suffix;
	}
	
	/**
	 * 
	 * @return
	 */
	public Directories getDirectory() {
		return directory;
	}
}