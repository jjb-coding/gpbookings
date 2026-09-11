package ui.nodes;

import java.util.EnumSet;

import ui.container.*;
import ui.displays.ViewAttendedBookingDisplay;

@Parent(ElementAccountBarNode.class)
@AbsorbsStatus("VIEW_ATTENDED_BOOKING")
public class ViewAttendedBookingNode extends Node {
	public ViewAttendedBookingNode(AppContainer _appContainer, Node parent, ViewBag bag) {
		super(_appContainer, parent, bag);
		display = (IDisplay)new ViewAttendedBookingDisplay(this);
	}

	@Override
	public String getTitleContribution() {
		return "View A Past Booking";
	}

	@Override
	public EnumSet<InjectableEnum> getInjectables() {
		return EnumSet.of(InjectableEnum.VIEW_ATTENDED_BOOKING_CONTROLLER);
	}

	@Override
	public InjectableEnum getExpectedInjectable() {
		return InjectableEnum.GET_ATTENDED_BOOKING_REQUEST;
	}
}
