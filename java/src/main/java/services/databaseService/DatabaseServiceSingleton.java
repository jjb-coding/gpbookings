package services.databaseService;

/**
 * A Singleton provider for DatabaseService, using the enum pattern.
 */
public enum DatabaseServiceSingleton {
	// The instance
	INSTANCE;
	
	// The DatabaseService singleton.
	DatabaseService databaseService;
	
	/**
	 * Constructs the instance.
	 */
	DatabaseServiceSingleton() {
		this.databaseService = new DatabaseService();
	}
	
	/**
	 * Gets the instance of the DatabaseService.
	 * @return The DatabaseService.
	 */
	public DatabaseService get() {
		return databaseService;
	}
}