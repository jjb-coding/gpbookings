package ui.component.components;

import java.awt.Color;

import javax.swing.JLabel;

import status.Status;
import ui.component.IReflectsStatus;
import ui.registry.TextStyleRegistry;

/**
 * 
 */
public class XLabelStatus
	extends JLabel
	implements IReflectsStatus {
	// *** STATIC
	private final static Color transparentColor = new Color(0f, 0f, 0f, 0.0f);
	
	// The serial version UID.
	private static final long serialVersionUID = 1L;

	
	private String css;
	
	/**
	 * Constructs a JLabelStatus object with a default.
	 * @param status	The default status.
	 */
	public XLabelStatus(Status status) {
		super();
		css = null;
		setStyle(TextStyleRegistry.STATUS_LABEL_DEFAULT);
		setStatus(status);
	}
	
	/**
	 * Constructs a JLabelStatus object with a default.
	 * @param status	The default status.
	 */
	public XLabelStatus(Status status, String css) {
		super();
		this.css = css;
		setStyle(TextStyleRegistry.STATUS_LABEL_DEFAULT);
		setStatus(status);
	}
	
	/**
	 * Gives the label a particular style. Omits the colour
	 * property, which is controlled by the status.
	 * @param textStyle	
	 */
	public void setStyle(TextStyleRegistry textStyle) {
		this.setFont(textStyle.getFont());
	}
	
	/**
	 * Binds the label to a status.
	 * @param status	The status. May be null.
	 */
	@Override
	public void setStatus(Status status) {
		// Handle null case
		if (status == null) {
			this.setText("---------------------");
			this.setForeground(transparentColor);
			return;
		}
		
		// If the status has no text, it is a functional status, not for displaying
		if (status.getText() == "")
			throw new RuntimeException("XLabel:runtime: Attempted to bind non-text displaying status " + status.name());
		
		// Set the properties
		setForeground(status.getColour());
		if (css != null)
			this.setText("<html><div style='" + css + "'>" + status.getText() + "</div></html>");
		else
			this.setText(status.getText());
	}
}
