package ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import exceptions.DatabaseException;
import exceptions.NoConnectionException;
import form.field.fields.GeneralComboBox;
import form.field.fields.GeneralText;
import form.field.fields.PersonalName;
import form.field.fields.PhoneNumber;
import form.field.fields.PostCode;
import form.form.Form;
import status.Status;
import status.StatusTransformer;
import ui.component.components.XComboBoxDoctor;
import ui.component.components.XComboBoxTitle;
import ui.component.components.XLabelStatus;
import ui.container.AppContainer;
import ui.container.Base;
import ui.container.InjectableEnum;
import ui.container.InjectableObject;

public class NewSecondController extends InjectableObject {
	NewFirstController newFirstController;
	Form form;
	
	/**
	 * Constructs a controller.
	 * @param _appContainer		The appContainer.
	 * @param parent			The parent of the controller.
	 */
	public NewSecondController(AppContainer _appContainer, Base parent) {
		super(_appContainer, parent);
	}

	/**
	 * Post-constructor initialisation for injection.
	 */
	public void initialise() {
		// Inject
		newFirstController = (NewFirstController)inject(InjectableEnum.NEW_FIRST_CONTROLLER);
		form = (Form)inject(InjectableEnum.NEW_SECOND_FORM);
	}
	
	public void attach(
			XLabelStatus statusLabel,
    		XComboBoxTitle titleCombo,
    		XLabelStatus titleError,
    		JTextField firstNameField,
    		XLabelStatus firstNameError,
    		JTextField surnameField,
    		XLabelStatus surnameError,
    		JTextField contactPhoneNumberField,
    		XLabelStatus contactPhoneNumberError,
    		JTextField street1Field,
    		XLabelStatus street1Error,
    		JTextField street2Field,
    		XLabelStatus street2Error,
    		JTextField cityField,
    		XLabelStatus cityError,
    		JTextField countyField,
    		XLabelStatus countyError,
    		JTextField postCodeField,
    		XLabelStatus postCodeError,
    		XComboBoxDoctor doctorCombo,
    		XLabelStatus doctorError,
    		JButton confirmButton,
    		JButton backButton
    		) {

		// Form
		form.attachFormErrorReflection(statusLabel);
		GeneralComboBox<String> doctor = form.attach(new GeneralComboBox<String>(doctorCombo, doctorError));
		GeneralComboBox<String> title = form.attach(new GeneralComboBox<String>(titleCombo, titleError));
		PersonalName firstName = form.attach(new PersonalName(firstNameField, firstNameError));
		PersonalName surname = form.attach(new PersonalName(surnameField, surnameError));
		PhoneNumber contactPhoneNumber = form.attach(new PhoneNumber(contactPhoneNumberField, contactPhoneNumberError));
		GeneralText street1 = form.attach(new GeneralText(street1Field, street1Error));
		GeneralText street2 = form.attach(new GeneralText(street2Field, street2Error));
		GeneralText city = form.attach(new GeneralText(cityField, cityError));
		GeneralText county = form.attach(new GeneralText(countyField, countyError));
		PostCode postCode = form.attach(new PostCode(postCodeField, postCodeError));
		
        // Add action listener to confirm button
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	confirmButton.setEnabled(false);
            	
            	// Execute form
            	if (!form.execute()) {
            		confirmButton.setEnabled(true);
            		return;
            	}

                // Execute API
            	api.output.NewOutput api = null;
                try {
                	api = new api.input.New(
                			newFirstController.email.postValue,
                			newFirstController.password.postValue,
                			doctor.postValue,
                			firstName.postValue,
                			surname.postValue,
                			title.postValue,
                			contactPhoneNumber.postValue,
                			street1.postValue,
                			street2.postValue,
                			city.postValue,
                			county.postValue,
                			postCode.postValue).execute();
                }
                catch (DatabaseException | NoConnectionException e1) {
                	handleError(e1);
                	return;
                }
                if (isBadStatus(api.status()))
                	return;
            	
            	// Set status
            	statusLabel.setStatus(StatusTransformer.specialiseSuccess(api.status(), Status.NEW_SUCCESS));
            	
            	// Action - if creation was successful or not
            	if (Status.toAction(api.status()) != Status.SUCCESS) {
            		confirmButton.setEnabled(true);
            		return;            		
            	}
            	
            	// Timered transition
        		setStatusAfterWait(800, Status.LOG_IN);
            }
        });

        // Add action listener to back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	setStatus(Status.NEW_FIRST);
            }
        });
        
	}
}
