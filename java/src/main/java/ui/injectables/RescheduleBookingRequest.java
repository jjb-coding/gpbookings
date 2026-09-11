package ui.injectables;

import ui.container.AppContainer;
import ui.container.Base;
import ui.container.InjectableObject;

public class RescheduleBookingRequest extends InjectableObject {
	// *** FIELDS
	String bookingUUID;	

	// *** CONSTRUCTORS
	public RescheduleBookingRequest(AppContainer _appContainer, Base parent) {
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
