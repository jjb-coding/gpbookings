package ui.component.components;

import java.sql.Date;

import ui.component.XComboBoxData;

/**
 * Extension of JComboBoxData, for displaying a handful of dates into the immediate future.
 */
public class XComboBoxDate extends XComboBoxData<Date> {
	// The serial Version UID
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new JComboBoxData and populates it with the provided list of identifiers.
	 */
	public XComboBoxDate() {
		super();
		populate(_practiceProvider.getDays(16));
	}
}
