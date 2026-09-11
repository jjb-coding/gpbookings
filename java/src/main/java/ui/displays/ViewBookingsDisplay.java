package ui.displays;

import javax.swing.*;
import java.awt.*;
import ui.component.components.XComboBoxMonth;
import ui.component.components.XComboBoxYear;
import ui.component.components.XScrollBookings;
import ui.container.IDisplay;
import ui.container.InjectableEnum;
import ui.container.Node;
import ui.controllers.ViewBookingsController;
import ui.injectables.LogInData;

/**
 * Frame for viewing all bookings made by the current user
 */
public class ViewBookingsDisplay extends JPanel
	implements IDisplay {
	// The serial version ID.
    private static final long serialVersionUID = 1L;
    
    // Fields
    Node _parent;
    
    public ViewBookingsDisplay(Node _parent) {
    	// Super
    	super(new BorderLayout(10, 10));
    	// Set parent
    	this._parent = _parent;
    	// Inject
    	ViewBookingsController _controller = (ViewBookingsController)inject(InjectableEnum.VIEW_BOOKINGS_CONTROLLER);
    	LogInData _logInData = (LogInData)inject(InjectableEnum.LOGIN_DATA);

        // Create buttons
        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");
        
        // Main panel with padding
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header panel with title and user info
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Your Appointment Bookings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JLabel userLabel = new JLabel("Patient: " + _logInData.getSecurityEmail());
        userLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        headerPanel.add(userLabel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Table in a scroll pane
        XScrollBookings scrollBookings = new XScrollBookings(_controller);
        scrollBookings.setSize(250, 300);
        add(scrollBookings, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
                
        // Bottom panel with buttons and status
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
        

        // Month & year select
        JPanel datePanel = new JPanel(new GridLayout(2, 2));
        JLabel monthLabel = new JLabel("Month:");
        XComboBoxMonth monthField = new XComboBoxMonth();
        JLabel yearLabel = new JLabel("Year:");
        XComboBoxYear yearField = new XComboBoxYear();
        datePanel.add(monthLabel);
        datePanel.add(monthField);
        datePanel.add(yearLabel);
        datePanel.add(yearField);
        add(datePanel, BorderLayout.SOUTH);
        
        // *** ATTACH
        _controller.attach(
        		monthField,
        		yearField,
        		scrollBookings,
        		refreshButton,
        		closeButton
        		);
    }
    
	@Override
	public Node getNode() {
		return _parent;
	}
}