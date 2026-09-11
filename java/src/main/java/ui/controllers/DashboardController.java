package ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import ui.container.AppContainer;
import ui.container.Base;
import ui.container.InjectableEnum;
import ui.container.InjectableObject;
import ui.injectables.LogInData;
import status.Status;

public class DashboardController extends InjectableObject {
	// Injections
	LogInData _logInData;
	
	/**
	 * Constructs a controller.
	 * @param _appContainer		The appContainer.
	 * @param parent			The parent of the controller.
	 */
	public DashboardController(AppContainer _appContainer, Base parent) {
		super(_appContainer, parent);
	}
	
	/**
	 * Post-constructor initialisation for injection.
	 */
	public void initialise() {
    	_logInData = (LogInData)inject(InjectableEnum.LOGIN_DATA);
	}
	
	public void attach(
			JLabel welcomeLabel,
    		JButton newBookingButton,
    		JButton patientDetailsButton,
    		JButton viewBookingsButton,
    		JButton logoutButton
    		) {
		welcomeLabel.setText("Welcome, " + _logInData.getSecurityEmail());

        newBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStatus(Status.MAKE_BOOKING);
            }
        });

        patientDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStatus(Status.UPDATE_DETAILS);
            }
        });
        
        viewBookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStatus(Status.VIEW_BOOKINGS);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStatus(Status.WELCOME);
            }
        });
        
	}
}
