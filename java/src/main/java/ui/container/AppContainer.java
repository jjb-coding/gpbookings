package ui.container;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import status.Status;
import util.ReflectionUtil;
import ui.nodes.AppNode;

/**
 * Contains and manages the app. Receives Status change requests & reconfigures
 * the UI stack, adding injectable objects where necessary.  
 */
public class AppContainer {
	// *** FIELDS
	// Maps
	HashMap<Class<?>,NodeConfiguration> classToNodeConfigurationMap;
	HashMap<Class<?>,InjectableConfiguration> classToInjectableConfigurationMap;

	// *** CONSTRUCTORS
	@SuppressWarnings("unchecked")
	public AppContainer() {
		ArrayList<Class<?>> nodes;
		try {
			nodes = ReflectionUtil.getNamedClasses("ui.nodes");
		}
		catch (Exception e) {
			throw new RuntimeException("APP:init: Couldn't get nodes.");
		}
		
		// For each node
		classToNodeConfigurationMap = new HashMap<>();
		HashSet<NodeConfiguration> leaves = new HashSet<>();
		for (Class<?> node : nodes) {
			if (node == null)
				continue;
			if (!Node.class.isAssignableFrom(node))
				throw new RuntimeException("APP:init[" + node.getSimpleName() + "]: Is not of type Node.");
			Class<? extends Node> nodeUp = (Class<? extends Node>)node;
			NodeConfiguration nodeConfiguration = new NodeConfiguration(nodeUp);
			if (nodeConfiguration.status != null)
				leaves.add(nodeConfiguration);
			classToNodeConfigurationMap.put(nodeUp, nodeConfiguration);
		}
		
		// For each leaf
		for (NodeConfiguration nodeConfiguration : leaves) {
			NodeConfiguration current = nodeConfiguration;
			while (current.parentCls != null) {
				// Fetch or generate actual parent
				NodeConfiguration parent = classToNodeConfigurationMap.get(current.parentCls);
				if (parent == null)
					throw new RuntimeException("APP:init[" + current.parentCls.getSimpleName() + "]: Is not a scanned class.");
				
				// If leaf, propagate status; otherwise, propagate set of statuses
				if (current == nodeConfiguration)
					parent.descendants.add(nodeConfiguration.status);
				else
					parent.descendants.addAll(current.descendants);
				
				// Add child
				parent.children.add(current);
				
				// Recurse
				current = parent;
			}
		}
		
		// For each injectable
		classToInjectableConfigurationMap = new HashMap<>();
		for (InjectableEnum injectable : InjectableEnum.values())
			classToInjectableConfigurationMap.put(injectable.getImplementer(), new InjectableConfiguration(injectable.getImplementer()));
	}

	// *** PUBLIC METHODS
	/**
	 * Provides a method for UI Nodes to reason about how to propagate a status change.
	 * @param cls		The class of the calling node.
	 * @param status 	The status that is being resolved.
	 * @return			cls if STOP; null if BACK; otherwise, the class to generate or fetch.
	 */
	public Class<?> decide(Class<? extends Node> cls, Status status) {
		NodeConfiguration nodeConfiguration = classToNodeConfigurationMap.get(cls);
		// VALIDATE: all Nodes should have been scanned
		if (nodeConfiguration == null)
			throw new RuntimeException("APP:decide: Couldn't find configuration for " + cls.getSimpleName());
		
		// If status is status, then it resolves - STOP
		if (nodeConfiguration.status == status)
			return cls;
		
		// If descendants don't contain status at all, then BACK
		if (!nodeConfiguration.descendants.contains(status))
			return null;
			
		// Search immediate children to see which contains the status
		for (NodeConfiguration child : nodeConfiguration.children)
			if (child.descendants.contains(status) || child.status == status)
				return child.thisCls;
			
		// fall-through: should not be possible to reach this
		System.out.println(status.name());
		System.out.println(cls);
		System.out.println("--");
		for (Status x : nodeConfiguration.descendants)
			System.out.println(x);

		System.out.println("--");
		for (NodeConfiguration child : nodeConfiguration.children) {
			for (Status x : child.descendants)
				System.out.println(x);
			System.out.println("-");
		}
		throw new RuntimeException("APP:decide: Failed on " + cls.getSimpleName());
	}
	
	/**
	 * Performs an initial launch with some Status.
	 * @param status	The starting status.
	 */
	public Node start(Status status) {
		Node node = launch(null, AppNode.class);
		node.setStatus(status);
		return node;
	}
	
	/**
	 * Launches a Node instance.
	 * @param launcher	The launcher instance.
	 * @param cls		The class to launch.
	 * @return			The object that has been launched.
	 */
	public Node launch(Node launcher, Class<?> cls) {
		NodeConfiguration configuration = classToNodeConfigurationMap.get(cls);
		if (configuration == null)
			throw new RuntimeException("");
		return (Node)configuration.execute(this, launcher, null);
	}
	
	/**
	 * Launches a Node instance with a ViewBag.
	 * @param launcher	The launcher instance.
	 * @param cls		The class to launch.
	 * @return			The object that has been launched.
	 */
	public Node launch(Node launcher, Class<?> cls, ViewBag bag) {
		NodeConfiguration configuration = classToNodeConfigurationMap.get(cls);
		if (configuration == null)
			throw new RuntimeException("");
		return (Node)configuration.execute(this, launcher, bag);
	}
	
	/**
	 * Launches an Injectable instance.
	 * @param launcher	The launcher instance.
	 * @param cls		The class to launch.
	 * @return			The object that has been launched.
	 */
	public InjectableObject launchInjectable(Base launcher, InjectableEnum injectableEnum) {
		InjectableConfiguration configuration = classToInjectableConfigurationMap.get(injectableEnum.getImplementer());
		if (configuration == null)
			throw new RuntimeException("APP:runtime: Couldn't find injectable " + injectableEnum.name());
		return (InjectableObject)configuration.execute(this, launcher);
	}

	/**
	 * Creates a ViewBag, an InjectableObject with deferred parentage.
	 * @param injectable
	 * @return
	 */
	public ViewBag createViewBag(InjectableEnum injectableEnum) {
		InjectableConfiguration configuration = classToInjectableConfigurationMap.get(injectableEnum.getImplementer());
		if (configuration == null)
			throw new RuntimeException("APP:runtime: Couldn't find injectable for ViewBag of name " + injectableEnum.name());
		return new ViewBag(injectableEnum, (InjectableObject)configuration.execute(this, null));
	}
	
	/**
	 * Gets the Status of a leaf node by class.
	 */
	public Status getStatus(Class<?> cls) {
		NodeConfiguration configuration = classToNodeConfigurationMap.get(cls);
		if (configuration == null)
			throw new RuntimeException("Not a recognised node");
		return configuration.status;
	}
}

/**
 * Classifies the data obtained from scanning a class whose superclass is Node.
 */
class NodeConfiguration extends Configuration {
	Class<?> parentCls;
	Status status;
	EnumSet<Status> descendants;
	ArrayList<NodeConfiguration> children;
		
	public NodeConfiguration(Class<?> cls) {
		super(cls, true);

		// Initialise descendants & children
		descendants = EnumSet.noneOf(Status.class);
		children = new ArrayList<>();
		
		// ReflectsStatus
		AbsorbsStatus reflectsStatusAnnotation = cls.getAnnotation(AbsorbsStatus.class);
		if (reflectsStatusAnnotation == null)
			status = null;
		else
			status = Status.fromName(reflectsStatusAnnotation.value());

		// Parent
		Parent parentAnnotation = cls.getAnnotation(Parent.class);
		if (parentAnnotation == null)
			parentCls = null;
		else
			parentCls = parentAnnotation.value();
	}
	
	public Object execute(AppContainer appContainer, Node parent, ViewBag bag) {
		Object[] values = new Object[3];
		values[0] = appContainer;
		values[1] = parent;
		values[2] = bag;
		
		/**
		if (appContainer != null)
			System.out.println(appContainer.getClass().getSimpleName());
		else
			System.out.println(appContainer);
		if (parent != null)
			System.out.println(parent.getClass().getSimpleName());
		else
			System.out.println(parent);
		if (bag != null)
			System.out.println(bag.getClass().getSimpleName());
		else
			System.out.println(bag);

		System.out.println(thisCls.getSimpleName());
		System.out.println("---");
		*/
		
		Object ret;
		try {
			ret = constructor.newInstance(values);
		}
		catch (Exception e) {
			throw new RuntimeException("APP:runtime: Couldn't instantiate " + thisCls.getSimpleName(), e.getCause());
		}
		return ret;
	}
}

/**
 * Classifies the data obtained from scanning a class whose superclass is InjectableObject.
 */
class InjectableConfiguration extends Configuration {
	public InjectableConfiguration(Class<?> cls) {
		super(cls, false);
	}

	/**
	 * Executes a configuration. Launches an Injectable.
	 * @param appContainer
	 * @return
	 */
	public Object execute(AppContainer appContainer, Base parent) {
		Object[] values = new Object[2];
		values[0] = appContainer;
		values[1] = parent;
		
		Object ret;
		try {
			ret = constructor.newInstance(values);
		}
		catch (Exception e) {
			throw new RuntimeException("APP:runtime: Couldn't instantiate injectable " + thisCls.getSimpleName(), e.getCause());
		}
		return ret;
	}
}

/**
 * Base class for scanning AppBase types.
 */
class Configuration {
	// This class
	Class<?> thisCls;
	// Constructor for the node
	final Constructor<?> constructor;
	
	public Configuration(Class<?> cls, boolean isNode) {
		// thisCls
		thisCls = cls;
		
		// Constructor
		Constructor<?>[] constructors = cls.getDeclaredConstructors();
		if (constructors.length != 1)
			throw new RuntimeException("APP:Node[" + cls.getSimpleName() + "]: Must only have 1 constructor.");
		constructor = constructors[0];
		
		// Scan constructor
		Type[] types = constructor.getGenericParameterTypes();

		// VALIDATE: length
		if (types.length != 2 && !isNode)
			throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: Two parameters were expected.");
		if (types.length != 3 && isNode)
			throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: Three parameters were expected.");
		
		// VALIDATE: is first parameter AppContainer?
		Type appContainerType = types[0];
		if (!(appContainerType.equals(AppContainer.class)))
			throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: First parameter is not AppContainer type.");
		
		// VALIDATE: is second parameter class?
		Type parentType = types[1];
		if (!(parentType instanceof Class<?>))
			throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: Second parameter is not a class.");
		
		// VALIDATE: is second parameter parent?
		Class<?> parentCls = (Class<?>)parentType;
		if (!(Base.class.isAssignableFrom(parentCls)))
			throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: Second parameter is not a subtype of Base.");
		
		// VALIDATE: is third parameter class?
		if (isNode) {
			// VALIDATE: is third parameter ViewBag?
			Type viewBagType = types[2];
			if (!(viewBagType.equals(ViewBag.class)))
				throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: Third parameter is not ViewBag type.");
		}
	}
}

// Experiments with constructor injection, taken out due to complexity
/**
	// Process rest of parameters, if any
	ArrayList<Class<?>> injectablesList = new ArrayList<>();
	for (int i = 2; i < size; i++) {
		Type type = types[i];
		
		// VALIDATE: is it a ParametrizedType?
		if (!(type instanceof ParameterizedType))
			throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: Encountered a non-parametrised type in the constructor arguments.");
		ParameterizedType upType = (ParameterizedType)type;
		
		// VALIDATE: is it an Inject type?
		Type parameterType = upType.getRawType();
		if (!parameterType.equals(Inject.class))
			throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: Encountered a parametrised type in the constructor arguments that is not of type Inject.");
		
		// VALIDATE: is it Inject<T,U>?
		Type[] generics = upType.getActualTypeArguments();
		if (generics.length != 2)
			throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: Encountered an Inject argument that does not have 2 type parameters.");
		
		// VALIDATE: is it output class?
		Type output = generics[0];
		if (!(output instanceof Class<?>))
			throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: Encountered an Inject argument whose first parameter is not a class.");
		
		// VALIDATE: is it injectable class?
		Type injectable = generics[1];
		if (!(injectable instanceof Class<?>))
			throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: Encountered an Inject argument whose second parameter is not a class.");
		
		// VALIDATE: is injectable class a subtype of Injectable?
		Class<?> injectableCls = (Class<?>)injectable;
		if (!(Injectable.class.isAssignableFrom(injectableCls)))
			throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: Encountered an Inject argument whose second parameter is not a subtype of Node.");
		
		// Add to list
		injectablesList.add(injectableCls);
	}
	
	injectables = (Class<?>[])injectablesList.toArray();
*/