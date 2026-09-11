package ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import form.field.fields.EmailAddress;
import form.field.fields.Password;
import form.field.fields.PasswordCreation;
import form.form.Form;
import form.multifield.multifields.Matches;
import status.Status;
import ui.component.components.XLabelStatus;
import ui.container.AppContainer;
import ui.container.Base;
import ui.container.InjectableEnum;
import ui.container.InjectableObject;

public class NewFirstController extends InjectableObject {
	// Injections
	Form form;
	
	// Attachments
	public EmailAddress email;
	public PasswordCreation password;
	
	/**
	 * Constructs a controller.
	 * @param _appContainer		The appContainer.
	 * @param parent			The parent of the controller.
	 */
	public NewFirstController(AppContainer _appContainer, Base parent) {
		super(_appContainer, parent);
	}
	
	/**
	 * Post-constructor initialisation for injection.
	 */	
	public void initialise() {
		// Inject
		form = (Form)inject(InjectableEnum.NEW_FIRST_FORM);
	}
	
	public void attach(
    		XLabelStatus statusLabel,
    		JTextField emailField,
    		XLabelStatus emailError,
    		JPasswordField passwordField,
    		XLabelStatus passwordError,
    		JPasswordField confirmPasswordField,
    		XLabelStatus confirmPasswordError,
    		JButton nextButton,
    		JButton cancelButton
			) {
		email = form.attach(new EmailAddress(emailField, emailError));
		password = form.attach(new PasswordCreation(passwordField, passwordError));
		Password password2 = form.attach(new Password(confirmPasswordField, confirmPasswordError));
		form.attach(new Matches(password, passwordError, password2, confirmPasswordError));

		form.attachFormErrorReflection(statusLabel);
		
        // Add action listener to register button
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (!form.execute())
            		return;
        		setStatus(Status.NEW_SECOND);
            }
        });
        
        // Add action listener to cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        		setStatus(Status.WELCOME);
            }
        });
	}
}
