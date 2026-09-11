package services.fileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import util.StringUtil;

public class FileService {
	// Fields
	DocumentBuilder builder;
	
	/**
	 * Starts the FileService.
	 */
	public FileService() {
		// Get app path & app data path
		Path appPath = FileSystems.getDefault().getPath("").toAbsolutePath();
		Path appDataPath;
		try { 
			appDataPath = Path.of(System.getenv("APPDATA"));
		}
		catch (Exception e) {
			appDataPath = appPath.resolve("user");
		}
		
		// Build directories
		Directories.INTERFACE.buildLocalPaths(appPath.resolve("resources/content"));
		Directories.INSTALLATION.setPath(appPath.resolve("resources/data"));
		Directories.USER.setPath(appDataPath.resolve("groupakent"));

		// Get XML builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		}
		catch (Exception e) {
			throw new RuntimeException("FILE:init: Couldn't get document builder");
		}
	}
	
	/**
	public void fetchLatestUpdate() {
		ConfigService _configService = ConfigServiceSingleton.INSTANCE.get();
	}
	*/
	
	/**
	 * Gets a file.
	 * @param path			The paths type.
	 * @param parameter		The parameter for the paths resolution. May be null.
	 * @return
	 */
	public File getFile(Paths path, String parameter) {
		// VALIDATE: is non-virtual?
		if (path.getDirectory().isVirtual())
			throw new RuntimeException("FILE: Tried to access a virtual file conventionally.");
		
		// Locate resource
		String lookup = StringUtil.unNull(parameter) + path.getSuffix();
		Path relPath = path.getRelativePath();
		Path pathQualified;
		if (relPath == null)
			pathQualified = path.getDirectory().getPath().resolve(lookup);
		else
			pathQualified = path.getDirectory().getPathQualified(relPath.resolve(lookup));
		
		// Return
		return pathQualified.toFile();
	}
	
	/**
	 * Gets the first file found over a virtual folder.
	 * @param path			The Paths entry.
	 * @param parameter		The parameter for the paths resolution. May be null.
	 * @return				One file.
	 */
	public File getVirtualFileOne(Paths path, String parameter) {
		// VALIDATE: is virtual?
		if (!path.getDirectory().isVirtual())
			throw new RuntimeException("FileService:runtime: Tried to get a file virtually from a non-virtual Paths entry.");
		
		// Get virtual paths
		parameter = StringUtil.unNull(parameter);
		VirtualisationPair<Path> paths = path.getDirectory().getPathsQualified(path.getRelativePath().resolve(parameter + path.getSuffix()));
				
		// Find the first file
		File aFile = paths.a().toFile();
		if (aFile.exists() && !aFile.isDirectory())
			return aFile;
		else
			return paths.b().toFile();
	}
	
	/**
	 * Gets both files found over a virtual folder.
	 * @param path			The Paths entry.
	 * @param parameter		The parameter for the paths resolution. May be null.
	 * @return				Both files.
	 */
	public VirtualisationPair<File> getVirtualFileBoth(Paths path, String parameter) {
		//VALIDATE: is virtual?
		if (!path.getDirectory().isVirtual())
			throw new RuntimeException("FileService:runtime: Tried to get both files virtually from a non-virtual Paths entry.");
		
		// Get virtual paths
		parameter = StringUtil.unNull(parameter);
		VirtualisationPair<Path> paths = path.getDirectory().getPathsQualified(path.getRelativePath().resolve(parameter + path.getSuffix()));
		
		// Find the first file
		return new VirtualisationPair<File>(
			paths.a().toFile(),
			paths.b().toFile()
			);
	}
	
	/**
	 * Gets both documents found over a virtual folder.
	 * @param path			The Paths entry.
	 * @param parameter		The parameter for the paths resolution. May be null.
	 * @return				Both documents.
	 * @throws SAXException	XML error.
	 * @throws IOException	File error.
	 */
	public VirtualisationPair<Element> getVirtualDocumentBoth(Paths path, String parameter) throws SAXException, IOException {
		VirtualisationPair<File> files = getVirtualFileBoth(path, parameter);
		return new VirtualisationPair<Element>(
			builder.parse(files.a()).getDocumentElement(),
			builder.parse(files.b()).getDocumentElement()
			);
	}
	
	/**
	 * Gets one document from a non-virtual folder.
	 * @param path			The Paths entry.
	 * @param parameter		The parameter for the paths resolution. May be null.
	 * @return				The document.
	 * @throws SAXException	XML error.
	 * @throws IOException	File error.
	 */
	public Element getDocumentByRoot(Paths path, String parameter) throws SAXException, IOException {
		File file = getFile(path, parameter);
		return builder.parse(file).getDocumentElement();
	}
}
