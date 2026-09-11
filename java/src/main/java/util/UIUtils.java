package util;

import java.awt.Dimension;

import javax.swing.JButton;

public class UIUtils {
	/**
     * Creates a nicely styled button
     */
    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 40));
        button.setFocusPainted(false);
        return button;
    }
}
