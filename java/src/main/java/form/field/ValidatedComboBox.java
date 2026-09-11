package form.field;

import ui.component.IReflectsStatus;
import ui.component.XComboBoxData;
import ui.object.Identifier;

/**
 * A validated combination box.
 * @param <T> The type that is provided when id() is called on an Identifier the box contains.
 */
public abstract class ValidatedComboBox<T> extends ValidatedField<Identifier<T>, T> {
	XComboBoxData<T> partner;
	
	/**
	 * Attaches the combo box partner, and passes on the
	 * the errorLabel partner to the superclass constructor.
	 * @param partner		The text field the object is tied to.
	 * @param errorLabel	The error label the object is tied to.
	 */
	public ValidatedComboBox(XComboBoxData<T> partner, IReflectsStatus errorReflection) {
		super(errorReflection);
		this.partner = partner;
	}

	/**
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean ApplyLogic() {
		Identifier<T> value = (Identifier<T>)partner.getSelectedItem();
		
		error = this.Validate(value);
		
		postValue = (value == null) ? null : value.id();
		
		return (error == null);
	}

	/**
	 * Passes the call onto the super.
	 */
	@Override
	public void Sync() {
		super.Sync();
	}
}
