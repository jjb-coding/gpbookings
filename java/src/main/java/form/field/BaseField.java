package form.field;

/**
 * The base field.
 * @param <T> The type that the field yields.
 */
public abstract class BaseField<T> {
	// Stores the value, after any manipulation done to it.
	public T postValue;
	
	/**
	 * Applies logic to the field, depending on it's type. This method
	 * is responsible for filling the postValue.
	 * @return 	Whether the field is valid or not.
	 */
	public abstract boolean ApplyLogic();
	
	/**
	 * Synchronises the postValue (the value after smartening logic) into
	 * the partnered control. Does nothing by default, and does not have
	 * to have an override.
	 */
	protected void Sync() {}
}
