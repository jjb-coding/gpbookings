package form.field;

import status.Status;
import ui.component.IReflectsStatus;

/**
 * A form field with some level of validation. 
 */
public abstract class ValidatedField<T, U> extends BaseField<U> {	
	// Store the error raised by the validation logic.
	protected Status error;
	
	// The partnered error reflection for local validation issues.
	protected IReflectsStatus errorReflection;
	
	/**
	 * Base constructor. Attaches the errorLabel partner.
	 * @param errorReflection	The error reflection the object is tied to.
	 */
	public ValidatedField(IReflectsStatus errorReflection) {
		this.errorReflection = errorReflection;
	}

	/**
	 * This method validates the object and provides an error message string.
	 * It is defined lower in the type hierarchy than when it is called, by ApplyLogic.
	 * @param 	in The string to validate.
	 * @return 	A human-readable error description, or null if valid.
	 */
	public abstract Status Validate(T in);
	
	/**
	 * Updates the error label.
	 */
	@Override
	public void Sync() {
		if (errorReflection != null)
			errorReflection.setStatus(error);
	}
	
	/**
	 * Exposes the error message, for Forms that do not display each error message
	 * attached to each field.
	 */
	public Status getError() {
		return error;
	}
}
