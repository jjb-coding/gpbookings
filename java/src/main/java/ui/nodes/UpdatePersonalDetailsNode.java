package ui.nodes;

import java.util.EnumSet;

import ui.container.*;
import ui.displays.UpdatePersonalDetailsDisplay;

@Parent(ElementAccountBarNode.class)
@AbsorbsStatus("UPDATE_DETAILS")
public class UpdatePersonalDetailsNode extends Node {
	public UpdatePersonalDetailsNode(AppContainer _appContainer, Node parent, ViewBag bag) {
		super(_appContainer, parent, bag);
		display = (IDisplay)new UpdatePersonalDetailsDisplay(this);
	}

	@Override
	public String getTitleContribution() {
		return "Update Personal Details";
	}

	@Override
	public EnumSet<InjectableEnum> getInjectables() {
		return EnumSet.of(
				InjectableEnum.UPDATE_PERSONAL_DETAILS_FORM,
				InjectableEnum.UPDATE_PERSONAL_DETAILS_CONTROLLER
				);
	}
}
