package services.mapperService;

/**
 * A Singleton provider for LoggerService, using the enum pattern.
 */
public enum MapperServiceSingleton {
	// The instance
	INSTANCE;
	
	// The FileService singleton.
	MapperService mapperService;
	
	/**
	 * Constructs the instance.
	 */
	MapperServiceSingleton() {
		this.mapperService = new MapperService();
	}
	
	/**
	 * Gets the instance of the ConfigService.
	 * @return The ConfigService.
	 */
	public MapperService get() {
		return mapperService;
	}
}