package services.authService;

/**
 * Service used by API endpoint classes & UI.
 */
public class AuthService {
	// Contains authentication credentials.
	AuthCredentials authCredentials;
	
	/**
	 * Determines whether the user is logged in or not.
	 * @return	Whether credentials exist.
	 */
	public boolean isLoggedIn() {
		return (authCredentials != null);
	}
	
	/**
	 * Retrieves the login credentials. Used by the APIService.
	 * @return
	 */
	public AuthCredentials getAuthCredentials() {
		return authCredentials;
	}
	
	/**
	 * Logs the user in.
	 * @param credentials	Their login credentials.
	 */
	public void logIn(AuthCredentials credentials) {
		authCredentials = credentials;
	}
	
	/**
	 * Logs the user out.
	 */
	public void logOut() {
		authCredentials = null;
	}
}