package ui.nodes;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import ui.container.*;
import ui.displays.TransparentPanelDisplay;

@Parent(ElementProgressBarNode.class)
public class NewNode extends MultiNode {
	public NewNode(AppContainer _appContainer, Node parent, ViewBag bag) {
		super(_appContainer, parent, bag);
		display = (IDisplay)new TransparentPanelDisplay(this);
	}

	@Override
	public String getTitleContribution() {
		return "Register";
	}

	@Override
	public EnumSet<InjectableEnum> getInjectables() {
		return EnumSet.of(
				InjectableEnum.NEW_FIRST_FORM,
				InjectableEnum.NEW_FIRST_CONTROLLER,
				InjectableEnum.NEW_SECOND_FORM,
				InjectableEnum.NEW_SECOND_CONTROLLER);
	}

	@Override
	public HashSet<Class<?>> getPersistentChildren() {
		return new HashSet<Class<?>>(Set.of(
				NewFirstNode.class, 
				NewSecondNode.class
				));
	}
}
