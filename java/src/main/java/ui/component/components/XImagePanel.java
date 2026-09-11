package ui.component.components;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import ui.registry.ImageRegistry;

public class XImagePanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;

    public XImagePanel(ImageRegistry imagePalette) {
    	image = imagePalette.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}