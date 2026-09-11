package ui.nodes;

import java.util.EnumSet;

import ui.container.AppContainer;
import ui.container.IDisplay;
import ui.container.InjectableEnum;
import ui.container.Node;
import ui.container.Parent;
import ui.container.ViewBag;
import ui.displays.ElementProgressBarDisplay;

@Parent(AppNode.class)
public class ElementProgressBarNode extends Node {
	public ElementProgressBarNode(AppContainer _appContainer, Node parent, ViewBag bag) {
		super(_appContainer, parent, bag);
		display = (IDisplay)new ElementProgressBarDisplay(this);
	}

	@Override
	public String getTitleContribution() {
		return null;
	}

	@Override
	public EnumSet<InjectableEnum> getInjectables() {
		return EnumSet.of(InjectableEnum.PROGRESSBAR_PUBLISHER, InjectableEnum.PROGRESSBAR_CONTROLLER);
	}
}
