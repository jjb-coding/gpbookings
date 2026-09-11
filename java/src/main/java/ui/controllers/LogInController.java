package ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import exceptions.DatabaseException;
import exceptions.NoConnectionException;
import form.field.fields.EmailAddress;
import form.field.fields.Password;
import form.form.Form;
import services.authService.AuthCredentials;
import services.authService.AuthService;
import services.authService.AuthServiceSingleton;
import services.securityService.SecurityService;
import services.securityService.SecurityServiceSingleton;
import status.Status;
import status.StatusTransformer;
import ui.component.components.XLabelStatus;
import ui.container.AppContainer;
import ui.container.Base;
import ui.container.InjectableEnum;
import ui.container.InjectableObject;
import ui.container.ViewBag;
import ui.injectables.LogInData;
import ui.injectables.ProgressBarPublisher;

public class LogInController extends InjectableObject {
	// Singletons
	SecurityService _securityService;
	AuthService _authService;
	// Injections
	Form form;
	ProgressBarPublisher pub;
	AppContainer _appContainer;
	
	/**
	 * Constructs a controller.
	 * @param _appContainer		The appContainer.
	 * @param parent			The parent of the controller.
	 */
	public LogInController(AppContainer _appContainer, Base parent) {
		// Super
		super(_appContainer, parent);
		
		// Singletons
        _securityService = SecurityServiceSingleton.INSTANCE.get();
        _authService = AuthServiceSingleton.INSTANCE.get();
        this._appContainer = _appContainer;
        
	}
	
	/**
	 * Post-constructor initialisation for injection.
	 */
	public void initialise() {
        // Injections
        form = (Form)inject(InjectableEnum.LOGIN_FORM);
        pub = (ProgressBarPublisher)inject(InjectableEnum.PROGRESSBAR_PUBLISHER);
	}
	
	public void attach(
			JButton loginButton,
			JButton cancelButton,
			XLabelStatus statusLabel,
    		JTextField emailField,
    		XLabelStatus emailError,
    		JPasswordField passwordField,
    		XLabelStatus passwordError) {
        
		// Configure form
		form.attachFormErrorReflection(statusLabel);
		EmailAddress email = form.attach(new EmailAddress(emailField, emailError));
		Password password = form.attach(new Password(passwordField, passwordError));

		// Listeners
		loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton.setEnabled(false);
                pub.send(0.0f);

                // Submit Form
                if (!form.execute()) {
                    loginButton.setEnabled(true);
                    return;
                }
                
                // Ready
                pub.send(0.2f);
                statusLabel.setStatus(Status.LOG_IN_READY);
                                
                // Execute API
                api.output.LogInOutput api = null;
                try {
                	api = new api.input.LogIn(email.postValue, password.postValue).execute();
                }
                catch (DatabaseException | NoConnectionException e1) {
                	handleError(e1);
                	return;
                }
                if (isBadStatus(api.status()))
                	return;
                
                // Clean the byte[] password
                _securityService.clean(password.postValue);
                
                // Set status label
                statusLabel.setStatus(StatusTransformer.specialiseSuccess(api.status(), Status.LOG_IN_SUCCESS));
                
                // Action - if login was successful or not
                if (Status.toAction(api.status()) != Status.SUCCESS) {
                    loginButton.setEnabled(true);
                    return;
            	}
                
                // Clean the field password
                password.clean();
                
                // Update progress bar
                pub.send(0.6f); 
                
                // Package LogIn injectable
                ViewBag logInViewBag = _appContainer.createViewBag(InjectableEnum.LOGIN_DATA);
                ((LogInData)logInViewBag.instance()).configure(email.postValue, api.accountID(), api.sessionToken());
                                
                // Submit authCredentials object
                _authService.logIn(new AuthCredentials(api.accountID(), api.sessionToken()));

                // Concentrate progress updates from setStatus() into 0.7f-1.0f
                pub.push(0.7f);
                
                // Transition
                setStatusAfterWait(100, Status.DASHBOARD, logInViewBag);
                
                // Clear progress bar
                pub.reset();
            }
		});
		cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	setStatus(Status.WELCOME);
            }
		});
	}
}
