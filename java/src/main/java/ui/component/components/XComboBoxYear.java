package ui.component.components;

import java.time.Year;
import java.util.ArrayList;

import ui.component.XComboBoxData;
import ui.object.Identifier;
import ui.object.NumberItem;

/**
 * Extension of JComboBoxData, for displaying years.
 */
public class XComboBoxYear extends XComboBoxData<Integer> {
	// The serial Version UID
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new JComboBoxYear and populates it with the provided list of identifiers.
	 */
	public XComboBoxYear() {
		super();
		populate(years);
		this.setSelectedItem(currentYear);
	}
	
	static final ArrayList<? extends Identifier<Integer>> years;
	static Identifier<Integer> currentYear;
	static {
		// Setup
		ArrayList<NumberItem> list = new ArrayList<>();
		int currentYearNumber = Year.now().getValue();

		// For some range
		for (int i = 1999; i < currentYearNumber + 1; i++) {
			NumberItem numberItem = new NumberItem(i);
			list.add(numberItem);
			if (i == currentYearNumber)
				currentYear = numberItem;
		}
		years = list;
	}
}