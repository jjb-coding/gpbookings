package ui.displays;

import javax.swing.*;
import java.awt.*;
import ui.container.IDisplay;
import ui.container.InjectableEnum;
import ui.container.Node;
import ui.controllers.ViewAttendedBookingController;

public class ViewAttendedBookingDisplay extends JPanel
	implements IDisplay {
	// The serial version ID.
    private static final long serialVersionUID = 1L;
    
    // Fields
    Node _parent;

    public ViewAttendedBookingDisplay(Node _parent) {
    	// Super
    	super(new BorderLayout());
    	// Set parent
    	this._parent = _parent;
    	// Inject
    	ViewAttendedBookingController _controller = (ViewAttendedBookingController)inject(InjectableEnum.VIEW_ATTENDED_BOOKING_CONTROLLER);
    	
    	// *** UI
        // Create main panel with padding
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create header
        JLabel headerLabel = new JLabel("Visit Details", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(headerLabel, BorderLayout.NORTH);

        // Booking timestamp
        JLabel bookingTimestampLabel = new JLabel("Booking:");
        JLabel bookingTimestampValue = new JLabel();
        
        // Modified timestamp
        JLabel modifiedTimestampLabel = new JLabel("Last Modified:");
        JLabel modifiedTimestampValue = new JLabel();
        
        // Doctor
        JLabel doctorLabel = new JLabel("Doctor: ");
        JLabel doctorValue = new JLabel();
        
        // Add
        add(bookingTimestampLabel, BorderLayout.CENTER);
        add(bookingTimestampValue, BorderLayout.CENTER);
        add(modifiedTimestampLabel, BorderLayout.CENTER);
        add(modifiedTimestampValue, BorderLayout.CENTER);
        add(doctorLabel, BorderLayout.CENTER);
        add(doctorValue, BorderLayout.CENTER);
        
        // Create text area to display summary
        JTextArea summaryText = new JTextArea();
        summaryText.setEditable(false);
        summaryText.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane summaryScroll = new JScrollPane(summaryText);
        add(summaryScroll, BorderLayout.SOUTH);
        
        // Create text area to display prescriptions
        JTextArea prescriptionsText = new JTextArea();
        prescriptionsText.setEditable(false);
        prescriptionsText.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane prescriptionsScroll = new JScrollPane(summaryText);
        add(prescriptionsScroll, BorderLayout.SOUTH);

        // *** ATTACH
        _controller.attach(
        		bookingTimestampValue,
        		modifiedTimestampValue,
        		doctorValue,
        		summaryText,
        		prescriptionsText
        		);
    }

	@Override
	public Node getNode() {
		return _parent;
	}
}