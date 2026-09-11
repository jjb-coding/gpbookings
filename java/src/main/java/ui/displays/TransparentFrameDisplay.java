package ui.displays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ui.container.IDisplay;
import ui.container.Node;

public class TransparentFrameDisplay extends JFrame
	implements IDisplay {
	// The serial version ID.
	private static final long serialVersionUID = 1L;
	
	// Fields
	Node _parent;
	
	// Components
	JPanel child;
	
	public TransparentFrameDisplay(Node _parent) {
		this._parent = _parent;
	}

	@Override
	public Node getNode() {
		return _parent;
	}

	@Override
	public void swapChild(IDisplay child) {
	    // Clear
	    getContentPane().removeAll();
	    
	    // Swap
	    this.child = (JPanel)child;
	    this.getContentPane().add(this.child);
	};
}
