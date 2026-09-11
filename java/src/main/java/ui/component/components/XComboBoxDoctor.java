package ui.component.components;

import ui.component.XComboBoxData;

/**
 * Extension of JComboBoxData, for displaying doctors.
 */
public class XComboBoxDoctor extends XComboBoxData<String> {
	// The serial Version UID
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new JComboBoxData and populates it with the provided list of identifiers.
	 */
	public XComboBoxDoctor() {
		super();
		populate(_practiceProvider.getDoctorsAsIdentifiers());
	}
}
