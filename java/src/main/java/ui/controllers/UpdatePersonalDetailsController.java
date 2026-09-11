package ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import exceptions.DatabaseException;
import exceptions.NoConnectionException;
import form.field.fields.EmailAddress;
import form.field.fields.GeneralComboBox;
import form.field.fields.GeneralText;
import form.field.fields.PhoneNumber;
import form.field.fields.PostCode;
import form.form.Form;
import services.practiceProvider.PracticeProvider;
import services.practiceProvider.PracticeProviderSingleton;
import status.Status;
import status.StatusTransformer;
import ui.component.components.XComboBoxDoctor;
import ui.component.components.XComboBoxTitle;
import ui.component.components.XLabelStatus;
import ui.container.AppContainer;
import ui.container.Base;
import ui.container.InjectableEnum;
import ui.container.InjectableObject;

public class UpdatePersonalDetailsController extends InjectableObject {
	// Singletons
	PracticeProvider _practiceProvider;
	// Injections
	Form form;
	
	/**
	 * Constructs a controller.
	 * @param _appContainer		The appContainer.
	 * @param parent			The parent of the controller.
	 */
	public UpdatePersonalDetailsController(AppContainer _appContainer, Base parent) {
		super(_appContainer, parent);
		_practiceProvider = PracticeProviderSingleton.INSTANCE.get();
	}
	
	/**
	 * Post-constructor initialisation for injection.
	 */
	@Override
	public void initialise() {
		// Injections
		form = (Form)inject(InjectableEnum.UPDATE_PERSONAL_DETAILS_FORM);
	}
	
	// *** PUBLIC METHODS
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
    		JTextField contactEmailField,
    		XLabelStatus contactEmailError,
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
    		JButton backButton) {
		
		// * Execute API - Get Doctor
		api.output.GetDoctorOutput doctorAPI;
		try {
			doctorAPI = new api.input.GetDoctor().execute();
        }
        catch (DatabaseException | NoConnectionException e1) {
        	handleError(e1);
        	return;
        }
        if (isBadStatus(doctorAPI.status()))
        	return;
        
        // Assign
		doctorCombo.setSelectedItem(_practiceProvider.getDoctorByID(doctorAPI.doctorUUID()));
		
		// * Execute API - Get Details
		api.output.GetDetailsOutput detailsAPI;
		try {
			detailsAPI = new api.input.GetDetails().execute();
        }
        catch (DatabaseException | NoConnectionException e1) {
        	handleError(e1);
        	return;
        }
        if (isBadStatus(detailsAPI.status()))
        	return;
        
        // Assign
		titleCombo.setSelectedItem(detailsAPI.title());
		firstNameField.setText(detailsAPI.firstName());
		surnameField.setText(detailsAPI.surname());
		contactPhoneNumberField.setText(detailsAPI.contactPhoneNumber());
		contactEmailField.setText(detailsAPI.contactEmail());
		street1Field.setText(detailsAPI.street1());
		street2Field.setText(detailsAPI.street2());
		cityField.setText(detailsAPI.city());
		countyField.setText(detailsAPI.county());
		postCodeField.setText(detailsAPI.postCode());
		
		// * Form
		form.attachFormErrorReflection(statusLabel);
		GeneralComboBox<String> title = form.attach(new GeneralComboBox<String>(titleCombo, titleError));
		GeneralComboBox<String> doctor = form.attach(new GeneralComboBox<String>(doctorCombo, doctorError));
		GeneralText firstName = form.attach(new GeneralText(firstNameField, firstNameError));
		GeneralText surname = form.attach(new GeneralText(surnameField, surnameError));
		PhoneNumber contactPhoneNumber = form.attach(new PhoneNumber(contactPhoneNumberField, contactPhoneNumberError));
		EmailAddress contactEmail = form.attach(new EmailAddress(contactEmailField, contactEmailError));
		GeneralText street1 = form.attach(new GeneralText(street1Field, street1Error));
		GeneralText street2 = form.attach(new GeneralText(street2Field, street2Error));
		GeneralText city = form.attach(new GeneralText(cityField, cityError));
		GeneralText county = form.attach(new GeneralText(countyField, countyError));
		PostCode postCode = form.attach(new PostCode(postCodeField, postCodeError));
		
		// Add a listener to the confirm button
		confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	// Execute form
            	if (!form.execute())
            		return;
            	
            	// Execute API - Set Doctor
            	api.output.SetDoctorOutput setDoctorAPI;
            	try {
            		setDoctorAPI = new api.input.SetDoctor(doctor.postValue).execute();
                }
                catch (DatabaseException | NoConnectionException e1) {
                	handleError(e1);
                	return;
                }
                if (isBadStatus(setDoctorAPI.status()))
                	return;
            	
                // Action - was Set Doctor successful
            	if (Status.toAction(setDoctorAPI.status()) != Status.SUCCESS)
            		return;
            	
            	// Execute API - Set Details
            	api.output.SetDetailsOutput setDetailsAPI;
            	try {
            		setDetailsAPI = new api.input.SetDetails(
            				firstName.postValue,
            				surname.postValue,
            				title.postValue,
            				contactEmail.postValue,
            				contactPhoneNumber.postValue,
            				street1.postValue,
            				street2.postValue,
            				city.postValue,
            				county.postValue,
            				postCode.postValue
            				).execute();
                }
                catch (DatabaseException | NoConnectionException e1) {
                	handleError(e1);
                	return;
                }
                if (isBadStatus(setDetailsAPI.status()))
                	return;
        		
                // Set status, over details API
        		statusLabel.setStatus(StatusTransformer.specialiseSuccess(setDetailsAPI.status(), Status.UPDATE_DETAILS_SUCCESS));
            }
        });

		// Add a listener to the back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	setStatus(Status.DASHBOARD);
            }
        });
	}
}
