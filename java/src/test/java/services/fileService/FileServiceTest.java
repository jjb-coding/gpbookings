package services.fileService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class FileServiceTest {
	/**
	 * Confirms that the service was constructed properly.
	 */
	@Test
	public void ConstructsProperly() {
		new FileService();
	}
	
	/**
	 * Confirms that the file service is correctly locating
	 * generic files i.e. colours.
	 */
	@Test
	public void GetsOneFileFromVirtualFolder() throws SQLException, SAXException, IOException {
		FileService service = new FileService();
		
		File file = service.getVirtualFileOne(Paths.COLOURS, null);

		Assertions.assertNotNull(file);
	}
	
	/**
	 * Confirms that the file service is correctly locating
	 * generic files i.e. colours.
	 */
	@Test
	public void GetsGenericVirtualDocuments() throws SQLException, SAXException, IOException {
		FileService service = new FileService();
		
		VirtualisationPair<Element> pair = service.getVirtualDocumentBoth(Paths.COLOURS, null);
		Assertions.assertNotNull(pair.a());
		Assertions.assertNotNull(pair.b());
		
		pair = service.getVirtualDocumentBoth(Paths.TEXT_STYLES, null);
		Assertions.assertNotNull(pair.a());
		Assertions.assertNotNull(pair.b());
		
		pair = service.getVirtualDocumentBoth(Paths.STATUSES, null);
		Assertions.assertNotNull(pair.a());
		Assertions.assertNotNull(pair.b());
	}
	
	/**
	 * Confirms that a virtual directory cannot be accessed physically.
	 * generic files i.e. colours.
	 */
	@Test
	public void IsGuardingVirtual() throws SQLException, SAXException, IOException {
		FileService service = new FileService();
		
		try {
			service.getDocumentByRoot(Paths.COLOURS, null);
		}
		catch (Exception e) {
			return;
		}
		Assertions.fail();
	}
}
