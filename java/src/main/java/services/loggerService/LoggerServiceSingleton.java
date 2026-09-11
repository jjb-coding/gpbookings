package services.loggerService;

/**
 * A Singleton provider for LoggerService, using the enum pattern.
 */
public enum LoggerServiceSingleton {
	// The instance
	INSTANCE;
	
	// The FileService singleton.
	LoggerService loggerService;
	
	/**
	 * Constructs the instance.
	 */
	LoggerServiceSingleton() {
		this.loggerService = new LoggerService();
	}
	
	/**
	 * Gets the instance of the ConfigService.
	 * @return The ConfigService.
	 */
	public LoggerService get() {
		return loggerService;
	}
}