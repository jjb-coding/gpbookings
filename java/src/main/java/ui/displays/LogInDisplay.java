	package ui.displays;

import javax.swing.*;

import ui.component.components.XLabelStatus;
import ui.component.components.XLabelText;
import ui.container.IDisplay;
import ui.container.InjectableEnum;
import ui.container.Node;
import ui.controllers.LogInController;
import ui.registry.ColourRegistry;
import ui.registry.TextRegistry;
import ui.registry.TextStyleRegistry;
import util.UIUtils;

import java.awt.*;

/**
 * Login GUI for the Medical System application
 */
public class LogInDisplay extends JPanel
	implements IDisplay {
    // The serial version ID.
	private static final long serialVersionUID = 1L;
	
	// Injections
	Node _parent;
	LogInController _controller;
	

    public LogInDisplay(Node _parent) {
    	// Call super
    	super(new BorderLayout());
    	// Set parent
    	this._parent = _parent;
    	// Get injectables
    	_controller = (LogInController)inject(InjectableEnum.LOGIN_CONTROLLER);
    	        
    	// * COMPONENTS
    	// Create input fields and labels
    	JTextField emailField = new JTextField(20);
        XLabelStatus emailError = new XLabelStatus(null);
        JPasswordField passwordField = new JPasswordField(20);
        XLabelStatus passwordError = new XLabelStatus(null);
        
        // Create buttons with consistent size
        JButton loginButton = UIUtils.createStyledButton("Login");
        JButton cancelButton = UIUtils.createStyledButton("Cancel");
        Dimension buttonSize = new Dimension(100, 25);
        loginButton.setPreferredSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        
        // Status
        XLabelStatus statusLabel = new XLabelStatus(null, "width: 200px"); 
        
        // * NORTH -> INSTRUCTION
        // Create instruction label
        XLabelText instructionLabel = new XLabelText(TextStyleRegistry.INSTRUCTION, TextRegistry.LOG_IN_INSTRUCTION);
        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(ColourRegistry.HEADER_BACKGROUND.getColour());
        northPanel.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createEtchedBorder(),
        	    BorderFactory.createEmptyBorder(10, 10, 10, 10)
        	));
        JPanel instructionContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        instructionContainer.add(instructionLabel);
        instructionContainer.setOpaque(false);
        northPanel.add(instructionContainer, BorderLayout.CENTER);
        
        // * CENTRE -> FORM PANEL
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Email label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.7;
        formPanel.add(new XLabelText(TextStyleRegistry.FIELD_LABEL, TextRegistry.EMAIL_LABEL), gbc);
        
        // Email field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        formPanel.add(emailField, gbc);
        
        // Email error
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        formPanel.add(emailError, gbc);
        
        // Password label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.7;
        formPanel.add(new XLabelText(TextStyleRegistry.FIELD_LABEL, TextRegistry.PASSWORD_LABEL), gbc);
        
        // Password field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);

        // Password error
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        formPanel.add(passwordError, gbc);
        
        // Container
        JPanel formPanelContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formPanelContainer.setBackground(ColourRegistry.FORM_BACKGROUND.getColour());
        formPanelContainer.setPreferredSize(new Dimension(0, 250));
        formPanelContainer.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        formPanelContainer.add(formPanel);
        
        // * SOUTH -> BUTTONS & STATUS PANEL
        JPanel southPanel = new JPanel(new BorderLayout(0, 10));
        southPanel.setBackground(ColourRegistry.FORM_BACKGROUND.getColour());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(ColourRegistry.FORM_BACKGROUND.getColour());
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        southPanel.add(buttonPanel, BorderLayout.NORTH);
        
        // Status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        statusPanel.setBackground(ColourRegistry.FORM_BACKGROUND.getColour());
        statusPanel.add(statusLabel);
        southPanel.add(statusPanel, BorderLayout.SOUTH);

        // * MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(160, 120, 160, 120));
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(formPanelContainer, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        mainPanel.setBackground(ColourRegistry.APP_BACKGROUND_PALE.getColour());
        
        // * THIS
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        add(mainPanel, BorderLayout.CENTER);
        
        // * OPAQUE
		setOpaque(false);
		formPanel.setOpaque(false);
        
        // *** ATTACH
        _controller.attach(
                loginButton,
                cancelButton,
                statusLabel,
                emailField,
                emailError,
                passwordField,
                passwordError
        		);
    }
    
	@Override
	public Node getNode() {
		return _parent;
	}
}