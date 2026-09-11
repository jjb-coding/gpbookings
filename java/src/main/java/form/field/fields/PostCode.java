package form.field.fields;

import java.util.regex.Pattern;

import javax.swing.JTextField;

import form.field.ValidatedText;
import status.Status;
import ui.component.IReflectsStatus;

public class PostCode extends ValidatedText {
	Pattern[] patterns;
	
	/**
	 * Constructs a UK postcode validation object.
	 * @param partner		The text field the object is tied to.
	 * @param errorLabel	The error label the object is tied to.
	 */
	public PostCode(JTextField partner, IReflectsStatus errorReflection) {
		super(partner, errorReflection);
		patterns = new Pattern[] {
			Pattern.compile("[A-Z][0-9] [0-9][A-Z][A-Z]"),
			Pattern.compile("[A-Z][0-9][0-9] [0-9][A-Z][A-Z]"),
			Pattern.compile("[A-Z][A-Z][0-9] [0-9][A-Z][A-Z]"),
			Pattern.compile("[A-Z][A-Z][0-9][0-9] [0-9][A-Z][A-Z]"),
			Pattern.compile("[A-Z][0-9][A-Z] [0-9][A-Z][A-Z]"),
			Pattern.compile("[A-Z][A-Z][0-9][A-Z] [0-9][A-Z][A-Z]"),
		};
	}	

	/**
	 * Trims and shifts to upper case.
	 */
	@Override
	public String Smarten(String in) {
		in = super.Smarten(in);
		
		return in.toUpperCase();
	}
	
	/**
	 * Ensures it is a valid UK postcode.
	 */
	public Status Validate(String in) {
		for (Pattern pattern : patterns)
			if (pattern.matcher(in).matches())
				return null;
		return Status.FIELD_POSTCODE_INVALID;
	}
}
