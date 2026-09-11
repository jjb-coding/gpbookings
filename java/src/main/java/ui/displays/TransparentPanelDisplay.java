package ui.displays;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import ui.container.IDisplay;
import ui.container.Node;

public class TransparentPanelDisplay extends JPanel
	implements IDisplay {
	// The serial version ID.
	private static final long serialVersionUID = 1L;
	
	// Fields
	Node _parent;
	
	// Components
	JPanel child;
	
	public TransparentPanelDisplay(Node _parent) {
		// Super
		super(new BorderLayout());
		// Set parent
		this._parent = _parent;
		
		// Configure
		setOpaque(false);
	}

	@Override
	public Node getNode() {
		return _parent;
	}

	@Override
	public void swapChild(IDisplay child) {
	    // Clear
		for (Component component : getComponents())
	        if (BorderLayout.CENTER.equals(((BorderLayout)getLayout()).getConstraints(component)))
	            remove(component);
	    
	    // Swap
 		add((JPanel)child, BorderLayout.CENTER);
	};
}
