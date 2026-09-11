package ui.displays;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import ui.component.components.XLabelTextParametrised;
import ui.container.IDisplay;
import ui.container.InjectableEnum;
import ui.container.Node;
import ui.controllers.ElementAccountBarController;
import ui.registry.ColourRegistry;
import ui.registry.ImageRegistry;
import ui.registry.TextRegistry;
import ui.registry.TextStyleRegistry;

public class ElementAccountBarDisplay extends JPanel
	implements IDisplay {
	// The serial version ID.
	private static final long serialVersionUID = 1L;
	
	// Fields
	Node _parent;
	
	// Components
	JPanel child;
	
	public ElementAccountBarDisplay(Node _parent) {
		// Super
		super(new BorderLayout(0, 0));
		// Set parent
		this._parent = _parent;
		// Inject
		ElementAccountBarController _controller = (ElementAccountBarController)inject(InjectableEnum.ACCOUNTBAR_CONTROLLER);
		
		// *** UI
		
		// Logout
		JButton logOutButton = new JButton(ImageRegistry.ICON_BUTTON_LOG_OUT.getIcon());
		logOutButton.setOpaque(false);
		JButton backButton = new JButton(ImageRegistry.ICON_BACK.getIcon());
		backButton.setOpaque(false);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		buttonPanel.add(logOutButton);
		buttonPanel.add(backButton);
		buttonPanel.setOpaque(false);
		
		// Text
		XLabelTextParametrised bannerText = new XLabelTextParametrised(TextRegistry.PARAMETRISED, TextStyleRegistry.EMAIL_ADDRESS);
		JPanel bannerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		bannerPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		bannerPanel.setOpaque(false);
		bannerPanel.add(bannerText);

		// Containerise & add
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ColourRegistry.ACCOUNT_BAR_BACKGROUND.getColour());
        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(bannerPanel, BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);
		
		// * THIS
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
		
		// *** ATTACH
		_controller.attach(
				logOutButton,
				backButton,
				bannerText
				);
	}

	@Override
	public Node getNode() {
		return _parent;
	}

	@Override
	public void swapChild(IDisplay child) {
	    // Clear
		for (Component component : getComponents())
	        if (BorderLayout.CENTER.equals(((BorderLayout)getLayout()).getConstraints(component)))
	            remove(component);
	    			
	    // Swap
 		add((JPanel)child, BorderLayout.CENTER);
	};
}
