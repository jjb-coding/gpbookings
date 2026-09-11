package form.multifield;

import java.util.List;

import status.Status;
import ui.component.IReflectsStatus;

/**
 * Binds multiple fields together under some kind of validation logic.
 */
public abstract class ValidatedMultifield {
	// Store the error raised by the validation logic.
	protected Status error;
	
	// The elements the multifield should reflect an error to.
	protected List<? extends IReflectsStatus> elements;
	
	// The error label that should display the error message.
	protected IReflectsStatus statusErrorLabel;
	
	/**
	 * Base constructor. Attaches the form elements.
	 * @param elements			All form elements that need to reflect whether there was a validation error or not.
	 * @param statusErrorLabel	The element that reflects the validation error status.
	 */
	public ValidatedMultifield(List<? extends IReflectsStatus> elements, IReflectsStatus statusErrorLabel) {
		this.elements = elements;
		this.statusErrorLabel = statusErrorLabel;
	}

	/**
	 * This method validates the object and provides an error message string.
	 * It is defined lower in the type hierarchy than when it is called, by ApplyLogic.
	 * @return 	A human-readable error description, or null if valid.
	 */
	public abstract Status Validate();
	
	/**
	 * Updates the error labels.
	 */
	public void Sync() {
		if (elements != null)
			for (IReflectsStatus item : elements)
				if (item != statusErrorLabel)
					item.setStatus(null);
		
		if (statusErrorLabel != null)
			statusErrorLabel.setStatus(error);
	}
	
	/**
	 * Applies logic.
	 */
	public boolean applyLogic() {
		error = Validate();
		return (error == null);
	}
	
	/**
	 * Exposes the error message, for Forms that do not display each error message
	 * attached to each field.
	 */
	public Status getError() {
		return error;
	}
}
