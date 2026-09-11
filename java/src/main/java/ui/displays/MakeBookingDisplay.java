package ui.displays;

import javax.swing.*;

import ui.component.components.*;
import ui.container.IDisplay;
import ui.container.InjectableEnum;
import ui.container.Node;
import ui.controllers.MakeBookingController;
import ui.registry.ColourRegistry;
import ui.registry.TextRegistry;
import ui.registry.TextStyleRegistry;

import java.awt.*;
/**
 * Panel for creating a new booking appointment with a doctor.
 */
public class MakeBookingDisplay extends JPanel
	implements IDisplay {
	// The serial version ID.
    private static final long serialVersionUID = 1L;
    
    // Parent
    Node _parent;
    // Injections
    MakeBookingController _controller;
            
    public MakeBookingDisplay(Node _parent) {
    	// Super
    	super(new BorderLayout(10, 10));
    	// Parent
    	this._parent = _parent;
    	// Injections
    	_controller = (MakeBookingController)inject(InjectableEnum.MAKE_BOOKING_CONTROLLER);

    	// *** UI
    	// * COMPONENTS
        // Date dropdown
        XComboBoxDate dateCombo = new XComboBoxDate();
        XLabelStatus dateError = new XLabelStatus(null);
        
        // Time slots
        XComboBoxBookingSlot slotCombo = new XComboBoxBookingSlot();
        XLabelStatus slotError = new XLabelStatus(null);
                
        // Buttons
        JButton submitButton = new JButton(TextRegistry.BUTTON_SUBMIT.getString());
        JButton cancelButton = new JButton(TextRegistry.BUTTON_CANCEL.getString());
        
        // Status label
        XLabelStatus statusLabel = new XLabelStatus(null);
        
        // * NORTH -> INSTRUCTION
        // Create instruction label
        XLabelText instructionLabel = new XLabelText(TextStyleRegistry.INSTRUCTION, TextRegistry.MAKE_BOOKING_INSTRUCTION);
        
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
        
        // * CENTER -> FORM
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
                
        // Date selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.7;
        formPanel.add(new XLabelText(TextRegistry.DATE_LABEL), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        formPanel.add(dateCombo, gbc);
        
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weighty = 0.7;
        formPanel.add(dateError, gbc);
        
        // Time selection
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        formPanel.add(new XLabelText(TextRegistry.SLOT_LABEL), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        formPanel.add(slotCombo, gbc);
        
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.7;
        formPanel.add(slotError, gbc);
        
        // Container
        JPanel formPanelContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formPanelContainer.setPreferredSize(new Dimension(0, 250));
        formPanelContainer.setBorder(BorderFactory.createEmptyBorder(40, 10, 40, 10));
        formPanelContainer.add(formPanel);
        
        // * SOUTH -> BUTTONS & STATUS
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        
        // Status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(statusLabel);
        
        // South panel with buttons and status
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.EAST);
        southPanel.add(statusPanel, BorderLayout.WEST);
        
        // * MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(130, 90, 130, 90));
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(formPanelContainer, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        mainPanel.setBackground(ColourRegistry.APP_BACKGROUND_PALE.getColour());
        
        // * THIS
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        add(mainPanel, BorderLayout.CENTER);

        // * OPAQUE        
		setOpaque(false);
        
        // *** ATTACH
        _controller.attach(
        		statusLabel,
        		submitButton,
        		cancelButton,
        		dateCombo,
        		dateError,
        		slotCombo,
        		slotError
        		);
    }

	@Override
	public Node getNode() {
		return _parent;
	}
}