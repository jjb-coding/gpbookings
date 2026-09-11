package ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import javax.swing.JButton;
import exceptions.DatabaseException;
import exceptions.NoConnectionException;
import form.field.fields.GeneralComboBox;
import form.form.Form;
import services.practiceProvider.PracticeProvider;
import services.practiceProvider.PracticeProviderSingleton;
import status.Status;
import status.StatusTransformer;
import ui.component.components.XComboBoxBookingSlot;
import ui.component.components.XComboBoxDate;
import ui.component.components.XLabelStatus;
import ui.container.AppContainer;
import ui.container.Base;
import ui.container.InjectableEnum;
import ui.container.InjectableObject;
import ui.object.DateItem;

public class MakeBookingController extends InjectableObject {
	// Injections
	Form form;
	PracticeProvider _practiceProvider;
	
	/**
	 * Constructs a controller.
	 * @param _appContainer		The appContainer.
	 * @param parent			The parent of the controller.
	 */
	public MakeBookingController(AppContainer _appContainer, Base parent) {
		super(_appContainer, parent);
		// Singletons
		_practiceProvider = PracticeProviderSingleton.INSTANCE.get();
	}
	
	/**
	 * Post-constructor initialisation for injection.
	 */
	public void initialise() {
		// Injections
		form = (Form)inject(InjectableEnum.MAKE_BOOKING_FORM);
	}
	
	public void attach(
			XLabelStatus statusLabel,
    		JButton submitButton,
    		JButton cancelButton,
    		XComboBoxDate dateCombo,
    		XLabelStatus dateError,
    		XComboBoxBookingSlot slotCombo,
    		XLabelStatus slotError
			) {
		// Form
		form.attachFormErrorReflection(statusLabel);
		GeneralComboBox<Date> date = form.attach(new GeneralComboBox<Date>(dateCombo, dateError));
		GeneralComboBox<Integer> slot = form.attach(new GeneralComboBox<Integer>(slotCombo, slotError));
		
		// Initialise
		DateItem dateItem = (DateItem)dateCombo.getSelectedItem();
		if (dateItem != null)
	    	slotCombo.populate(_practiceProvider.getBookingSlots(dateItem.id()));
		
        // Submit button action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	submitButton.setEnabled(false);
                statusLabel.setStatus(Status.MAKE_BOOKING_READY);

            	// Submit Form
                if (!form.execute()) {
                    submitButton.setEnabled(true);
                    return;
                }

                // Execute API
                api.output.MakeBookingOutput api = null;
                try {
                	api = new api.input.MakeBooking(date.postValue, slot.postValue).execute();
                }
                catch (DatabaseException | NoConnectionException e1) {
                	handleError(e1);
                	return;
                }
                if (isBadStatus(api.status()))
                	return;
                
                // Reflect success
                statusLabel.setStatus(StatusTransformer.specialiseSuccess(api.status(), Status.MAKE_BOOKING_SUCCESS));

                // Proceed or not
                if (Status.toAction(api.status()) != Status.SUCCESS) {
                	submitButton.setEnabled(false);
                	return;
                }
                
                // Timered transition
                setStatusAfterWait(1500, Status.DASHBOARD);
            }
        });
        
        // Cancel button action
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	setStatus(Status.DASHBOARD);
            }
        });
        
        // Link date combo to slot combo
        dateCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Date date = ((DateItem)dateCombo.getSelectedItem()).id();
            	slotCombo.populate(_practiceProvider.getBookingSlots(date));
            }
        });
		}
}