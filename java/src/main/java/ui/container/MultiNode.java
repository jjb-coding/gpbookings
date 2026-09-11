package ui.container;

import java.util.HashMap;
import java.util.HashSet;

public abstract class MultiNode extends Node {
	// *** FIELDS
	HashMap<Class<?>, Node> persistentChildren;
	
	// *** CONSTRUCTORS
	/**
	 * Creates an instance of a MultiNode. This class stores multiple children
	 * persistently, allowing them to retain state.
	 * @param _appContainer		The app container.
	 * @param parent			The parent.
	 * @param bag				The view bag.
	 */
	public MultiNode(AppContainer _appContainer, Node parent, ViewBag bag) {
		super(_appContainer, parent, bag);
		
		persistentChildren = new HashMap<>();
		for (Class<?> cls : getPersistentChildren())
			persistentChildren.put(cls, _appContainer.launch(this, cls));
	};
	
	/**
	 * Sources a node from the list of persistent children, instead of
	 * creating it.
	 */
	@Override
	protected Node sourceNode(Class<?> cls, ViewBag bag) {
		Node node = persistentChildren.get(cls);
		if (node == null)
			throw new RuntimeException("APP:MultiNode[" + this.getClass().getSimpleName() + "]: Couldn't source " + cls.getSimpleName());
		return node;
	}

	
	// *** ABSTRACTIONS
	/**
	 * Supplies the list of Nodes that this Node persists.
	 * @return	The list of nodes.
	 */
	public abstract HashSet<Class<?>> getPersistentChildren();
}
