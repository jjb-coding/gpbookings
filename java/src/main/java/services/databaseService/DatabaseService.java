package services.databaseService;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import services.configService.ConfigService;
import services.configService.ConfigServiceSingleton;

public class DatabaseService {
	// Singletons
	ConfigService _configService = ConfigServiceSingleton.INSTANCE.get();
	
	// Fields
	Connection connection;
    private String location;
    Properties properties;
    
    // *** CONSTRUCTORS
    /**
     * Starts the database service.
     */
    public DatabaseService() {
    	location = _configService.getDatabaseLocation();
    	
        properties = new Properties();
        properties.setProperty("user", _configService.getDatabaseUsername());
        properties.setProperty("password", _configService.getDatabasePassword());
        properties.setProperty("connectTimeout", "10000");
        properties.setProperty("socketTimeout", "30000");
        
        connect();
    }
    
    // *** PUBLIC METHODS
    /**
     * Gets a callable statement formatted by a string.
     * @param string			The call string.
     * @return					The callable statement.
     * @throws SQLException		If the call string format is malformed.
     */
    public CallableStatement getCallableStatement(String string) throws SQLException {
    	return getConnection().prepareCall(string);
    }
    
    /**
     * Closes the connection.
     */
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // *** PRIVATE METHODS
    private Connection getConnection() {
        if (connection == null || !isConnectionValid()) connect();
        return connection;
    }
    
    public void closeConnection() {
    	close();
    }
    
    private void connect() {
        try {            
            connection = DriverManager.getConnection(location, properties);
        } catch (SQLException e) {
            throw new RuntimeException("DATABASE: Could not connect to database.");
        }
    }
    
    private boolean isConnectionValid() {
        try {
            return connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
}
