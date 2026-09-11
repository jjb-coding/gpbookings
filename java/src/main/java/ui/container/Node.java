package ui.container;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;

import status.Status;

public abstract class Node extends Base {
	// *** FIELDS
	protected Node child;
	EnumMap<InjectableEnum,InjectableObject> injectables;
	protected IDisplay display;

	ViewBag viewBagCache;

	// *** CONSTRUCTORS	
	/**
	 * Constructs a Node instance.
	 * @param _appContainer	The app container.
	 * @param parent		The parent.
	 * @param bag			The view bag.
	 */
	public Node(AppContainer _appContainer, Node parent, ViewBag bag) {
		super(_appContainer, parent);
		
		// * INJECTABLES
		injectables = new EnumMap<>(InjectableEnum.class);
		// ViewBag
		if (getExpectedInjectable() != null)
			if (bag != null && bag.injectable() == getExpectedInjectable()) {
				bag.instance().lateAssignParent(this);
				injectables.put(bag.injectable(), bag.instance());
			}
			else
				throw new RuntimeException("APP:node[" + this.getClass().getSimpleName() + "]: Wasn't provided " + getExpectedInjectable().name());
		
		// Launched
		if (getInjectables() != null)
			for (InjectableEnum injectable : getInjectables())
				injectables.put(injectable, _appContainer.launchInjectable(this, injectable));
		
		// Initialise all injectables to allow them to interact
		for (InjectableObject object : injectables.values())
			object.initialise();
	};

	// *** GETTERS
	public IDisplay getDisplay() {
		return display;
	}
	
	// *** PUBLIC METHODS
	/**
	 * Propagates title information up the tree & packs. Will iterate
	 * until it reaches the root Node.
	 * @param sb	The list of strings used to build the title.
	 */
	public void refresh(ArrayList<String> stringList) {
		String title = getTitleContribution();
		if (title != null)
			stringList.add(title);
		((Node)parent).refresh(stringList);
	}
	
	/**
	 * Sets the status on this node.
	 * @param status The status. Must be a Node type i.e. DASHBOARD.
	 */
	public void setStatus(Status status) {
		Class<? extends Node> thisCls = this.getClass();
		Class<?> cls = _appContainer.decide(thisCls, status);
		
		// STOP
		if (cls == thisCls) {
			refresh(new ArrayList<>());
		}
		// BACK
		else if (cls == null) {
			setParentStatus(status);
		}
		// LAUNCH & CONTINUE
		else {
			Node newChild = sourceNode(cls, null);
			swapChild(newChild);
			child.setStatus(status);
		}
	}
	
	/**
	 * Sets the status on this node, also propagating a ViewBag.
	 * @param status The status. Must be a Node type i.e. DASHBOARD.
	 * @param bag	 The view bag.
	 */
	public void setStatus(Status status, ViewBag bag) {
		Class<? extends Node> thisCls = this.getClass();
		Class<?> cls = _appContainer.decide(thisCls, status);
		
		// STOP
		if (cls == thisCls) {
			refresh(new ArrayList<>());
		}
		// BACK
		else if (cls == null) {
			setParentStatus(status, bag);
		}
		// LAUNCH & CONTINUE
		else {
			// Construct & swap container element logically
			Node newChild = sourceNode(cls, bag);
			swapChild(newChild);
			
			// Measure whether the bag's contents have been assigned;
			// if not, pass it along.
			if (bag.instance().hasParent())
				child.setStatus(status);
			else
				child.setStatus(status, bag);
		}
	}
	
	/**
	 * Fetches an injectable object. If it is not provided by
	 * this node, looks for it in the parent node, recursively.
	 */
	@Override
	public Object inject(InjectableEnum injectable) {
		if (injectables != null) {
			Object object = injectables.get(injectable);
			if (object != null)
				return object;
		}
		return parentInject(injectable);
	};
	
	// *** PRIVATE & PROTECTED METHODS
	/**
	 * Propagates a getLeaf request up to the leaf.
	 * @return	The leaf.
	 */
	protected Node getLeaf() {
		if (child != null)
			return child.getLeaf();
		else
			return this;
	}
	
	protected void swapChild(Node newChild) {
		child = newChild;
		display.swapChild(child.display);
	};

	/**
	 * Sources a node by generating it dynamically.
	 */
	protected Node sourceNode(Class<?> cls, ViewBag bag) {
		return (Node)_appContainer.launch(this, cls, bag);
	}
	
	// *** ABSTRACTIONS
	/**
	 * Supplies a portion of the title string.
	 * @return	A portion of the title string, or null if no contribution is to be made.
	 */
	public abstract String getTitleContribution();
	
	/**
	 * Supplies the list of injectables that this Node provides.
	 * @return	The list of injectables. Null if this Node supplies none.
	 */
	public abstract EnumSet<InjectableEnum> getInjectables();
	
	/**
	 * Supplies the injectable that this Node expects to receive
	 * from a ViewBag.
	 * @return	The injectable. Null if nothing is expected.
	 */
	public InjectableEnum getExpectedInjectable() {
		return null;
	};
	
}
