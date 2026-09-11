package ui.nodes;

import java.util.EnumSet;

import ui.container.*;
import ui.displays.RescheduleBookingDisplay;

@Parent(ElementAccountBarNode.class)
@AbsorbsStatus("RESCHEDULE_BOOKING")
public class RescheduleBooking extends Node {
	public RescheduleBooking(AppContainer _appContainer, Node parent, ViewBag bag) {
		super(_appContainer, parent, bag);
		display = (IDisplay)new RescheduleBookingDisplay(this);
	}

	@Override
	public String getTitleContribution() {
		return "Reschedule Booking";
	}

	@Override
	public EnumSet<InjectableEnum> getInjectables() {
		return EnumSet.of(
				InjectableEnum.RESCHEDULE_BOOKING_FORM,
				InjectableEnum.RESCHEDULE_BOOKING_CONTROLLER
				);
	}
	
	@Override
	public InjectableEnum getExpectedInjectable() {
		return InjectableEnum.RESCHEDULE_BOOKING_REQUEST;
	}

}
