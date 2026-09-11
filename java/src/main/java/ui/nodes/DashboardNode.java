package ui.nodes;

import java.util.EnumSet;

import ui.container.*;
import ui.displays.DashboardDisplay;

@Parent(ElementAccountBarNode.class)
@AbsorbsStatus("DASHBOARD")
public class DashboardNode extends Node {
	public DashboardNode(AppContainer _appContainer, Node parent, ViewBag bag) {
		super(_appContainer, parent, bag);
		display = new DashboardDisplay(this);
	}

	@Override
	public String getTitleContribution() {
		return "Dashboard";
	}

	@Override
	public EnumSet<InjectableEnum> getInjectables() {
		return EnumSet.of(InjectableEnum.DASHBOARD_CONTROLLER);
	}
}
