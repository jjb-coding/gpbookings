package form.form;

import form.field.ValidatedField;
import form.multifield.ValidatedMultifield;
import status.Status;
import ui.component.IReflectsStatus;
import ui.container.AppContainer;
import ui.container.Base;
import ui.container.InjectableObject;

import java.util.ArrayList;

/**
 * 
 */
public class Form extends InjectableObject {
	// All fields that the form contains 
	ArrayList<ValidatedField<?,?>> fields;
	ArrayList<ValidatedMultifield> multifields;
	
	// The form-level validation error message
	String error;
	
	// The error component
	IReflectsStatus errorReflection;
	
	/**
	 * Constructs a Form instance.
	 * @param _appContainer	The app container.
	 * @param parent		The parent.
	 */
	public Form(AppContainer _appContainer, Base parent) {
		super(_appContainer, parent);
		fields = new ArrayList<>();
		multifields = new ArrayList<>();
	}
		
	/**
	 * Sets the form's error reflection object, generally a label.
	 * @param reflection	The object.
	 * @return				The object just set.
	 */
	public IReflectsStatus attachFormErrorReflection(IReflectsStatus reflection) {
		this.errorReflection = reflection;
		return reflection;
	}
	
	/**
	 * Adds a field to the form.
	 * @param field The field to add.
	 * @return 		The field just added.
	 */
	public <T extends ValidatedField<?,?>> T attach(T field) {
		fields.add(field);
		return field;
	}
	
	/**
	 * Adds a multifield to the form.
	 * @param multifield	 The field to add.
	 * @return 				 The field just added.
	 */
	public <T extends ValidatedMultifield> T attach(T multifield) {
		multifields.add(multifield);
		return multifield;
	}
	
	/**
	 * Executes the form.
	 */
	public boolean execute() {
		// Call field-specific Logic; return is Validation
		boolean allCorrect = true;
		for (ValidatedField<?,?> field : fields)
			allCorrect &= field.ApplyLogic();
		
		// Synchronise form elements with their partners
		for (ValidatedField<?,?> field : fields)
			field.Sync();
		
		if (!allCorrect) {
			errorReflection.setStatus(Status.FIELD_INVALID);
			return false;	
		}
		
		// Do multifields
		for (ValidatedMultifield multifield : multifields)
			allCorrect &= multifield.applyLogic();
		
		// Synchronise multifield elements with their partners
		for (ValidatedMultifield multifield : multifields)
			multifield.Sync();
		
		// Return validation state
		return allCorrect;
	}
}