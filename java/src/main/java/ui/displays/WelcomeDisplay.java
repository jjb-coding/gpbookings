package ui.displays;

import javax.swing.*;

import ui.component.components.XImagePanel;
import ui.container.IDisplay;
import ui.container.InjectableEnum;
import ui.container.Node;
import ui.controllers.WelcomeController;
import ui.registry.ColourRegistry;
import ui.registry.ImageRegistry;
import util.UIUtils;

import java.awt.*;

/**
 * Welcome page that allows users to choose between login and registration
 */
public class WelcomeDisplay extends JPanel
	implements IDisplay {
	// The serial version ID.
	private static final long serialVersionUID = 1L;
	// Fields
	Node _parent;
    
    public WelcomeDisplay(Node _parent) {
    	// Super
    	super(new BorderLayout(0, 20));
    	// Set parent
    	this._parent = _parent;
    	// Injections
    	WelcomeController _controller = (WelcomeController)inject(InjectableEnum.WELCOME_CONTROLLER);

        // *** UI 
        // * COMPONENTS
        // Create buttons with consistent size
        JButton loginButton = UIUtils.createStyledButton("Login");
        JButton registerButton = UIUtils.createStyledButton("Register New Account");
        
        Dimension buttonSize = new Dimension(200, 40);
        loginButton.setPreferredSize(buttonSize);
        registerButton.setPreferredSize(buttonSize);
        
        setVisible(true);
         
        // * NORTH -> HEADER & SUBTITLE
        // Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel welcomeLabel = new JLabel("Welcome To The Medical System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(welcomeLabel);
        
        // Subtitle
        JPanel subtitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel subtitleLabel = new JLabel("Please select an option to continue:");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitlePanel.add(subtitleLabel);
        
        // Combine header and subtitle
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ColourRegistry.APP_BACKGROUND_PALE.getColour());
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(subtitlePanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        
        // * CENTER -> Buttons
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        
        // Add login button with proper alignment
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.add(loginButton);
        buttonPanel.add(loginPanel);
        
        // Add spacing between buttons
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Add register button with proper alignment
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerPanel.add(registerButton);
        buttonPanel.add(registerPanel);
        
        // Container
        JPanel buttonPanelContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanelContainer.add(buttonPanel);
        add(buttonPanelContainer, BorderLayout.CENTER);
        
        // * SOUTH -> Banner
        XImagePanel imagePanel = new XImagePanel(ImageRegistry.WELCOME_BANNER);
        imagePanel.setPreferredSize(new Dimension(600, 310));
        add(imagePanel, BorderLayout.SOUTH);
        
        // * THIS
        setBorder(BorderFactory.createEmptyBorder(90, 60, 90, 60));
        setBackground(ColourRegistry.APP_BACKGROUND.getColour());
        
        // * OPAQUE
        buttonPanel.setOpaque(false);
        loginPanel.setOpaque(false);
        registerPanel.setOpaque(false);
        buttonPanelContainer.setOpaque(false);
        //topPanel.setOpaque(false);
        subtitlePanel.setOpaque(false);
        headerPanel.setOpaque(false);
        
        // *** ATTACH
        _controller.attach(
        		loginButton,
        		registerButton
        		);
    }
    
	@Override
	public Node getNode() {
		return _parent;
	}
}