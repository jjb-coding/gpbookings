package services.fileService;

import java.nio.file.Path;
import java.util.EnumMap;

public enum Directories {
	// --------------------
	USER			(true, null),
	INSTALLATION	(true, null),
	BUILTIN			(false, null),
	PRACTICE		(false, null),
	INTERFACE		(true, new VirtualisationPair<Directories>(Directories.BUILTIN, Directories.PRACTICE))
	;
		
	// --------------------
	// Fields
	boolean isAccessible;
	VirtualisationPair<Directories> pair;
	
	/**
	 * Constructs a Directory instance.
	 * @param isAccessible	False if it is a child of a virtual directory.
	 * @param pair			May be null. The pair of directories that this directory represents.
	 */
	Directories(boolean isAccessible, VirtualisationPair<Directories> pair) {
		this.isAccessible = isAccessible;
		this.pair = pair;
	}
	
	/**
	 * Whether the directory is virtual.
	 * @return	The boolean return
	 */
	public boolean isVirtual() {
		return (pair != null);
	}
	
	/**
	 * Whether the directory can be directly accessed,
	 * or if it is a child of a virtual directory.
	 * @return	The boolean return
	 */
	public boolean isAccessible() {
		return isAccessible;
	}
	
	/**
	 * Set the path of this directory
	 * @param path	The path
	 */
	public void setPath(Path path) {
		paths.put(this, path);
	}
	
	/**
	 * Gets the path of this directory.
	 * @return	The path
	 */
	public Path getPath() {
		return paths.get(this);
	}
	
	/**
	 * Gets the path of this directory with another
	 * path appended to it.
	 * @param path	The appended path
	 * @return		The complete path
	 */
	public Path getPathQualified(Path path) {
		return paths.get(this).resolve(path);
	}

	/**
	 * Gets a pair for each true directory and corresponding
	 * path this virtual directory represents.
	 * @return	The pair of paths
	 */
	public VirtualisationPair<Path> getPaths() {
		return new VirtualisationPair<Path>(pair.a().getPath(), pair.b().getPath());
	}
	
	/**
	 * Gets a pair for each true directory and corresponding
	 * path this virtual directory represents, each with the same
	 * appended path modifier.
	 * @param path	The appended path
	 * @return		The pair of paths
	 */
	public VirtualisationPair<Path> getPathsQualified(Path path) {
		return new VirtualisationPair<Path>(pair.a().getPath().resolve(path), pair.b().getPath().resolve(path));
	}
	
	/**
	 * Sets the initial branch of a virtualisation pair based
	 * on their names. 
	 * @param base
	 */
	public void buildLocalPaths(Path base) {
		pair.a().setPath(base.resolve(pair.a().name()));
		pair.b().setPath(base.resolve(pair.b().name()));
	}
	
	// *** STATIC
	static final EnumMap<Directories,Path> paths;
	static {
		paths = new EnumMap<>(Directories.class);
	}
}
