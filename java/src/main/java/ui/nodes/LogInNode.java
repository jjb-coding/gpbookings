package ui.nodes;

import java.util.EnumSet;

import ui.container.AbsorbsStatus;
import ui.container.AppContainer;
import ui.container.IDisplay;
import ui.container.InjectableEnum;
import ui.container.Node;
import ui.container.Parent;
import ui.container.ViewBag;
import ui.displays.LogInDisplay;

@Parent(ElementProgressBarNode.class)
@AbsorbsStatus("LOG_IN")
public class LogInNode extends Node {
	public LogInNode(AppContainer _appContainer, Node parent, ViewBag bag) {
		super(_appContainer, parent, bag);
		display = (IDisplay)new LogInDisplay(this);
	}

	@Override
	public String getTitleContribution() {
		return "Log In Or Register";
	}

	@Override
	public EnumSet<InjectableEnum> getInjectables() {
		return EnumSet.of(InjectableEnum.LOGIN_CONTROLLER, InjectableEnum.LOGIN_FORM);
	}
}
