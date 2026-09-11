package ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import status.Status;
import ui.component.components.XLabelTextParametrised;
import ui.container.AppContainer;
import ui.container.Base;
import ui.container.InjectableEnum;
import ui.container.InjectableObject;
import ui.injectables.LogInData;

public class ElementAccountBarController extends InjectableObject {
	// Injections
	LogInData logInData;
	
	/**
	 * Constructs a controller.
	 * @param _appContainer		The appContainer.
	 * @param parent			The parent of the controller.
	 */
	public ElementAccountBarController(AppContainer _appContainer, Base parent) {
		super(_appContainer, parent);
	}
	
	public void initialise() {
		logInData = (LogInData)inject(InjectableEnum.LOGIN_DATA);
	}
	
	public void attach(
			JButton logOutButton,
			JButton backButton,
			XLabelTextParametrised bannerText) {
		// Set email address
		bannerText.setParameter(logInData.getSecurityEmail());
		
		// Set listener on log out button
		logOutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setStatus(Status.WELCOME);
			}});
		
		// Set listener on back button
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendBack();
			}});
	}
}
