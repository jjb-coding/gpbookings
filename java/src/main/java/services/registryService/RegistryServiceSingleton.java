package services.registryService;

/**
 * A Singleton provider for RegistryService, using the enum pattern.
 */
public enum RegistryServiceSingleton {
	// The instance
	INSTANCE;
	
	// The FileService singleton.
	RegistryService registryService;
	
	/**
	 * Constructs the instance.
	 */
	RegistryServiceSingleton() {
		this.registryService = new RegistryService();
	}
	
	/**
	 * Gets the instance of the FileService.
	 * @return The FileService.
	 */
	public RegistryService get() {
		return registryService;
	}
}