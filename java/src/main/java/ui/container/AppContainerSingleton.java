package ui.container;

/**
 * A Singleton provider for AppContainer, using the enum pattern.
 */
public enum AppContainerSingleton {
	// The instance
	INSTANCE;
	
	// The AppContainer singleton.
	AppContainer appContainer;
	
	/**
	 * Constructs the instance.
	 */
	AppContainerSingleton() {
		this.appContainer = new AppContainer();
	}
	
	/**
	 * Gets the instance of the AppContainer.
	 * @return The AppContainer.
	 */
	public AppContainer get() {
		return appContainer;
	}
}