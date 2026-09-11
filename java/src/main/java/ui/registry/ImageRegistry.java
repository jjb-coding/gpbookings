
package ui.registry;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import services.fileService.FileService;
import services.fileService.FileServiceSingleton;
import services.fileService.Paths;

public enum ImageRegistry {
	// -----------------------------
	ICON_BUTTON_LOG_OUT,
	ICON_BACK,
	ICON_YES,
	ICON_NO,
	ICON_NA,
	ICON_YES_GO,
	
	WELCOME_BANNER
	;

	// -----------------------------
	BufferedImage image;
	
	/**
	 * 
	 */
	ImageRegistry() {
		FileService fileService = FileServiceSingleton.INSTANCE.get();
		try {
			image = ImageIO.read(
				fileService.getVirtualFileOne(
					Paths.IMAGES,
					this.name()
				));
		}
		catch (Exception e) {
			throw new RuntimeException("ImageRegistry: Couldn't find " + this.name());
		}
	}
	
	/**
	 * Retrieves the image represented by this enum entry.
	 * @return	The image.
	 */
	public BufferedImage getImage() {
		return image;
	}
	
	/**
	 * Creates an image icon using this image.
	 * @return
	 */
	public ImageIcon getIcon() {
		return new ImageIcon(image);
	}
}
