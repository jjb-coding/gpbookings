package ui.nodes;

import java.util.EnumSet;

import ui.container.*;
import ui.displays.MakeBookingDisplay;

@Parent(ElementAccountBarNode.class)
@AbsorbsStatus("MAKE_BOOKING")
public class MakeBookingNode extends Node {
	public MakeBookingNode(AppContainer _appContainer, Node parent, ViewBag bag) {
		super(_appContainer, parent, bag);
		display = new MakeBookingDisplay(this);
	}

	@Override
	public String getTitleContribution() {
		return "Make Booking";
	}

	@Override
	public EnumSet<InjectableEnum> getInjectables() {
		return EnumSet.of(InjectableEnum.MAKE_BOOKING_CONTROLLER, InjectableEnum.MAKE_BOOKING_FORM);
	}
}
