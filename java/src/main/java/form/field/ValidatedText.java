package form.field;

import javax.swing.*;

import ui.component.IReflectsStatus;


public abstract class ValidatedText extends ValidatedField<String, String> {
	// The partner control
	protected JTextField partner;
	
	/**
	 * Attaches the text field partner, and passes on the
	 * the errorLabel partner to the superclass constructor.
	 * @param partner		The text field the object is tied to.
	 * @param errorLabel	The error label the object is tied to.
	 */
	public ValidatedText(JTextField partner, IReflectsStatus errorReflection) {
		super(errorReflection);
		this.partner = partner;
	}
	
	/**
	 * Implementation of local logic application,
	 * for text fields.
	 */
	@Override
	public boolean ApplyLogic() {
		String value = partner.getText();
		value = this.Smarten(value);
		
		error = this.Validate(value);
		
		postValue = value;
		
		return (error == null);
	}
	
	/**
	 * Smartening allows for transformation of the value before
	 * validation. Defined method is default behaviour and may be overridden.
	 * @param in	The value, as char[] type.
	 * @return		The transformed value, as String type.
	 */
	public String Smarten(String in) { return in.trim(); };
	
	/**
	 * Updates the text field.
	 */
	@Override
	public void Sync() {
		super.Sync();
		partner.setText(postValue);
	}
}