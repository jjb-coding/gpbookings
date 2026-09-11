package ui.component;

import java.util.ArrayList;

import javax.swing.JComboBox;

import services.practiceProvider.PracticeProvider;
import services.practiceProvider.PracticeProviderSingleton;
import ui.object.Identifier;

/**
 * Extension of JComboBox, streamlined for displaying data
 * from the DataProvider.
 */
public abstract class XComboBoxData<T> extends JComboBox<Identifier<T>> {
	// Inject the PracticeProvider service
	protected PracticeProvider _practiceProvider = PracticeProviderSingleton.INSTANCE.get();
	
	// The serial Version UID
	private static final long serialVersionUID = 1L;
	
	/**
	 * Passes on constructor chaining to JComboBox's constructor.
	 */
	public XComboBoxData() {
		super();
	}
	
	/**
	 * Populate based on a list of identifiers. Self-returning.
	 * @param identifiers The list of identifiers
	 */
	public XComboBoxData<T> populate(ArrayList<? extends Identifier<T>> identifiers) {
		this.removeAllItems();
		for (Identifier<T> identifier : identifiers)
			addItem(identifier);
		return this;
	}
}
