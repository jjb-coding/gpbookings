package ui.container;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import exceptions.DatabaseException;
import exceptions.NoConnectionException;
import status.Status;

public abstract class InjectableObject extends Base {
	// *** CONSTRUCTORS
	/**
	 * Constructs an InjectableObject instance.
	 * @param _appContainer	The app container.
	 * @param parent		The parent.
	 */
	public InjectableObject(AppContainer _appContainer, Base parent) {
		super(_appContainer, parent);
	}

	// *** PUBLIC METHODS	
	/**
	 * Fetches an instance of an injection object by enum.
	 * Injection objects do not themselves scope or provide injections,
	 * so this just passes the call along to the parent recursively.
	 */
	@Override
	public Object inject(InjectableEnum injectable) {
		return parentInject(injectable);
	};
	
	/**
	 * Injection objects do not consume Status, so this just
	 * passes the call along to the parent recursively.
	 */
	public void setStatus(Status status) {
		setParentStatus(status);
	}
	
	/**
	 * Sets the status after some period of time.
	 */
	public void setStatusAfterWait(int milliseconds, Status status) {
        Timer timer = new Timer(milliseconds, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                ((Timer)e.getSource()).stop();
				setStatus(status);			
			}
        });
        timer.start();
	}
	
	/**
	 * Sets the status after some period of time.
	 */
	public void setStatusAfterWait(int milliseconds, Status status, ViewBag bag) {
        Timer timer = new Timer(milliseconds, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                ((Timer)e.getSource()).stop();
				setStatus(status, bag);			
			}
        });
        timer.start();
	}
	
	/**
	 * Injection objects do not consume Status, so this just
	 * passes the call along to the parent recursively.
	 */
	public void setStatus(Status status, ViewBag bag) {
		setParentStatus(status, bag);
	}
	
	/**
	 * Assigns the parent if assignation was deferred i.e. for
	 * a ViewBag object. Use of the object before this may
	 * result in program failure.
	 * @param parent	The parent.
	 */
	public void lateAssignParent(Node parent) {
		this.parent = parent;
	}
	
	/**
	 * Determines if the object has been assigned a parent.
	 * @return			Whether it has been assigned a parent
	 */
	public boolean hasParent() {
		return (this.parent != null);
	}
	
	/**
	 * Designed to be overriden if there is a need for
	 * post-constructor initialisation. Post-constructor initialisation
	 * is necessary for injectables that take other injectables.
	 */
	public void initialise() {}
	
	// *** UTILITY METHODS
	/**
	 * Handles generic API status issues, like session timeout.
	 * @param status
	 */
	public boolean isBadStatus(Status status) {
		if (status == Status.INVALID_CREDENTIALS || status == Status.SESSION_TIMEOUT) {
			entry.Main.showNonFatalError(status);
			setStatus(Status.LOG_IN);
			return true;
		}
		return false;
	}
	
	/**
	 * Handles generic API error conditions.
	 * @param e
	 */
	public void handleError(RuntimeException e) {
		if (e instanceof DatabaseException)
			entry.Main.exit(Status.SQL_ERROR);
		else if (e instanceof NoConnectionException)
			entry.Main.exit(Status.SQL_NO_CONNECTION);
	}
}
