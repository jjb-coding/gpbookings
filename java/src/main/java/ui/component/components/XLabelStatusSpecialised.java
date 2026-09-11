package ui.component.components;

import status.Status;
import ui.component.IReflectsStatus;
import ui.registry.TextRegistry;

/**
 * 
 */
public class XLabelStatusSpecialised
	extends XLabelStatus
	implements IReflectsStatus {
	// The serial version UID.
	private static final long serialVersionUID = 1L;

	TextRegistry text;
	
	/**
	 * Constructs a JLabelStatus object with a default.
	 * @param status	The default status.
	 */
	public XLabelStatusSpecialised(TextRegistry text) {	
		super(null);
		this.text = text;
	}
	
	/**
	 * Binds the label to a status.
	 * @param status	The status. May be null.
	 */
	@Override
	public void setStatus(Status status) {
		// Handle null case
		if (status == null) {
			this.setText("");
			return;
		}
		
		// If the status has no text, it is a functional status, not for displaying
		if (status.getText() == "")
			throw new RuntimeException("XLabel:runtime: Attempted to bind non-text displaying status " + status.name());
				
		// Set the properties
		setForeground(status.getColour());
		this.setText(String.format(status.getText(), text.getString()));
	}
}
