package entry;
import static javax.swing.JOptionPane.showMessageDialog;

import javax.swing.UIManager;

import status.Status;

import services.registryService.*;
import ui.registry.*;

import services.mapperService.*;
import services.apiService.*;
import services.configService.*;
import services.databaseService.*;
import services.fileService.*;
import services.loggerService.*;
import ui.container.AppContainer;
import services.practiceProvider.*;

/**
 * Entry point. Handles resource loading & then calls into UI.
 */
public class Main {
	static FileService _fileService = null;
	static LoggerService _loggerService = null;
	static ConfigService _configService = null;
	static DatabaseService _databaseService = null;
	static APIService _apiService = null;
	static MapperService _mapperService = null;
	static PracticeProvider _practiceProvider = null;
	static RegistryService _registryService = null;
	static AppContainer _appContainer = null;
	
	/**
	 * Posts an error during initialisation, before the logger comes online.
	 * @param e		The error.
	 */
	private static void initialisationError(RuntimeException e) {
		e.printStackTrace();
		showMessageDialog(null, "There was an error starting up. Error:\n" + e.toString());
		exit();
	}

	/**
	 * Posts an error during initialisation, with the logger.
	 * @param _loggerService	The logger service.
	 * @param e					The error.
	 */
	private static void initialisationError(LoggerService _loggerService, RuntimeException e) {
		e.printStackTrace();
		_loggerService.log(e.toString());
		showMessageDialog(null, "There was an error starting up.");
		exit();
	}
	
	/**
	 * Shows a serious error that is not fatal.
	 * @param status	The status.
	 */
	public static void showNonFatalError(Status status) {
		showMessageDialog(null, status.getText());
	}
	
	/**
	 * Exit with some status.
	 * @param status	The status.
	 */
	public static void exit(Status status) {
		showMessageDialog(null, status.getText());
		exit();
	}
	

	/**
	 * Exit correctly, closing the logger.
	 */
	public static void exit() {
		_loggerService.dispose();
		_databaseService.close();
		System.exit(0);
	}
	
    public static void main(String[] args) {
    	// start FileService -> reported to terminal
    	try {
    		_fileService = FileServiceSingleton.INSTANCE.get();
    	}
    	catch (RuntimeException e) {
    		initialisationError(e);
    	}
    	
    	// start LoggerService -> message box
    	try {
			_loggerService = LoggerServiceSingleton.INSTANCE.get();
    	}
    	catch (RuntimeException e) {
    		initialisationError(_loggerService, e);
    	}

    	// start ConfigService -> logged
    	try {
    		_configService = ConfigServiceSingleton.INSTANCE.get();
    	}
    	catch (RuntimeException e) {
    		initialisationError(_loggerService, e);
    	}

    	// start DatabaseService
    	try {
    		_databaseService = DatabaseServiceSingleton.INSTANCE.get();
    	}
    	catch (RuntimeException e) {
    		initialisationError(_loggerService, e);
    	}
    	    	
    	// start APIService
    	try {
    		_apiService = APIServiceSingleton.INSTANCE.get();
    	}
    	catch (RuntimeException e) {
    		initialisationError(_loggerService, e);
    	}
    	
    	// build APIService
    	try {
    		_apiService.build();
    	}
    	catch (RuntimeException e) {
    		initialisationError(_loggerService, e);
    	}
    	
    	// start MapperService
    	try {
    		_mapperService = MapperServiceSingleton.INSTANCE.get();
    	}
    	catch (RuntimeException e) {
    		initialisationError(_loggerService, e);
    	}
    	
    	// Fetch latest update
    	// _fileService.fetchLatestUpdate();
    	
    	// start PracticeProvider -> logged
    	try {
    		_practiceProvider = PracticeProviderSingleton.INSTANCE.get();
    	}
    	catch (RuntimeException e) {
    		initialisationError(_loggerService, e);
    	}
    	
    	// start RegistryService -> logged
    	try {
    		_registryService = RegistryServiceSingleton.INSTANCE.get();
    	}
    	catch (RuntimeException e) {
    		initialisationError(_loggerService, e);
    	}

    	// then initialise registries
    	initialiseRegistries();
    	
    	// start AppContainer -> logged
    	try {
    		_appContainer = new AppContainer();
    	}
    	catch (RuntimeException e) {
    		initialisationError(_loggerService, e);
    	}
        
        // Apply look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
        // launch: Welcome
    	javax.swing.SwingUtilities.invokeLater(() -> _appContainer.start(Status.WELCOME));
    }
    
    private static void initialiseRegistries() {
    	try {
    		Class.forName(ColourRegistry.class.getName());
    		Class.forName(TextRegistry.class.getName());
    		Class.forName(TextStyleRegistry.class.getName());
    		Class.forName(FontRegistry.class.getName());
    		Class.forName(ImageRegistry.class.getName());
    	}
    	catch (ClassNotFoundException e) {
    		System.out.println("Class not found: " + e.toString());
    		System.exit(0);
    	}
    	catch (RuntimeException e) {
    		initialisationError(_loggerService, e);
    	}
    }
}