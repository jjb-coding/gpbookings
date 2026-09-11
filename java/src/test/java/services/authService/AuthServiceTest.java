package services.authService;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

public class AuthServiceTest {
	/**
	 * Confirms that the service was constructed properly.
	 */
	@Test
	public void ConstructsProperly() {
		new AuthService();
	}
	
	/**
	 * Confirms that the user can log in.
	 */
	@Test
	public void CanDetectLogin() throws SQLException {
		AuthService authService = new AuthService();
		
		authService.logIn(new AuthCredentials("", ""));
		
		assertTrue(authService.isLoggedIn());
	}
	
	/**
	 * Confirms that the user can log in and out.
	 */
	@Test
	public void CanLogInAndOut() throws SQLException {
		AuthService authService = new AuthService();
		
		authService.logIn(new AuthCredentials("", ""));
		
		assertTrue(authService.isLoggedIn());
		
		authService.logOut();
		
		assertFalse(authService.isLoggedIn());
	}
	
	/**
	 * Confirms that logging in sets the credentials correctly.
	 */
	@Test
	public void SetsCredentialsCorrecly() throws SQLException {
		AuthService authService = new AuthService();
		
		authService.logIn(new AuthCredentials("ACCOUNT", "SESSION"));
		
		assertTrue(
				authService.getAuthCredentials().accountUUID().equals("ACCOUNT")
				);
		assertTrue(
				authService.getAuthCredentials().sessionToken().equals("SESSION")
				);
	}
	
	/**
	 * Confirms that logging in sets the credentials correctly.
	 */
	@Test
	public void ClearsCredentialsCorrectly() throws SQLException {
		AuthService authService = new AuthService();
		
		authService.logIn(new AuthCredentials("ACCOUNT", "SESSION"));
		
		assertTrue(
				authService.getAuthCredentials().accountUUID().equals("ACCOUNT")
				);
		assertTrue(
				authService.getAuthCredentials().sessionToken().equals("SESSION")
				);
		
		authService.logOut();
		
		assertNull(authService.getAuthCredentials());
	}
}
