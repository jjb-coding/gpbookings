package form.field.fields;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;

import form.field.ValidatedText;
import status.Status;
import ui.component.IReflectsStatus;

/**
 * Email address field.
 */
public class EmailAddress extends ValidatedText {
	Pattern pattern;
	
	/**
	 * Constructs an email address validation object.
	 * @param partner		The text field the object is tied to.
	 * @param errorLabel	The error label the object is tied to.
	 */
	public EmailAddress(JTextField partner, IReflectsStatus errorReflection) {
		super(partner, errorReflection);
		pattern = Pattern.compile("[A-Za-z0-9]*[-A-Za-z0-9]*[A-Za-z0-9]@([A-Za-z0-9]*[-A-Za-z0-9]*[A-Za-z0-9].)*[A-Za-z0-9]*[-A-Za-z0-9]*[A-Za-z0-9]", Pattern.CASE_INSENSITIVE);
	}
	
	/**
	 * Trims and shifts to lower case.
	 */
	@Override
	public String Smarten(String in) {
		in = super.Smarten(in);
		
		return in.toLowerCase();
	}
	
	/**
	 * Full email validation, with many error types.
	 */
	@Override
	public Status Validate(String in) {
		Matcher matcher = pattern.matcher(in);
		if (matcher.matches())
			return null;
		else
			return Status.FIELD_EMAIL_INVALID;
	}
}
