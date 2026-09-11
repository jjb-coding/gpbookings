package ui.nodes;

import java.util.EnumSet;

import ui.container.*;
import ui.displays.WelcomeDisplay;

@Parent(AppNode.class)
@AbsorbsStatus("WELCOME")
public class WelcomeNode extends Node {
	public WelcomeNode(AppContainer _appContainer, Node parent, ViewBag bag) {
		super(_appContainer, parent, bag);
		display = (IDisplay)new WelcomeDisplay(this);
	}

	@Override
	public String getTitleContribution() {
		return "Welcome";
	}

	@Override
	public EnumSet<InjectableEnum> getInjectables() {
		return EnumSet.of(InjectableEnum.WELCOME_CONTROLLER);
	}
}
