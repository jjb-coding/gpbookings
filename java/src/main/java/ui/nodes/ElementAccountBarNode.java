package ui.nodes;

import java.util.EnumSet;

import ui.container.AppContainer;
import ui.container.IDisplay;
import ui.container.InjectableEnum;
import ui.container.Node;
import ui.container.Parent;
import ui.container.ViewBag;
import ui.displays.ElementAccountBarDisplay;

@Parent(ElementProgressBarNode.class)
public class ElementAccountBarNode extends Node {
	public ElementAccountBarNode(AppContainer _appContainer, Node parent, ViewBag bag) {
		super(_appContainer, parent, bag);
		display = (IDisplay)new ElementAccountBarDisplay(this);
	}

	@Override
	public String getTitleContribution() {
		return null;
	}

	@Override
	public EnumSet<InjectableEnum> getInjectables() {
		return EnumSet.of(InjectableEnum.ACCOUNTBAR_CONTROLLER);
	}
	
	@Override
	public InjectableEnum getExpectedInjectable() {
		return InjectableEnum.LOGIN_DATA;
	}
}
