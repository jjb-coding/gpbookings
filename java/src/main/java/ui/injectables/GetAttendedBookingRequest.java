package ui.injectables;

import ui.container.AppContainer;
import ui.container.Base;
import ui.container.InjectableObject;

public class GetAttendedBookingRequest extends InjectableObject {
	// *** FIELDS
	String bookingUUID;	

	// *** CONSTRUCTORS
	public GetAttendedBookingRequest(AppContainer _appContainer, Base parent) {
		super(_appContainer, parent);
	}
	
	// *** PUBLIC METHODS
	public void configure(String bookingUUID) {
		this.bookingUUID = bookingUUID;
	}
	
	public String getBookingUUID() {
		return bookingUUID;
	}
}
