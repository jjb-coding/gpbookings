package ui.component.components;

import java.time.LocalDate;
import java.util.ArrayList;
import ui.component.XComboBoxData;
import ui.object.EnumeratedItem;
import ui.object.Identifier;

/**
 * Extension of JComboBoxData, for displaying months.
 */
public class XComboBoxMonth extends XComboBoxData<Integer> {
	// The serial Version UID
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new JComboBoxMonth and populates it with the provided list of identifiers.
	 */
	public XComboBoxMonth() {
		super();
		populate(months);
		this.setSelectedItem(currentMonth);
	}
	
	/**
	 * Initialises the months.
	 */
	static final ArrayList<? extends Identifier<Integer>> months;
	static Identifier<Integer> currentMonth;
	static {
		// Setup
		ArrayList<EnumeratedItem> list = new ArrayList<>();
		int currentMonthNumber = LocalDate.now().getMonthValue() - 1;
		
		// Populate list
		list.add(new EnumeratedItem(1, "January"));
		list.add(new EnumeratedItem(2, "February"));
		list.add(new EnumeratedItem(3, "March"));
		list.add(new EnumeratedItem(4, "April"));
		list.add(new EnumeratedItem(5, "May"));
		list.add(new EnumeratedItem(6, "June"));
		list.add(new EnumeratedItem(7, "July"));
		list.add(new EnumeratedItem(8, "August"));
		list.add(new EnumeratedItem(9, "September"));
		list.add(new EnumeratedItem(10, "November"));
		list.add(new EnumeratedItem(11, "October"));
		list.add(new EnumeratedItem(12, "December"));
		months = list;
		
		// Assign currentMonth
		currentMonth = months.get(currentMonthNumber);
	}
}
