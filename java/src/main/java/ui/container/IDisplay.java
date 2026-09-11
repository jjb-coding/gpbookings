package ui.container;

public interface IDisplay {
	public Node getNode();
	
	public default Object inject(InjectableEnum injectable) {
		return (Object)getNode().inject(injectable);
	}
	
	public default void swapChild(IDisplay child) {
		throw new RuntimeException("IDisplay[" + this.getClass().getSimpleName() + "]: Has no container.");
	};
}
