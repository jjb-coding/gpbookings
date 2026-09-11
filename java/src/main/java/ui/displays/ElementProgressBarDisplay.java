package ui.displays;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import ui.container.IDisplay;
import ui.container.InjectableEnum;
import ui.container.Node;
import ui.controllers.ElementProgressBarController;
import ui.registry.ColourRegistry;

public class ElementProgressBarDisplay extends JPanel
	implements IDisplay {
	// The serial version ID.
	private static final long serialVersionUID = 1L;

	// Fields
	Node _parent;
	
	// Components
	JPanel child;
	
	public ElementProgressBarDisplay(Node _parent) {
		// Super
		super(new BorderLayout(0, 0));
		// Set parent
		this._parent = _parent;
		// Inject
		ElementProgressBarController _controller = (ElementProgressBarController)inject(InjectableEnum.PROGRESSBAR_CONTROLLER);
		
		// *** UI				
		// Logout
		JProgressBar progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(false);
		progressBar.setForeground(ColourRegistry.PROGRESS_BAR.getColour());
		progressBar.setBorderPainted(false);
		progressBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		progressBar.setPreferredSize(new Dimension(0, 20));

		// Containerise & add
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ColourRegistry.APP_BACKGROUND.getColour());
        topPanel.add(progressBar, BorderLayout.CENTER);
		add(topPanel, BorderLayout.NORTH);

		// * THIS
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(false);
		
		// *** ATTACH
		_controller.attach(
				progressBar);
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
	    
	    // Add
 		add((JPanel)child, BorderLayout.CENTER);
	};
}
