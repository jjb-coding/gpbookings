package form.field.fields;

import javax.swing.JTextField;

import form.field.ValidatedText;
import status.Status;
import ui.component.IReflectsStatus;
import util.CharacterSets;

public class PersonalName extends ValidatedText {	
	/**
	 * Constructs a personal name validation object.
	 * @param partner		The text field the object is tied to.
	 * @param errorLabel	The error label the object is tied to.
	 */
	public PersonalName(JTextField partner, IReflectsStatus errorReflection) {
		super(partner, errorReflection);
	}
	
	/**
	 * Raises the first character of each word.
	 */
	@Override
	public String Smarten(String in) {
		in = super.Smarten(in);
		
		int len = in.length();
		boolean raiseNext;
		char[] chrs = new char[len];
		
		for (int i = 0; i < len; i++) {
            char c = in.charAt(i);
            raiseNext = false;
            
            if (raiseNext)
            	chrs[i] = Character.toUpperCase(c);
        	else
        		chrs[i] = c;
            
            if (c == ' ')
            	raiseNext = true;
		}
		
		return String.copyValueOf(chrs);
	}

	/**
	 * Ensures that there are no symbols that could not be present
	 * in given names, and that it is more than one character long.
	 */
	@Override
	public Status Validate(String in) {
		int len = in.length();
		if (len <= 1)
			return Status.FIELD_PERSONAL_NAME_INVALID_CHARACTERS;

		for (int i = 0; i < len; i++) {
            char c = in.charAt(i);
    		if (!(CharacterSets.UPPER.has(c) || CharacterSets.LOWER.has(c) || CharacterSets.DIGIT.has(c)))
    			return Status.FIELD_PERSONAL_NAME_INVALID_CHARACTERS;
        }
		return null;
	}
}
