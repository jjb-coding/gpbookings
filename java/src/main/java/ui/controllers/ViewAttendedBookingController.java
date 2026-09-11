package ui.controllers;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import exceptions.DatabaseException;
import exceptions.NoConnectionException;
import services.practiceProvider.PracticeProvider;
import services.practiceProvider.PracticeProviderSingleton;
import ui.container.AppContainer;
import ui.container.Base;
import ui.container.InjectableEnum;
import ui.container.InjectableObject;
import ui.injectables.GetAttendedBookingRequest;

public class ViewAttendedBookingController extends InjectableObject {
	// Singletons
	PracticeProvider practiceProvider;
	// Injections
	GetAttendedBookingRequest request;
	
	/**
	 * Constructs a controller.
	 * @param _appContainer		The appContainer.
	 * @param parent			The parent of the controller.
	 */
	public ViewAttendedBookingController(AppContainer _appContainer, Base parent) {
		super(_appContainer, parent);
	}
	
	/**
	 * Post-constructor initialisation for injection.
	 */
	public void initialise() {
		request = (GetAttendedBookingRequest)inject(InjectableEnum.GET_ATTENDED_BOOKING_REQUEST);
		practiceProvider = PracticeProviderSingleton.INSTANCE.get();
	}
	
	public void attach(
    		JLabel bookingTimestampValue,
    		JLabel modifiedTimestampValue,
    		JLabel doctorValue,
    		JTextArea summaryText,
    		JTextArea prescriptionsText) {

		// * Execute API
		api.output.GetAttendedBookingOutput api;
		try {
        	api = new api.input.GetAttendedBooking(request.getBookingUUID()).execute();
        }
        catch (DatabaseException | NoConnectionException e1) {
        	handleError(e1);
        	return;
        }
        if (isBadStatus(api.status()))
        	return;
        
        // Assign
		bookingTimestampValue.setText(api.bookingTimestamp().toString());
		modifiedTimestampValue.setText(api.modifiedTimestamp().toString());
		doctorValue.setText(practiceProvider.getDoctorByID(api.DoctorUUID()).getFormattedName());
		summaryText.setText(api.Summary());
		prescriptionsText.setText(api.PrescriptionsJSON());
	}
}
