package form.field;

import javax.swing.JPasswordField;

import services.securityService.SecurityService;
import services.securityService.SecurityServiceSingleton;
import ui.component.IReflectsStatus;

public abstract class ValidatedPassword extends ValidatedField<char[], byte[]> {
	// Singletons
	SecurityService _securityService;
	// The partner control
	protected JPasswordField partner;
	
	/**
	 * Attaches the password field partner, and passes on the
	 * the errorLabel partner to the superclass constructor.
	 * @param partner		The password field the object is tied to.
	 * @param errorLabel	The error label the object is tied to.
	 */
	public ValidatedPassword(JPasswordField partner, IReflectsStatus errorReflection) {
		super(errorReflection);
		this.partner = partner;
		
		// Injections
		_securityService = SecurityServiceSingleton.INSTANCE.get(); 
	}
	
	/**
	 * Clean the partner using the security service.
	 */
	public void clean() {
		_securityService.clean(partner.getPassword());
	}
	
	
	/**
	 * Implementation of local logic application,
	 * for password fields.
	 */
	@Override
	public boolean ApplyLogic() {
		char[] value = partner.getPassword();
		
		error = this.Validate(value);
		
		postValue = PostProcess(value);
		
		return (error == null);
	}
	
	/**
	 * Post-processing allows for transformation of the value after
	 * validation. Defined method is default behaviour and may be overridden.
	 * @param in	The value, as char[] type.
	 * @return		The transformed value, as String type.
	 */
	public byte[] PostProcess(char[] in) {
		byte[] bytes = new byte[in.length];
		for (int i = 0; i < in.length; i++)
		    bytes[i] = (byte)in[i];
		return bytes;
	};
	
	/**
	 * Does nothing here, just calls super.
	 */
	@Override
	public void Sync() {
		super.Sync();
	}
}