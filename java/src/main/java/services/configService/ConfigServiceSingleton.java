package services.configService;

/**
 * A Singleton provider for ConfigService, using the enum pattern.
 */
public enum ConfigServiceSingleton {
	// The instance
	INSTANCE;
	
	// The FileService singleton.
	ConfigService configService;
	
	/**
	 * Constructs the instance.
	 */
	ConfigServiceSingleton() {
		this.configService = new ConfigService();
	}
	
	/**
	 * Gets the instance of the ConfigService.
	 * @return The ConfigService.
	 */
	public ConfigService get() {
		return configService;
	}
}