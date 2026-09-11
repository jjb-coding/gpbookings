package services.configService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConfigServiceTest {
	/**
	 * Confirms that the service was constructed properly.
	 */
	@Test
	public void ConstructsProperly() {
		new ConfigService();
	}
	
	/**
	 * Confirms that all properties were loaded properly.
	 */
	@Test
	public void NoPropertiesAreNull() {
		ConfigService service = new ConfigService();
		
		Assertions.assertNotNull(service.getLanguage());
		Assertions.assertNotNull(service.getDatabaseUsername());
		Assertions.assertNotNull(service.getDatabasePassword());
		Assertions.assertNotNull(service.getDatabaseLocation());
	}
	
}
