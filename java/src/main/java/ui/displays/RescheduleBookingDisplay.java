package ui.displays;

import javax.swing.*;

import ui.component.components.*;
import ui.container.IDisplay;
import ui.container.InjectableEnum;
import ui.container.Node;
import ui.controllers.RescheduleBookingController;

import java.awt.*;
/**
 * Panel for creating a new booking appointment with a doctor.
 */
public class RescheduleBookingDisplay extends JPanel
	implements IDisplay {
    private static final long serialVersionUID = 1L;
    
    // Parent
    Node _parent;
    // Injections
    RescheduleBookingController _controller;
    
    // UI Components
    private XComboBoxDate dateCombo;
    private XComboBoxBookingSlot slotCombo;
    private JButton submitButton;
    private JButton cancelButton;
    private XLabelStatus statusLabel;
        
    public RescheduleBookingDisplay(Node _parent) {
    	// Super
    	super(new BorderLayout(10, 10));
    	
    	// Parent
    	this._parent = _parent;
    	// Singletons
    	
    	// Injections
    	_controller = (RescheduleBookingController)inject(InjectableEnum.RESCHEDULE_BOOKING_CONTROLLER);
    	
        createComponents();
        setupLayout();

        // *** ATTACH
        /*
        _controller.attach(
        		statusLabel,
        		submitButton,
        		cancelButton,
        		dateCombo,
        		new XLabelStatus(null),
        		slotCombo,
        		new XLabelStatus(null)
        		);*/
    }
    
    /**
     * Creates and initializes all UI components
     */
    private void createComponents() {        
        // Date dropdown (next 7 days)
        dateCombo = new XComboBoxDate();
        
        // Time slots
        slotCombo = new XComboBoxBookingSlot();
        
        // Reason text area
        /*
        reasonTextArea = new JTextArea(5, 20);
        reasonTextArea.setLineWrap(true);
        reasonTextArea.setWrapStyleWord(true);
        */
        
        // Buttons
        submitButton = new JButton("Submit Booking");
        cancelButton = new JButton("Cancel");
        
        // Status label
        statusLabel = new XLabelStatus(null);
        statusLabel.setForeground(Color.RED);
    }
    
    /**
     * Arranges components using layout managers
     */
    private void setupLayout() {
        // Main panel with padding
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Doctor selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Select Doctor:"), gbc);
        
        /*
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        formPanel.add(doctorCombo, gbc);
        */
        
        // Date selection
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Select Date:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        formPanel.add(dateCombo, gbc);
        
        // Time selection
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Select Time:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        formPanel.add(slotCombo, gbc);
        
        // Reason for appointment
        /*
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        formPanel.add(new JLabel("Reason:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        JScrollPane scrollPane = new JScrollPane(reasonTextArea);
        formPanel.add(scrollPane, gbc);
        */
        
        // Add form panel to main panel
        add(formPanel, BorderLayout.CENTER);
        
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
        
        add(southPanel, BorderLayout.SOUTH);
    }
    
	@Override
	public Node getNode() {
		return _parent;
	}
}