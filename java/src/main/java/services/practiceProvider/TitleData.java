package services.practiceProvider;

import ui.object.Identifier;

/**
 * Immutable data object. Hosts a Title instance.
 */
public class TitleData extends Identifier<String> {	
	// Details
	String title;
	
	/**
	 * Constructs a new data object instance of Title type.
	 * @param title		The name & identifier.
	 */
	public TitleData(String title) {
		this.title = title;
	}

	public String getDescription() {
		return title;
	}
	
	/**
	 * Returns the UUID.
	 */
	@Override
	public String id() {
		return title;
	}
	/**
	 * Returns the formatted name.
	 */
	@Override
	public String toString() {
		return title;
	}
}