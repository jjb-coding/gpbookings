package services.loggerService;

import java.time.LocalDateTime;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;

import services.fileService.FileService;
import services.fileService.FileServiceSingleton;
import services.fileService.Paths;

public class LoggerService {
	// 
	FileService _fileService = FileServiceSingleton.INSTANCE.get();
	FileWriter writer;
	int i;
	
	/**
	 * 
	 * @throws Exception
	 */
	public LoggerService() {
		//
		i = 1;
		//
		LocalDateTime now = java.time.LocalDateTime.now();
		File file = _fileService.getFile(Paths.LOGS, "log-" + now.toString().replace(':', '-'));
		if (file.exists() || file.isDirectory())
			throw new RuntimeException("LOGGER:init: Log file already exists.");
		try {
		Files.createDirectories(file.toPath().getParent());
		}
		catch (Exception e) {
		}
		try {
			file.createNewFile();
		}
		catch (Exception e) {
			throw new RuntimeException("LOGGER:init: Couldn't create file.");
		}
		try {
			writer = new FileWriter(file);
			writer.write("<log>");
			writer.write("	<metadata>");
			writer.write("		<time>" + now.toString() + "</time>");
			writer.write("	</metadata>");
			writer.write("	<entries>");
		}
		catch (Exception e) {
			throw new RuntimeException("LOGGER:init: Couldn't write initial file structure.");
		}
	}
	
	/**
	 * 
	 * @param entry
	 */
	public void log(String entry) {
		try {
			writer.write("		<entry>");
			LocalDateTime now = java.time.LocalDateTime.now();
			writer.write("			<index>" + String.valueOf(i++) + "</index>");
			writer.write("			<time>" + now.toString() + "</time>");
			writer.write("			<message>" + entry + "</message>");
			writer.write("		</entry>");
		}
		catch (Exception e) {}
	}
	
	/**
	 * 
	 */
	public void dispose() {
		try {
			writer.write("	</entries>");
			writer.write("</log>");
			writer.close();
		}
		catch (Exception e) {}
	}
}
