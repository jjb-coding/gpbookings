package ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import status.Status;
import ui.container.AppContainer;
import ui.container.Base;
import ui.container.InjectableObject;

public class WelcomeController extends InjectableObject {
	
	// *** CONSTRUCTORS & INITIALISERS
	public WelcomeController(AppContainer _appContainer, Base parent) {
		super(_appContainer, parent);
	}
	
	// *** PUBLIC METHODS
	public void attach(
			JButton logInButton,
			JButton registerButton) {
		
		
		// Add listeners
		logInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setStatus(Status.LOG_IN);				
			}
		});
		
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setStatus(Status.NEW_FIRST);				
			}
		});
	}
}
