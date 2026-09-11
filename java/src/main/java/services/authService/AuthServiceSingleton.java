package services.authService;

/**
 * A Singleton provider for AuthService, using the enum pattern.
 */
public enum AuthServiceSingleton {
	// The instance
	INSTANCE;
	
	// The AuthService singleton.
	AuthService authService;
	
	/**
	 * Constructs the instance.
	 */
	AuthServiceSingleton() {
		this.authService = new AuthService();
	}
	
	/**
	 * Gets the instance of the AuthService.
	 * @return The AuthService.
	 */
	public AuthService get() {
		return authService;
	}
}