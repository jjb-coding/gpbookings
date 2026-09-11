package ui.displays;

import javax.swing.*;

import java.awt.*;
import ui.component.components.XComboBoxDoctor;
import ui.component.components.XComboBoxTitle;
import ui.component.components.XLabelStatus;
import ui.component.components.XLabelText;
import ui.container.IDisplay;
import ui.container.InjectableEnum;
import ui.container.Node;
import ui.controllers.UpdatePersonalDetailsController;
import ui.registry.ColourRegistry;
import ui.registry.TextRegistry;
import ui.registry.TextStyleRegistry;
import util.UIUtils;

public class UpdatePersonalDetailsDisplay extends JPanel
	implements IDisplay {
	// The serial version ID.
	private static final long serialVersionUID = 1L;

	// Fields
	Node _parent;
	        
	public UpdatePersonalDetailsDisplay(Node _parent) {
    	// Super
    	super();
    	// Set parent
    	this._parent = _parent;
    	// Inject
    	UpdatePersonalDetailsController _controller = (UpdatePersonalDetailsController)inject(InjectableEnum.UPDATE_PERSONAL_DETAILS_CONTROLLER);

		// * COMPONENTS
        XComboBoxTitle titleCombo = new XComboBoxTitle();
        XLabelStatus titleError = new XLabelStatus(null, "width: 100px; text-align: right");
        
        JTextField firstNameField = new JTextField();
        XLabelStatus firstNameError = new XLabelStatus(null, "width: 100px; text-align: right");
        
        JTextField surnameField = new JTextField();
        XLabelStatus surnameError = new XLabelStatus(null, "width: 100px; text-align: right");
        
        JTextField contactPhoneNumberField = new JTextField();
        XLabelStatus contactPhoneNumberError = new XLabelStatus(null, "width: 100px; text-align: right");
        
        JTextField contactEmailField = new JTextField();
        XLabelStatus contactEmailError = new XLabelStatus(null, "width: 100px; text-align: right");
        
        JTextField street1Field = new JTextField();
        XLabelStatus street1Error = new XLabelStatus(null, "width: 100px; text-align: right");
        
        JTextField street2Field = new JTextField();
        XLabelStatus street2Error = new XLabelStatus(null, "width: 100px; text-align: right");
        
        JTextField cityField = new JTextField();
        XLabelStatus cityError = new XLabelStatus(null, "width: 100px; text-align: right");

        JTextField countyField = new JTextField();
        XLabelStatus countyError = new XLabelStatus(null, "width: 100px; text-align: right");
        
        JTextField postCodeField = new JTextField();
        XLabelStatus postCodeError = new XLabelStatus(null, "width: 100px; text-align: right");
        
        XComboBoxDoctor doctorCombo = new XComboBoxDoctor();
        XLabelStatus doctorError = new XLabelStatus(null, "width: 100px; text-align: right");

        XLabelStatus statusLabel = new XLabelStatus(null);
        
        JButton confirmButton = UIUtils.createStyledButton(TextRegistry.BUTTON_SUBMIT.getString());
        JButton backButton = UIUtils.createStyledButton(TextRegistry.BUTTON_BACK.getString());
        
        // * NORTH -> INSTRUCTION
        // Create instruction label
		XLabelText instructionLabel = new XLabelText(TextStyleRegistry.INSTRUCTION, TextRegistry.NEW_INSTRUCTION);
        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(ColourRegistry.HEADER_BACKGROUND.getColour());
        northPanel.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createEtchedBorder(),
        	    BorderFactory.createEmptyBorder(6, 10, 6, 10)
        	));
        JPanel instructionContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        instructionContainer.add(instructionLabel);
        instructionContainer.setOpaque(false);
        northPanel.add(instructionContainer, BorderLayout.CENTER);
        
        // * CENTRE -> FORM PANEL
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        
        // Title label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.55;
        gbc.weighty = 1.0;
        formPanel.add(new XLabelText(TextStyleRegistry.FIELD_LABEL, TextRegistry.TITLE_LABEL), gbc);
        
        // Title field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(titleCombo, gbc);
        
        // Title error
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        formPanel.add(titleError, gbc);
        

        // First name label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.55;
        gbc.weighty = 1.0;
        formPanel.add(new XLabelText(TextStyleRegistry.FIELD_LABEL, TextRegistry.FIRST_NAME_LABEL), gbc);
        
        // First name field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(firstNameField, gbc);

        // First name error
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        formPanel.add(firstNameError, gbc);
        

        // Surname label
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.55;
        gbc.weighty = 1.0;
        formPanel.add(new XLabelText(TextStyleRegistry.FIELD_LABEL, TextRegistry.SURNAME_LABEL), gbc);
        
        // Surname field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(surnameField, gbc);

        // Surname error
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        formPanel.add(surnameError, gbc);
        

        // Contact phone number label
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 0.55;
        gbc.weighty = 1.0;
        formPanel.add(new XLabelText(TextStyleRegistry.FIELD_LABEL, TextRegistry.CONTACT_PHONE_NUMBER_LABEL), gbc);
        
        // Contact phone number field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(contactPhoneNumberField, gbc);

        // Contact phone number error
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        formPanel.add(contactPhoneNumberError, gbc);
        
        
        // Contact email label
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 0.55;
        gbc.weighty = 1.0;
        formPanel.add(new XLabelText(TextStyleRegistry.FIELD_LABEL, TextRegistry.EMAIL_LABEL), gbc);
        
        // Contact email field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(contactEmailField, gbc);

        // Contact email error
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        formPanel.add(contactEmailError, gbc);
        

        // Doctor label
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.55;
        gbc.weighty = 1.0;
        formPanel.add(new XLabelText(TextStyleRegistry.FIELD_LABEL, TextRegistry.DOCTOR_LABEL), gbc);
        
        // Doctor field
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(doctorCombo, gbc);

        // Doctor error
        gbc.gridy = 11;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        formPanel.add(doctorError, gbc);
        

        // Street 1 label
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 0.55;
        gbc.weighty = 1.0;
        formPanel.add(new XLabelText(TextStyleRegistry.FIELD_LABEL, TextRegistry.STREET_1_LABEL), gbc);
        
        // Street 1 field
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(street1Field, gbc);

        // Street 1 error
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        formPanel.add(street1Error, gbc);
        

        // Street 2 label
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.55;
        gbc.weighty = 1.0;
        formPanel.add(new XLabelText(TextStyleRegistry.FIELD_LABEL, TextRegistry.STREET_2_LABEL), gbc);
        
        // Street 2 field
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(street2Field, gbc);

        // Street 2 error
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        formPanel.add(street2Error, gbc);
        

        // City label
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.55;
        gbc.weighty = 1.0;
        formPanel.add(new XLabelText(TextStyleRegistry.FIELD_LABEL, TextRegistry.CITY_LABEL), gbc);
        
        // City field
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(cityField, gbc);

        // City error
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        formPanel.add(cityError, gbc);
        

        // County label
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.55;
        gbc.weighty = 1.0;
        formPanel.add(new XLabelText(TextStyleRegistry.FIELD_LABEL, TextRegistry.COUNTY_LABEL), gbc);
        
        // County field
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(countyField, gbc);

        // County error
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        formPanel.add(countyError, gbc);
        

        // Post code label
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.55;
        gbc.weighty = 1.0;
        formPanel.add(new XLabelText(TextStyleRegistry.FIELD_LABEL, TextRegistry.POSTCODE_LABEL), gbc);
        
        // Post code field
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(postCodeField, gbc);

        // Post code error
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        formPanel.add(postCodeError, gbc);
                   
        add(formPanel, BorderLayout.CENTER);

        // Container
        formPanel.setOpaque(false);
        JPanel formPanelContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formPanelContainer.setBackground(ColourRegistry.FORM_BACKGROUND.getColour());
        formPanelContainer.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        formPanelContainer.add(formPanel);
        
        // * SOUTH -> BUTTONS & STATUS PANEL
        JPanel southPanel = new JPanel(new BorderLayout(0, 10));
        southPanel.setBackground(ColourRegistry.FORM_BACKGROUND.getColour());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(ColourRegistry.FORM_BACKGROUND.getColour());

        buttonPanel.add(confirmButton);
        buttonPanel.add(backButton);
        southPanel.add(buttonPanel, BorderLayout.NORTH);
        
        // Status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        statusPanel.setBackground(ColourRegistry.FORM_BACKGROUND.getColour());
        statusPanel.add(statusLabel);
        southPanel.add(statusPanel, BorderLayout.SOUTH);
        
        // * MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 60, 15, 60));
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(formPanelContainer, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        mainPanel.setOpaque(false);
        
        // * THIS
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setBackground(ColourRegistry.APP_BACKGROUND_PALE.getColour());
        add(mainPanel, BorderLayout.CENTER);
        
        
        // *** ATTACH
        _controller.attach(
        		statusLabel,
        		titleCombo,
        		titleError,
        		firstNameField,
        		firstNameError,
        		surnameField,
        		surnameError,
        		contactPhoneNumberField,
        		contactPhoneNumberError,
        		contactEmailField,
        		contactEmailError,
        		street1Field,
        		street1Error,
        		street2Field,
        		street2Error,
        		cityField,
        		cityError,
        		countyField,
        		countyError,
        		postCodeField,
        		postCodeError,
        		doctorCombo,
        		doctorError,
        		confirmButton,
        		backButton
        		);
    }

	@Override
	public Node getNode() {
		return _parent;
	}
}