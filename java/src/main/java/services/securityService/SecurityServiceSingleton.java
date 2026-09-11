package services.securityService;

/**
 * A Singleton provider for SecurityService, using the enum pattern.
 */
public enum SecurityServiceSingleton {
	// The instance
	INSTANCE;
	
	// The UserService singleton.
	SecurityService securityService;
	
	/**
	 * Constructs the instance.
	 */
	SecurityServiceSingleton() {
		this.securityService = new SecurityService();
	}
	
	/**
	 * Gets the instance of the SecurityService.
	 * @return The SecurityService.
	 */
	public SecurityService get() {
		return securityService;
	}
}