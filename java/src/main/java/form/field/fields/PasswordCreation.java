package form.field.fields;

import javax.swing.JPasswordField;

import form.field.ValidatedPassword;
import services.securityService.SecurityService;
import services.securityService.SecurityServiceSingleton;
import status.Status;
import ui.component.IReflectsStatus;
import util.CharacterSets;

public class PasswordCreation extends ValidatedPassword {
	// Injects the securityService singleton
	SecurityService _securityService = SecurityServiceSingleton.INSTANCE.get();
	
	/**
	 * Constructs a password validation object.
	 * @param partner		The password field the object is tied to.
	 * @param errorLabel	The error label the object is tied to.
	 */
	public PasswordCreation(JPasswordField partner, IReflectsStatus errorReflection) {
		super(partner, errorReflection);
	}
	
	/**
	 * Ensures password meets minimum length requirements,
	 * contains a mixture of characters, etc.
	 * TODO
	 */
	@Override
	public Status Validate(char[] in) {
		if (in.length < 8)
			return Status.FIELD_PASSWORD_CREATION_TOO_SHORT;
		if (in.length > 32)
			return Status.FIELD_PASSWORD_CREATION_TOO_LONG;
		
		boolean hasSpecial, hasNumber, hasUpper, hasLower;
		hasSpecial = hasNumber = hasUpper = hasLower = false;
		
		for (int i = 0; i < in.length; i++) {
			hasSpecial |= CharacterSets.SPECIAL.has(in[i]);
			hasNumber |= CharacterSets.DIGIT.has(in[i]);
			hasUpper |= CharacterSets.UPPER.has(in[i]);
			hasLower |= CharacterSets.LOWER.has(in[i]);
		}
		
		if (!hasUpper)
			return Status.FIELD_PASSWORD_NEEDS_UPPERCASE;
		if (!hasLower)
			return Status.FIELD_PASSWORD_NEEDS_LOWERCASE;
		if (!hasSpecial)
			return Status.FIELD_PASSWORD_NEEDS_SPECIAL;
		if (!hasNumber)
			return Status.FIELD_PASSWORD_NEEDS_DIGIT;
		
		return null;
	}
	
	/**
	 * Hashes the password before transmission.
	 */
	@Override
	public byte[] PostProcess(char[] in) {
		//return _securityService.hashToBase64(in);
		return super.PostProcess(in);
	}

}
