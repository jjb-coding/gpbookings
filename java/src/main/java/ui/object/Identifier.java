package ui.object;

public abstract class Identifier<T> {
	/**
	 * Identifies itself in a manner that makes sense to the database.
	 */
	public abstract T id();
	
	/**
	 * Display characteristic, particularly for combo boxes.
	 */
	public abstract String toString();
}
