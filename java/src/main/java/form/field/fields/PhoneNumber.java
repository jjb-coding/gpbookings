package form.field.fields;

import javax.swing.JTextField;

import form.field.ValidatedText;
import status.Status;
import ui.component.IReflectsStatus;
import util.CharacterSets;

public class PhoneNumber extends ValidatedText {
	/**
	 * Constructs a phone number validation object.
	 * @param partner		The text field the object is tied to.
	 * @param errorLabel	The error label the object is tied to.
	 */
	public PhoneNumber(JTextField partner, IReflectsStatus errorReflection) {
		super(partner, errorReflection);
	}
	
	/**
	 * Adds a + if otherwise of international phone number length.
	 */
	@Override 
	public String Smarten(String in) {
		in = super.Smarten(in);
		
		if (in.length() == 12)
			return "+" + in;
		return in;
	}

	/**
	 * Ensures it is either 11 digits or 12 digits with a + symbol.
	 */
	public Status Validate(String in) {
		
		int len = in.length();
		if (!(len == 13 || len == 11))
			return Status.FIELD_PHONE_NUMBER_INVALID_LENGTH;
		
		for (int i = 0; i < len; i++) {
            char c = in.charAt(i);
            if (len == 13 && i == 0) {
               	if (!(c == '+'))
            		return Status.FIELD_PHONE_NUMBER_NEEDS_PLUS;
            }
            else if (!(CharacterSets.DIGIT.has(c)))
            	return Status.FIELD_PHONE_NUMBER_INVALID_CHARACTERS;
        }
		return null;
	}
}
