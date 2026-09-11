package form.field.fields;

import javax.swing.JPasswordField;

import form.field.ValidatedPassword;
import status.Status;
import ui.component.IReflectsStatus;

public class Password extends ValidatedPassword {
	// Injects the securityService singleton
	//SecurityService _securityService = SecurityServiceSingleton.INSTANCE.get();
	
	/**
	 * Constructs a password validation object.
	 * @param partner		The password field the object is tied to.
	 * @param errorLabel	The error label the object is tied to.
	 */
	public Password(JPasswordField partner, IReflectsStatus errorReflection) {
		super(partner, errorReflection);
	}
	
	/**
	 * Ensures password is not empty. No other assumptions can be made
	 * as password creation validation may have changed.
	 */
	@Override
	public Status Validate(char[] in) {
		if (in.length == 0)
			return Status.FIELD_PASSWORD_EMPTY;
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
