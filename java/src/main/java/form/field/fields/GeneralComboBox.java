package form.field.fields;

import form.field.ValidatedComboBox;
import status.Status;
import ui.component.IReflectsStatus;
import ui.component.XComboBoxData;
import ui.object.Identifier;

public class GeneralComboBox<T> extends ValidatedComboBox<T> {	
	/**
	 * Constructs an general combo box validation object.
	 * @param partner		The combo box the object is tied to.
	 * @param errorLabel	The error label the object is tied to.
	 */
	public GeneralComboBox(XComboBoxData<T> partner, IReflectsStatus errorReflection) {
		super(partner, errorReflection);
	}
		
	/**
	 * Checks whether nothing has been selected.
	 */
	@Override
	public Status Validate(Identifier<T> in) {
		if (in == null)
			return Status.FIELD_GENERAL_COMBO_BOX_UNSELECTED;
		else
			return null;
	}
}
