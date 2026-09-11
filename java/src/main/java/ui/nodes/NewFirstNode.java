package ui.nodes;

import java.util.EnumSet;

import ui.container.*;
import ui.displays.NewFirstDisplay;

@Parent(NewNode.class)
@AbsorbsStatus("NEW_FIRST")
public class NewFirstNode extends Node {
	public NewFirstNode(AppContainer _appContainer, Node parent, ViewBag bag) {
		super(_appContainer, parent, bag);
		display = (IDisplay)new NewFirstDisplay(this);
	}

	@Override
	public String getTitleContribution() {
		return "1 of 2";
	}

	@Override
	public EnumSet<InjectableEnum> getInjectables() {
		return null;
	}
}
