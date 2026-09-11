package services.apiService;

/**
 * A Singleton provider for APIService, using the enum pattern.
 */
public enum APIServiceSingleton {
	// The instance
	INSTANCE;
	
	// The APIService singleton.
	APIService apiService;
	
	/**
	 * Constructs the instance.
	 */
	APIServiceSingleton() {
		this.apiService = new APIService();
	}
	
	/**
	 * Gets the instance of the APIService.
	 * @return The APIService.
	 */
	public APIService get() {
		return apiService;
	}
}