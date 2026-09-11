package ui.component.components;

import javax.swing.JLabel;

import ui.registry.TextRegistry;
import ui.registry.TextStyleRegistry;

public class XLabelTextParametrised extends JLabel {
	// The serial version UID.
	private static final long serialVersionUID = 1L;
	TextRegistry text;
	TextStyleRegistry textStyle;
	
	/**
	 * Constructs an XLabelTextParametrised object with unformatted text.
	 * Must run on the EDT.
	 * @param text		The text registry item.
	 */
	public XLabelTextParametrised(TextRegistry text) {
		super();
		this.text = text;
	}
	
	/**
	 * Constructs an XLabelTextParametrised object with unformatted text and a default formatting parameter.
	 * Must run on the EDT.
	 * @param text		The text registry item.
	 */
	public XLabelTextParametrised(TextRegistry text, String parameter) {
		super();
		this.text = text;
		setParameter(parameter);
	}
	
	/**
	 * Constructs an XLabelTextParametrised object with a style and unformatted text.
	 * Must run on the EDT.
	 * @param textStyle	The text style registry item.
	 */
	public XLabelTextParametrised(TextRegistry text, TextStyleRegistry textStyle) {
		super();
		this.text = text;
		this.textStyle = textStyle;
		setStyle(textStyle);
	}
	
	/**
	 * Constructs an XLabelTextParametrised object with text, style and a parameter.
	 * Must run on the EDT.
	 * @param textStyle	The text style registry item.
	 * @param text		The text registry item.
	 */
	public XLabelTextParametrised(TextRegistry text, TextStyleRegistry textStyle, String parameter) {
		super();
		this.text = text;
		this.textStyle = textStyle;
		setStyle(textStyle);
		setParameter(parameter);
	}
	
	/**
	 * Sets the style property of the XLabelTextParametrised object.
	 * Must run on the EDT.
	 * @param textStyle	The text style registry item.
	 * @return 			Self-returning.	
	 */
	public XLabelTextParametrised setStyle(TextStyleRegistry textStyle) {
		setFont(textStyle.getFont());
		setForeground(textStyle.getColour());
		return this;
	}
	
	/**
	 * Reflects a parameter through string formatted, to the XLabelTextParametrised object.
	 * Must run on the EDT.
	 * @param text		The text registry item.
	 * @return 			Self-returning.
	 */
	public XLabelTextParametrised setParameter(String parameter) {
		this.setText(String.format(text.getString(), parameter));
		return this;
	}
}
