package form.field.fields;

import javax.swing.JTextField;

import form.field.ValidatedText;
import status.Status;
import ui.component.IReflectsStatus;

public class GeneralText extends ValidatedText {
	/**
	 * Constructs a generic text object with no significant validation.
	 * @param partner		The text field the object is tied to.
	 * @param errorLabel	The error label the object is tied to.
	 */
	public GeneralText(JTextField partner, IReflectsStatus errorReflection) {
		super(partner, errorReflection);
	}

	/**
	 * Ensures the value isn't excessively long.
	 */
	@Override
	public Status Validate(String in) {
		if (in.length() > 64)
			return Status.FIELD_TEXT_TOO_LONG;
		return null;
	}

}
