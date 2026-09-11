package ui.nodes;

import java.util.EnumSet;

import ui.container.*;
import ui.displays.NewSecondDisplay;

@Parent(NewNode.class)
@AbsorbsStatus("NEW_SECOND")
public class NewSecondNode extends Node {
	public NewSecondNode(AppContainer _appContainer, Node parent, ViewBag bag) {
		super(_appContainer, parent, bag);
		display = (IDisplay)new NewSecondDisplay(this);
	}

	@Override
	public String getTitleContribution() {
		return "2 of 2";
	}

	@Override
	public EnumSet<InjectableEnum> getInjectables() {
		return null;
	}
}
