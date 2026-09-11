package ui.displays;

import javax.swing.*;

import ui.component.components.XLabelTextParametrised;
import ui.container.IDisplay;
import ui.container.InjectableEnum;
import ui.container.Node;
import ui.controllers.DashboardController;
import ui.registry.ColourRegistry;
import ui.registry.TextRegistry;
import ui.registry.TextStyleRegistry;

import java.awt.*;

public class DashboardDisplay extends JPanel
	implements IDisplay {
	// The serial version ID.
	private static final long serialVersionUID = 1L;
	
	// Fields
    Node _parent;
    DashboardController _dashboardController;
    
    public DashboardDisplay(Node _parent) {
    	// Call super
    	super(new BorderLayout());
    	// Set parent
    	this._parent = _parent;
    	// Injections
    	_dashboardController = (DashboardController)inject(InjectableEnum.DASHBOARD_CONTROLLER);
    	
    	// Apply look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    	// *** UI
        // This
        setBackground(ColourRegistry.APP_BACKGROUND.getColour());
        setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        // * WELCOME PANEL
        // Create welcome panel at the top
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(ColourRegistry.HEADER_BACKGROUND.getColour());
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        XLabelTextParametrised welcomeLabel = new XLabelTextParametrised(TextRegistry.DASHBOARD_WELCOME_MESSAGE);
        welcomeLabel.setStyle(TextStyleRegistry.HEADER);
        welcomePanel.add(welcomeLabel);
        welcomePanel.setSize(186, 30);

        // Containerise & add
        JPanel welcomeContainerPanel = new JPanel();
        welcomeContainerPanel.add(welcomePanel);
        welcomeContainerPanel.setBackground(ColourRegistry.APP_BACKGROUND_PALE.getColour());
        welcomeContainerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        add(welcomeContainerPanel, BorderLayout.NORTH);

        // *** TABBED PANE
        // Create tabbed pane for main content
        JPanel iconsPanel = new JPanel();
		iconsPanel.setBackground(ColourRegistry.APP_BACKGROUND_PALE.getColour());
        iconsPanel.setLayout(new GridLayout(2, 2, 80, 80));
        iconsPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Create buttons
        JButton viewBookingsButton = createDashboardButton(TextRegistry.DASHBOARD_BUTTON_VIEW_BOOKINGS);
        JButton newBookingButton = createDashboardButton(TextRegistry.DASHBOARD_BUTTON_NEW_BOOKING);
        JButton patientDetailsButton = createDashboardButton(TextRegistry.DASHBOARD_BUTTON_DETAILS);
        JButton messagesButton = createDashboardButton(TextRegistry.DASHBOARD_BUTTON_MESSAGES);

        // Add buttons to the button panel
        iconsPanel.add(viewBookingsButton);
        iconsPanel.add(newBookingButton);
        iconsPanel.add(patientDetailsButton);
        iconsPanel.add(messagesButton);

        // Containerise & add
		JPanel iconsContainerPanel = new JPanel();
		iconsContainerPanel.setLayout(new BoxLayout(iconsContainerPanel, BoxLayout.Y_AXIS));
		iconsPanel.setPreferredSize(new Dimension(580,580));
		iconsPanel.setMaximumSize(new Dimension(580,580));
		iconsContainerPanel.add(iconsPanel);
		iconsContainerPanel.add(Box.createVerticalGlue());
        
        // Add the tabbed pane to this
        add(iconsContainerPanel, BorderLayout.CENTER);
        
        // *** ATTACH
        _dashboardController.attach(
        		welcomeLabel,
        		newBookingButton,
        		patientDetailsButton,
        		viewBookingsButton,
        		messagesButton
        		);
        

        revalidate();
        repaint();
    }
    
    public JButton createDashboardButton(TextRegistry text) {
    	JButton button = new JButton(text.getString());
    	button.setFont(TextStyleRegistry.DASHBOARD_ICONS.getFont());
    	button.setForeground(TextStyleRegistry.DASHBOARD_ICONS.getColour());
    	button.setBackground(ColourRegistry.DASHBOARD_ICONS_BACKGROUND.getColour());
    	button.setText("<html><center>" + text.getString() + "</center></html>");
    	button.setSize(80, 80);
    	return button;
    }

	@Override
	public Node getNode() {
		return _parent;
	}
}