package ui.container;

import status.Status;
import ui.component.IReflectsStatus;

/**
 * Base class, common to Injectables and Nodes. Reflects status
 * in an application-wide way.
 */
public abstract class Base
	implements IReflectsStatus {
	// Fields
	protected AppContainer _appContainer;
	protected Base parent;
	
	// *** PUBLIC METHODS
	/**
	 * Sends a page back request down the tree.
	 */
	public void sendBack() {
		if (parent == null)
			throw new RuntimeException("APP: Couldn't send back.");
		parent.sendBack();
	}
	
	/**
	 * Attaches the AppContainer and parent.
	 * @param _appContainer The AppContainer.
	 * @param parent		The immediate parent.
	 */
	public Base(AppContainer _appContainer, Base parent) {
		this._appContainer = _appContainer;
		this.parent = parent;
	}
	
	public abstract Object inject(InjectableEnum injectable);

	/**
	 * Passes the injection search to the parent. Will throw
	 * a runtime exception if the Base stack has been exhausted.
	 */
	protected Object parentInject(InjectableEnum injectable) {
		if (parent == null)
			throw new RuntimeException("APP: Couldn't find injectable " + injectable.name());
		return parent.inject(injectable);
	}
	
	/**
	 * Does normal status propagation, but with a ViewBag - an injectable object. 
	 */
	public abstract void setStatus(Status status, ViewBag viewBag);

	/**
	 * Passes the status resolution to the parent. Will throw
	 * a runtime exception if the Base stack has been exhausted.
	 * @param status
	 */
	protected void setParentStatus(Status status) {
		if (status == Status.EXIT)
			entry.Main.exit(status);
		
		if (parent == null)
			throw new RuntimeException("APP:base: Status wasn't caught: " + status.name());
		parent.setStatus(status);
	}
	
	/**
	 * Passes the status resolution to the parent, with a viewBag. Will throw
	 * a runtime exception if the Base stack has been exhausted.
	 * @param status
	 */
	protected void setParentStatus(Status status, ViewBag bag) {
		if (parent == null)
			throw new RuntimeException("APP:base: Status (with ViewBag) wasn't caught: " + status.name());
		parent.setStatus(status, bag);
	}
}
