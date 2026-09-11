package ui.component.components;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import ui.registry.ImageRegistry;
import ui.registry.TextRegistry;

public class XImageButton extends JButton {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;

    public XImageButton(ImageRegistry imagePalette) {
    	image = imagePalette.getImage();
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
    }
    
    public XImageButton(TextRegistry text, ImageRegistry imagePalette) {
    	super(text.getString());
    	image = imagePalette.getImage();
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image != null)
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        super.paintComponent(g);
    }
}