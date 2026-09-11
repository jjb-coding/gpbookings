package services.practiceProvider;

/**
 * A Singleton provider for DataProvider, using the enum pattern.
 */
public enum PracticeProviderSingleton {
	// The instance
	INSTANCE;
	
	// The DataProvider singleton.
	PracticeProvider dataProvider;
	
	/**
	 * Constructs the instance.
	 */
	PracticeProviderSingleton() {
		this.dataProvider = new PracticeProvider();
	}
	
	/**
	 * Gets the instance of the DataProvider.
	 * @return The DataProvider.
	 */
	public PracticeProvider get() {
		return dataProvider;
	}
}