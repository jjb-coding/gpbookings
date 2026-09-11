package ui.injectables;

import ui.container.AppContainer;
import ui.container.Base;
import ui.container.InjectableObject;

public class LogInData extends InjectableObject {
	// *** FIELDS
	String accountUUID;
	String sessionToken;
	String securityEmail;
	
	// *** CONSTRUCTORS
	public LogInData(AppContainer _appContainer, Base parent) {
		super(_appContainer, parent);
	}
	
	// *** PUBLIC METHODS
	public void configure(String securityEmail, String accountUUID, String sessionToken) {
		this.securityEmail = securityEmail;
		this.accountUUID = accountUUID;
		this.sessionToken = sessionToken;
	}
	
	public String getSecurityEmail() {
		return securityEmail;
	}
}
