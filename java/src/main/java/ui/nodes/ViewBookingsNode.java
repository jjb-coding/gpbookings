package ui.nodes;

import java.util.EnumSet;

import ui.container.*;
import ui.displays.ViewBookingsDisplay;

@Parent(ElementAccountBarNode.class)
@AbsorbsStatus("VIEW_BOOKINGS")
public class ViewBookingsNode extends Node {
	public ViewBookingsNode(AppContainer _appContainer, Node parent, ViewBag bag) {
		super(_appContainer, parent, bag);
		display = new ViewBookingsDisplay(this);
	}

	@Override
	public String getTitleContribution() {
		return "View Bookings";
	}

	@Override
	public EnumSet<InjectableEnum> getInjectables() {
		return EnumSet.of(InjectableEnum.VIEW_BOOKINGS_CONTROLLER);
	}

}
