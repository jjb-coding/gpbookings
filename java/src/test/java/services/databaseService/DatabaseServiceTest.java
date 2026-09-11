package services.databaseService;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

public class DatabaseServiceTest {
	/**
	 * Confirms that the service was constructed properly.
	 */
	@Test
	public void ConstructsProperly() {
		new DatabaseService();
	}
	
	/**
	 * Confirms that a generic callable statement can be retrieved.
	 */
	@Test
	public void CanGetConnection() throws SQLException {
		DatabaseService service = new DatabaseService();
		service.getCallableStatement("call API_Patient_Public_DownloadTitles()");
	}
	
	/**
	 * Confirms that the database service's connections can be closed.
	 */
	@Test
	public void CanCloseConnection() throws SQLException {
		DatabaseService service = new DatabaseService();
		service.close();
	}
}
