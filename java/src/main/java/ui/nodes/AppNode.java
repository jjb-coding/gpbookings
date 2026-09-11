package ui.nodes;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import status.Status;
import ui.container.AppContainer;
import ui.container.IDisplay;
import ui.container.InjectableEnum;
import ui.container.Node;
import ui.container.ViewBag;
import ui.displays.TransparentFrameDisplay;

public class AppNode extends Node {	
	public AppNode(AppContainer _appContainer, Node parent, ViewBag bag) {
		super(_appContainer, parent, bag);
		display = (IDisplay)new TransparentFrameDisplay(this);

		// Cast to JFrame
		JFrame frame = (JFrame)display;
		frame.setResizable(false);
		frame.setSize(600,760);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
            	entry.Main.exit();
            }
        });
        
        // Setup backspace detection
        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = frame.getRootPane().getActionMap();
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "BACK_EVENT");
        actionMap.put("BACK_EVENT", new AbstractAction() {
            // The serial version ID.
			private static final long serialVersionUID = 1L;

			@Override
            public void actionPerformed(ActionEvent e) {
				sendBack();
            }
        });
        
        frame.setVisible(true);
	}

	@Override
	public String getTitleContribution() {
		return "Medical Centre";
	}

	@Override
	public EnumSet<InjectableEnum> getInjectables() {
		return null;
	}
	
	@Override
	public void refresh(ArrayList<String> stringList) {
		// Cast to JFrame
		JFrame frame = (JFrame)display;
		
		// Build & set title
		stringList.add(getTitleContribution());
		ArrayList<String> reversed = new ArrayList<String>();
		for (int i = (stringList.size() - 1); i >= 0; i--)
			reversed.add(stringList.get(i));
		frame.setTitle(String.join(" | ", reversed));
		
	    // Redraw
	    frame.validate();
	    frame.repaint();
	}

	
	/**
	 * Catches a request to go back. Takes the user 
	 * back a page, unless they are on WELCOME.
	 */
	@Override
	public void sendBack() {
		// Get the leaf status
		Node leaf = getLeaf();
		Status leafStatus = _appContainer.getStatus(getLeaf().getClass());
		
		// Map
		Status back = backMap.get(leafStatus);
		if (back == null)
			throw new RuntimeException("APP:node: Couldn't go back from " + leafStatus.name());
		
		// Transition
		leaf.setStatus(back);
	}
	
	static final EnumMap<Status, Status> backMap;
	static {
		backMap = new EnumMap<>(Status.class);
	    backMap.put(Status.WELCOME, null);
	    backMap.put(Status.LOG_IN, Status.WELCOME);
	    backMap.put(Status.NEW_FIRST, Status.WELCOME);
	    backMap.put(Status.NEW_SECOND, Status.NEW_FIRST);
	    backMap.put(Status.DASHBOARD, Status.WELCOME);
	    backMap.put(Status.VIEW_BOOKINGS, Status.DASHBOARD);
	    backMap.put(Status.RESCHEDULE_BOOKING, Status.VIEW_BOOKINGS);
	    backMap.put(Status.VIEW_ATTENDED_BOOKING, Status.VIEW_BOOKINGS);
	    backMap.put(Status.MAKE_BOOKING, Status.DASHBOARD);
	    backMap.put(Status.UPDATE_DETAILS, Status.DASHBOARD);
	}
}
