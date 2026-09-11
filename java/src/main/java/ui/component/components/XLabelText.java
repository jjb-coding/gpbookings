package ui.component.components;

import javax.swing.JLabel;

import ui.registry.TextRegistry;
import ui.registry.TextStyleRegistry;

public class XLabelText extends JLabel {
	// The serial version UID.
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs an XLabelText object with no default text or style.
	 * Must run on the EDT.
	 */
	public XLabelText() {
		super();
	}
	
	/**
	 * Constructs an XLabelText object with text.
	 * Must run on the EDT.
	 * @param text		The text registry item.
	 */
	public XLabelText(TextRegistry text) {
		super();
		setText(text);
	}
	
	/**
	 * Constructs an XLabelText object with a style.
	 * Must run on the EDT.
	 * @param textStyle	The text style registry item.
	 */
	public XLabelText(TextStyleRegistry textStyle) {
		super();
		setStyle(textStyle);
	}
	
	/**
	 * Constructs an XLabelText object with text and a style.
	 * Must run on the EDT.
	 * @param textStyle	The text style registry item.
	 * @param text		The text registry item.
	 */
	public XLabelText(TextStyleRegistry textStyle, TextRegistry text) {
		super();
		set(textStyle, text);
	}
	
	/**
	 * Constructs an XLabelText object with text and a style.
	 * Must run on the EDT.
	 * @param textStyle	The text style registry item.
	 * @param text		The text registry item.
	 */
	public XLabelText(TextStyleRegistry textStyle, TextRegistry text, TextAlignment alignment) {
		super();
		set(textStyle, text, alignment);
	}
	
	/**
	 * Sets the text and style properties of the XLabelText object.
	 * Must run on the EDT.
	 * @param textStyle	The text style registry item.
	 * @param text		The text registry item.
	 * @return 			Self-returning.
	 */
	public XLabelText set(TextStyleRegistry textStyle, TextRegistry text) {
		setStyle(textStyle);
		setText(text);
		return this;
	}
	
	/**
	 * Sets the text and style properties of the XLabelText object.
	 * Must run on the EDT.
	 * @param textStyle	The text style registry item.
	 * @param text		The text registry item.
	 * @return 			Self-returning.
	 */
	public XLabelText set(TextStyleRegistry textStyle, TextRegistry text, TextAlignment alignment) {
		setStyle(textStyle);
		setText(text, alignment);
		return this;
	}
	
	
	/**
	 * Sets the style property of the XLabelText object.
	 * Must run on the EDT.
	 * @param textStyle	The text style registry item.
	 * @return 			Self-returning.	
	 */
	public XLabelText setStyle(TextStyleRegistry textStyle) {
		this.setFont(textStyle.getFont());
		this.setForeground(textStyle.getColour());
		return this;
	}
	
	/**
	 * Sets the text of the XLabelText object.
	 * Must run on the EDT.
	 * @param text		The text registry item.
	 * @return 			Self-returning.
	 */
	public XLabelText setText(TextRegistry text) {
		this.setText(text.getString());
		return this;
	}
	
	/**
	 * Sets the text of the XLabelText object.
	 * Must run on the EDT.
	 * @param text		The text registry item.
	 * @return 			Self-returning.
	 */
	public XLabelText setText(TextRegistry text, TextAlignment alignment) {
		String alignmentText;
		if (alignment == TextAlignment.LEFT)
			alignmentText = "left";
		else if (alignment == TextAlignment.CENTRE)
			alignmentText = "center";
		else
			alignmentText = "right";
		
		this.setText(
				"<html><" + alignmentText + ">"
				+ text.getString()
				+ "<" + alignmentText + "/></html>"
				);
		return this;
	}
}
