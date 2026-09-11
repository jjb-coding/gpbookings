package form.multifield.multifields;

import java.util.Arrays;

import form.field.ValidatedField;
import form.multifield.ValidatedMultifield;
import status.Status;
import ui.component.IReflectsStatus;

public class Matches extends ValidatedMultifield {
	// The fields to match
	ValidatedField<?, byte[]> a;
	ValidatedField<?, byte[]> b;

	/**
	 * 
	 * @param errorLabels
	 * @param outputErrorLabel
	 */
	public Matches(
			ValidatedField<?, byte[]> a,
			IReflectsStatus aErrorLabel,
			ValidatedField<?, byte[]> b,
			IReflectsStatus bErrorLabel
			) {
		super(Arrays.asList(bErrorLabel), aErrorLabel);
		this.a = a;
		this.b = b;
	}

	/**
	 * Determines if the two fields match. 
	 * @return 
	 */
	@Override
	public Status Validate() {
		if (a.postValue.length != b.postValue.length)
			return Status.MULTIFIELD_MATCHES_INVALID;
			
		for (int i = 0; i < a.postValue.length; i++)
			if (a.postValue[i] != b.postValue[i])
				return Status.MULTIFIELD_MATCHES_INVALID;
		
		return null;
	}
}
