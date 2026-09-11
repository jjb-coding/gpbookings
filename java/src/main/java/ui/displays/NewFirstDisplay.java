
package ui.displays;
import javax.swing.*;

import ui.component.components.XLabelStatus;
import ui.component.components.XLabelStatusSpecialised;
import ui.component.components.XLabelText;
import ui.container.IDisplay;
import ui.container.InjectableEnum;
import ui.container.Node;
import ui.controllers.NewFirstController;
import ui.registry.ColourRegistry;
import ui.registry.TextRegistry;
import ui.registry.TextStyleRegistry;
import util.UIUtils;

import java.awt.*;

public class NewFirstDisplay extends JPanel
	implements IDisplay {
	// The serial version ID.
	private static final long serialVersionUID = 1L;
	// Fields
	Node _parent;
	
    public NewFirstDisplay(Node _parent) {
    	// Super
    	super(new BorderLayout());
    	// Set parent
    	this._parent = _parent;
    	// Inject
    	NewFirstController _controller = (NewFirstController)inject(InjectableEnum.NEW_FIRST_CONTROLLER);
    	
        // * COMPONENTS
        JTextField emailField = new JTextField(20);
        XLabelStatus emailError = new XLabelStatus(null);
        
        JPasswordField passwordField = new JPasswordField(20);
        XLabelStatus passwordError = new XLabelStatus(null, "width: 140px; text-align: right");
        
        JPasswordField confirmPasswordField = new JPasswordField(20);
        XLabelStatus confirmPasswordError = new XLabelStatus(null);
        
        XLabelStatusSpecialised statusLabel = new XLabelStatusSpecialised(TextRegistry.SPECIALISE_PASSWORDS);
        
        JButton nextButton = UIUtils.createStyledButton(TextRegistry.BUTTON_NEXT.getString());
        JButton cancelButton = UIUtils.createStyledButton(TextRegistry.BUTTON_CANCEL.getString());
        
        // * NORTH -> INSTRUCTION
        // Create instruction label
        XLabelText instructionLabel = new XLabelText(TextStyleRegistry.INSTRUCTION, TextRegistry.NEW_INSTRUCTION);
        
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
        
        // Confirm password label
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.7;
        formPanel.add(new XLabelText(TextStyleRegistry.FIELD_LABEL, TextRegistry.CONFIRM_PASSWORD_LABEL), gbc);
        
        // Confirm password field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        formPanel.add(confirmPasswordField, gbc);

        // Confirm password error
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        formPanel.add(confirmPasswordError, gbc);

        // Container
        formPanel.setOpaque(false);
        JPanel formPanelContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formPanelContainer.setBackground(ColourRegistry.FORM_BACKGROUND.getColour());
        formPanelContainer.setPreferredSize(new Dimension(0, 250));
        formPanelContainer.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        formPanelContainer.add(formPanel);

        // * SOUTH -> BUTTONS & STATUS PANEL
        JPanel southPanel = new JPanel(new BorderLayout(0, 10));
        southPanel.setBackground(ColourRegistry.FORM_BACKGROUND.getColour());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(ColourRegistry.FORM_BACKGROUND.getColour());

        buttonPanel.add(nextButton);
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(160, 100, 160, 100));
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(formPanelContainer, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        mainPanel.setBackground(ColourRegistry.APP_BACKGROUND_PALE.getColour());
        
        // * THIS
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        add(mainPanel, BorderLayout.CENTER);

        
        // *** ATTACH
        _controller.attach(
        		statusLabel,
        		emailField,
        		emailError,
        		passwordField,
        		passwordError,
        		confirmPasswordField,
        		confirmPasswordError,
        		nextButton,
        		cancelButton
        		);
    }
    
	@Override
	public Node getNode() {
		return _parent;
	}
}
