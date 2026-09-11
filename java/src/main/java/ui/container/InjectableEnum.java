package ui.container;

import form.form.Form;
import ui.controllers.*;
import ui.injectables.*;

public enum InjectableEnum {
	DASHBOARD_CONTROLLER					(DashboardController.class),
	LOGIN_CONTROLLER						(LogInController.class),
	MAKE_BOOKING_CONTROLLER					(MakeBookingController.class),
	NEW_FIRST_CONTROLLER					(NewFirstController.class),
	NEW_SECOND_CONTROLLER					(NewSecondController.class),
	UPDATE_PERSONAL_DETAILS_CONTROLLER		(UpdatePersonalDetailsController.class),
	VIEW_ATTENDED_BOOKING_CONTROLLER		(ViewAttendedBookingController.class),
	VIEW_BOOKINGS_CONTROLLER				(ViewBookingsController.class),
	WELCOME_CONTROLLER						(WelcomeController.class),
	RESCHEDULE_BOOKING_CONTROLLER			(RescheduleBookingController.class),
	
	PROGRESSBAR_CONTROLLER					(ElementProgressBarController.class),
	ACCOUNTBAR_CONTROLLER					(ElementAccountBarController.class),
	
	LOGIN_FORM								(Form.class),
	NEW_FIRST_FORM							(Form.class),
	NEW_SECOND_FORM							(Form.class),
	MAKE_BOOKING_FORM						(Form.class),
	RESCHEDULE_BOOKING_FORM					(Form.class),
	UPDATE_PERSONAL_DETAILS_FORM			(Form.class),
	
	LOGIN_DATA								(LogInData.class),
	PROGRESSBAR_PUBLISHER					(ProgressBarPublisher.class),
	GET_ATTENDED_BOOKING_REQUEST			(GetAttendedBookingRequest.class),
	RESCHEDULE_BOOKING_REQUEST				(RescheduleBookingRequest.class)
	;
	// *** FIELDS
	final Class<?> implementer;
	
	// *** CONSTRUCTORS
	/**
	 * Constructs an Injectable instance.
	 * @param implementer	The class that implements this injectable.
	 */
	InjectableEnum(Class<?> implementer) {
		this.implementer = implementer;
	}
	
	// *** GETTERS & SETTERS
	public Class<?> getImplementer() {
		return implementer;
	}
}
