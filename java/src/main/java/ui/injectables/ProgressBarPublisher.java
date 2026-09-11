package ui.injectables;

import java.util.Stack;

import ui.container.AppContainer;
import ui.container.Base;
import ui.container.InjectableEnum;
import ui.container.InjectableObject;
import ui.controllers.ElementProgressBarController;

public class ProgressBarPublisher extends InjectableObject {
	// *** FIELDS
	ElementProgressBarController progressBar;
	Stack<Float> stack;

	// *** CONSTRUCTORS
	public ProgressBarPublisher(AppContainer _appContainer, Base parent) {
		super(_appContainer, parent);
		stack = new Stack<>();
	}
	
	@Override
	public void initialise() {
		progressBar = (ElementProgressBarController)inject(InjectableEnum.PROGRESSBAR_CONTROLLER);
	}

	// *** PUBLIC METHODS
	/**
	 * Sends a progress change, within the current interval.
	 * @param progress	In the interval of 0.0 to 1.0.
	 */
	public void send(float progress) {
		sendAndGet(progress);
	}
	
	private float sendAndGet(float progress) {
		// Clamp the value
		if (progress > 1.0f)
			progress = 1.0f;
		if (progress < 0.0f)
			progress = 0.0f;
		
		// Get the current interval being used
		float start;
		if (stack.size() == 0)
			// Default (0.0,1.0)
			start = 0.0f;
		else
			start = stack.get(stack.size() - 1);
			
		// Map
		progress = start + ((1.0f - start) * progress);
		
		// Send to the progress bar controller
		progressBar.setProgress(progress);
		
		// Return transformed value
		return progress;
	}
	
	/**
	 * Updates the progress bar and maps future
	 * sends into the remaining region.
	 * @param start
	 */
	public void push(float start) {
		stack.push(sendAndGet(start));
	}
	
	/**
	 * Unrolls a single layer of mapping.
	 */
	public void pop() {
		stack.pop();
	}
	
	/**
	 * Resets the progress bar and stack.
	 */
	public void reset() {
		stack.empty();
		progressBar.setProgress(0);
	}
}
