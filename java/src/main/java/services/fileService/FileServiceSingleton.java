package services.fileService;

/**
 * A Singleton provider for FileService, using the enum pattern.
 */
public enum FileServiceSingleton {
	// The instance
	INSTANCE;
	
	// The FileService singleton.
	FileService fileService;
	
	/**
	 * Constructs the instance.
	 */
	FileServiceSingleton() {
		this.fileService = new FileService();
	}
	
	/**
	 * Gets the instance of the FileService.
	 * @return The FileService.
	 */
	public FileService get() {
		return fileService;
	}
}