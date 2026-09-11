package services.apiService;

import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.APIInput;
import api.APIOutput;
import api.APIOutputResult;
import exceptions.DatabaseException;
import exceptions.NoConnectionException;
import services.authService.AuthCredentials;
import services.authService.AuthService;
import services.authService.AuthServiceSingleton;
import services.databaseService.DatabaseService;
import services.databaseService.DatabaseServiceSingleton;
import status.Status;
import util.ReflectionUtil;

import java.sql.ResultSet;

/**
 * Facilitates API calls.
 * Does not need to be accessed directly outside of the API package.
 */
public class APIService {
	// Mapper & configurer registries
	Map<Class<?>, InputMapper> classToInputMapper;
	Map<Class<?>, Integer> classToOutputConfigurer;
	Map<Class<?>, OutputMapper> classToOutputMapper;
	Map<Class<?>, OutputResultsMapper> classToOutputResultsMapper;
	
	// Input class registry
	final Map<Class<?>, InputConfiguration> registry;
	
	/**
	 * An API service instance contains read-only mapper registries, and a
	 * registry of scanned API endpoints.
	 */
	public APIService() {
		// Initialise input mapper map
		constructInputMapperRegistry();
		
		// Initialise output configurer map
		constructOutputConfigurerRegistry();
		
		// Initialise output mapper map
		constructOutputMapperRegistry();
		
		// Initialise output results mapper map
		constructOutputResultsMapperRegistry();
		
		// Initialise registry
		registry = new HashMap<>();
	}
	
	public void build() {
		// Scan
		ArrayList<Class<?>> inputs;
		try {
			inputs = ReflectionUtil.getClasses("api.input");
		} catch (Exception e) {
			throw new RuntimeException("API:init: Couldn't get classes.");
		}
		
		// Configure
		for (Class<?> input : inputs)
			registry.put(input, new InputConfiguration(input));
	}
	
	private void constructInputMapperRegistry() {
		// Construct map
		classToInputMapper = new HashMap<>();
		
		// Supply entries
		classToInputMapper.put(Integer.class, new InputMapper() {
			@Override
			public void invoke(int i, Object object, CallableStatement statement) throws SQLException {
				statement.setInt(i, (int)object);
			}
		});
		classToInputMapper.put(Long.class, new InputMapper() {
			@Override
			public void invoke(int i, Object object, CallableStatement statement) throws SQLException {
				statement.setLong(i, (Long)object);
			}
		});
		classToInputMapper.put(String.class, new InputMapper() {
			@Override
			public void invoke(int i, Object object, CallableStatement statement) throws SQLException {
				statement.setString(i, (String)object);
			}
		});
		classToInputMapper.put(byte[].class, new InputMapper() {
			@Override
			public void invoke(int i, Object object, CallableStatement statement) throws SQLException {
				statement.setBytes(i, (byte[])object);
			}
		});
		classToInputMapper.put(Timestamp.class, new InputMapper() {
			@Override
			public void invoke(int i, Object object, CallableStatement statement) throws SQLException {
				statement.setTimestamp(i, (Timestamp)object);
			}
		});
		classToInputMapper.put(Date.class, new InputMapper() {
			@Override
			public void invoke(int i, Object object, CallableStatement statement) throws SQLException {
				statement.setDate(i, (Date)object);
			}
		});
	}
	
	private void constructOutputConfigurerRegistry() {
		// Construct map
		classToOutputConfigurer = new HashMap<>();
		
		// Supply entries
		classToOutputConfigurer.put(
				Status.class,
				Types.VARCHAR);
		classToOutputConfigurer.put(
				Integer.class,
				Types.INTEGER);
		classToOutputConfigurer.put(
				Integer.class,
				Types.BIGINT);
		classToOutputConfigurer.put(
				String.class, 
				Types.VARCHAR);
		classToOutputConfigurer.put(
				byte[].class, 
				Types.VARBINARY);
		classToOutputConfigurer.put(
				Timestamp.class,
				Types.TIMESTAMP);
		classToOutputConfigurer.put(
				Date.class,
				Types.DATE);
	}
	
	private void constructOutputMapperRegistry() {
		// Construct map
		classToOutputMapper = new HashMap<>();
		
		// Supply entries
		classToOutputMapper.put(Status.class, new OutputMapper() {
			@Override
			public Object invoke(int i, CallableStatement statement) throws SQLException {
				String name = statement.getString(i);
				return (Object)Status.fromName(name);
			}
		});
		classToOutputMapper.put(Long.class, new OutputMapper() {
			@Override
			public Object invoke(int i, CallableStatement statement) throws SQLException {
				return (Object)statement.getLong(i);
			}
		});
		classToOutputMapper.put(Integer.class, new OutputMapper() {
			@Override
			public Object invoke(int i, CallableStatement statement) throws SQLException {
				return (Object)statement.getInt(i);
			}
		});
		classToOutputMapper.put(String.class, new OutputMapper() {
			@Override
			public Object invoke(int i, CallableStatement statement) throws SQLException {
				return (Object)statement.getString(i);
			}
		});
		classToOutputMapper.put(byte[].class, new OutputMapper() {
			@Override
			public Object invoke(int i, CallableStatement statement) throws SQLException {
				return (Object)statement.getBytes(i);
			}
		});
		classToOutputMapper.put(Timestamp.class, new OutputMapper() {
			@Override
			public Object invoke(int i, CallableStatement statement) throws SQLException {
				return (Object)statement.getDate(i);
			}
		});
		classToOutputMapper.put(Date.class, new OutputMapper() {
			@Override
			public Object invoke(int i, CallableStatement statement) throws SQLException {
				return (Object)statement.getDate(i);
			}
		});
	}
	
	private void constructOutputResultsMapperRegistry() {
		// Construct map
		classToOutputResultsMapper = new HashMap<>();
		
		// Supply entries
		classToOutputResultsMapper.put(Integer.class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getInt(name);
			};
		});
		classToOutputResultsMapper.put(Long.class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getLong(name);
			};
		});
		classToOutputResultsMapper.put(String.class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getString(name);
			};
		});
		classToOutputResultsMapper.put(byte[].class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getBytes(name);
			};
		});
		classToOutputResultsMapper.put(Timestamp.class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getTimestamp(name);
			};
		});
		classToOutputResultsMapper.put(Date.class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getDate(name);
			};
		});
		classToOutputResultsMapper.put(Time.class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getTime(name);
			};
		});
		classToOutputResultsMapper.put(long.class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getLong(name);
			};
		});
		classToOutputResultsMapper.put(Boolean.class, new OutputResultsMapper() {
			@Override
			public Object invoke(String name, ResultSet resultSet) throws SQLException {
				return (Object)resultSet.getBoolean(name);
			};
		});
	}
	
	/**
	 * Calls an API endpoint over an input record endpoint. Passes along the
	 * request to the correct input configuration instance through the registry.
	 * @param object	The input record.
	 * @return			The output record.
	 */
	public Object execute(APIInput<?> object) {
		// Get the class of the runtime object
		Class<?> cls = object.getClass();

		// VALIDATE: is object type in registry?
		InputConfiguration input = registry.get(cls);
		if (input == null)
			throw new RuntimeException("API:refl: Attempted to execute API request on object of type " + cls.getSimpleName() + "which was not found in the registry.");
		
		// Pass along to InputConfiguration object
		return input.execute(object);
	}
}

/**
 * Classifies the information retrieved from scanning an input record.
 */
class InputConfiguration {
	// An array of mappers that take an object, cast it, and put it into a callable statement. 
	public Method[] accessors;
	// An array of accessors that can be invoked on an object, in order.
	public InputMapper[] mappers;
	// The configured output record.
	public OutputConfiguration outputConfiguration;
	// API name
	String apiName;
	// String to build callable statement
	String statementString;
	// Whether the call is authenticated or not
	boolean isAuthenticated;
	
	/**
	 * Scans an input record and builds a configuration object around it.
	 * @param cls	The record to scan.
	 */
	public InputConfiguration(Class<?> cls) {
		// Inject the APIService
		APIService _apiService = APIServiceSingleton.INSTANCE.get();
		
		// VALIDATE: is it a record?
	    if (!cls.isRecord())
	        throw new RuntimeException("API:refl:input[" + cls.getSimpleName() + "]: Is not a record class.");
	    
	    // Get components
	    RecordComponent[] fields = cls.getRecordComponents();

	    // Get authentication
	    isAuthenticated = isAuthenticated(cls);
	    
	    // Construct 
	    accessors = new Method[fields.length];
	    mappers = new InputMapper[fields.length];
	    
	    // Scan components
	    for (int i = 0; i < fields.length; i++) {
	    	RecordComponent field = fields[i];
	    	Class<?> fieldCls = field.getType();
    		accessors[i] = field.getAccessor();
    		mappers[i] = _apiService.classToInputMapper.get(fieldCls);
    		if (mappers[i] == null)
    			throw new RuntimeException("API:refl:input[" + cls.getSimpleName() + "]: " + field.getName() + ": Field is of unsupported type " + fieldCls.getSimpleName() + ".");
	    }
	    	    
	    // Get output
	    Class<?> output;
	    try {
	    	api.Output annotation = cls.getAnnotation(api.Output.class);
	    	if (annotation == null)
	    		throw new RuntimeException("API:refl:input[" + cls.getSimpleName() + "]: No Output annotation was present.");
	    	output = annotation.value();
	    }
	    catch (NullPointerException e) {
	    	throw new RuntimeException("API:refl:input[" + cls.getSimpleName() + "]: Output annotation was misconfigured.");
	    }
	    int offset = mappers.length + (isAuthenticated ? 2 : 0);
	    
	    // Construct OutputConfiguration
	    outputConfiguration = new OutputConfiguration(output, offset);
	    
	    // Configure call string & build initial statement
	    int n = offset + outputConfiguration.size;
	    statementString =
				"{call " +
				"API_" + (isAuthenticated(cls) ? "Private" : "Public") + "_Patient_" + cls.getSimpleName() +
				" (" +
				((n == 0) ? "" : String.join(", ", Collections.nCopies(n, "?")))
				+ ")}";
		buildCallableStatement();
	}
	
	private boolean isAuthenticated(Class<?> cls) {
		return cls.isAnnotationPresent(api.Authenticated.class);
	}
	
	/**
	 * Calls an API endpoint over an input record object.
	 * @param object	The input record object.	
	 * @return			The output record object.
	 */
	public Object execute(APIInput<?> object) {
		CallableStatement statement = buildCallableStatement();
		mapToCallableStatement(object, statement);
		try {
			statement.execute();
		}
		catch (SQLException e) {
			throw new DatabaseException();
		}
		return outputConfiguration.execute(statement);
	}
	
	private void mapToCallableStatement(Object object, CallableStatement statement) {
		// Handle authentication
		int offset = 1;
		if (isAuthenticated) {
			AuthService _userService = AuthServiceSingleton.INSTANCE.get();
			AuthCredentials authCredentials = _userService.getAuthCredentials();
			try {
				statement.setString(1, authCredentials.accountUUID());
				statement.setString(2, authCredentials.sessionToken());
			}
			catch (Exception e) {
				throw new RuntimeException("API:exec:input[" + object.getClass().getSimpleName() + "]: Error assigning authentication data.");
			}
			offset += 2;
		}
		
		// Map input object
		for (int i = 0; i < accessors.length; i++) {
			try {
				mappers[i].invoke(i + offset, accessors[i].invoke(object), statement);
			}
			catch (Exception e) {
				throw new RuntimeException("API:exec:input[" + object.getClass().getSimpleName() + "]: Couldn't invoke accessor on record.");
			}
		}
	}
	
	private CallableStatement buildCallableStatement() {
		// Get database singleton
		DatabaseService _databaseService = DatabaseServiceSingleton.INSTANCE.get();
		
		// Get a statement
		CallableStatement statement;
		try {
			statement = _databaseService.getCallableStatement(statementString);
		}
		catch (Exception e) {
			throw new NoConnectionException();
		}
		
		// Configure the statement
		try {
			outputConfiguration.configureStatement(statement);
		}
		catch (RuntimeException e) {
			throw new RuntimeException(statementString + " | " + e.toString());
		}
		
		// Return
		return statement;
	}
}

/**
 * Classifies the information retrieved from scanning an output record.
 */
class OutputConfiguration {
	// An array of configurers that are used to prepare the statement to receive output.
	public Integer[] configurers;
	// An array that is a mixture of
	//		mappers that take a callable statement, and return an object;
	// 	and	OutputResultsConfiguration objects.
	public Object[] mappers;
	// The declared constructor of appropriate type
	public Constructor<?> constructor;
	// The number of OUT parameters
	public int size;
	// The parameter at which output starts
	public int offset;
			
	/**
	 * Scans an output record and builds a configuration object around it.
	 * @param cls	The record to scan.
	 */
	public OutputConfiguration(Class<?> cls, int offset) {
		// Inject the APIService
		APIService _apiService = APIServiceSingleton.INSTANCE.get();
		
		// Capture offset
		this.offset = offset + 1;
		
		// VALIDATE: is it a record?
	    if (!cls.isRecord())
	        throw new RuntimeException("API:refl: Output [" + cls.getSimpleName() + "]: Is not a record class.");
	    
	    // Get components & scan
	    RecordComponent[] fields = cls.getRecordComponents();
	    ArrayList<Object> mappersList = new ArrayList<>();
	    ArrayList<Integer> configurersList = new ArrayList<>();
	    size = 0;
	    for (RecordComponent field : fields) {
	    	// Get data about field
	    	Class<?> fieldCls = field.getType();
	    	String fieldName = field.getName();
	    	
	    	// Determine if the field represents is a result set or a variable output
	    	if (field.isAnnotationPresent(api.ResultSet.class)) {
	    		// A result set field
	    		// VALIDATE: is it a list?
	    		if (!List.class.isAssignableFrom(fieldCls))
	    			throw new RuntimeException("API:refl:output[" + cls.getSimpleName() + "]: " + fieldName + ": Is marked ResultSet but not a list type.");
	    		
	    		// VALIDATE: is it a raw type?
	    		Type parameterGeneric = field.getGenericType();
	    		if (!(parameterGeneric instanceof ParameterizedType))
	    			throw new RuntimeException("API:refl:output[" + cls.getSimpleName() + "]: " + fieldName + ": ResultSet List is raw.");
	    		
	    		// VALIDATE: is there only 1 parameter? 
	    		Type[] parameters = ((ParameterizedType)parameterGeneric).getActualTypeArguments();
	    		if (parameters.length != 1)
	    			throw new RuntimeException("API:refl:output[" + cls.getSimpleName() + "]: " + fieldName + ": ResultSet List must only have 1 type parameter.");
	    		
	    		// VALIDATE: must be a class type
	    		Type parameterType = parameters[0];
	    		if (!(parameterType instanceof Class<?>))
	    			throw new RuntimeException("API:refl:output[" + cls.getSimpleName() + "]: " + fieldName + ": ResultSet List is parametrised by a type that is not a class.");

	    		// VALIDATE: 
	    		Class<?> parameter = (Class<?>)parameterType;
	    		if (!(parameter.isRecord()))
	    			throw new RuntimeException("API:refl:output[" + cls.getSimpleName() + "]: " + fieldName + ": ResultSet List is parametrised by a type that is not a record.");
	    		
	    		// Add to mappers list
	    		mappersList.add(new OutputResultsConfiguration(parameter));
	    	}
	    	else {
	    		// A variable field
	    		size++;
	    		
	    		// VALIDATE: is mapper recognised?
	    		OutputMapper mapper = _apiService.classToOutputMapper.get(fieldCls);
	    		if (mapper == null)
	    			throw new RuntimeException("API:refl:output[" + cls.getSimpleName() + "]: " + fieldName + ": Mapper: Field is of unsupported type " + fieldCls.getSimpleName() + ".");
	    		
	    		// Add to mappers list
	    		mappersList.add(mapper);
	    		
	    		// VALIDATE: is configurer recognised?
	    		Integer configurer = _apiService.classToOutputConfigurer.get(fieldCls);
	    		if (configurer == null)
	    			throw new RuntimeException("API:refl:output[" + cls.getSimpleName() + "]: " + fieldName + ": Configurer: Field is of unsupported type " + fieldCls.getSimpleName() + ".");
	    		
	    		// Add to mappers list
	    		configurersList.add(configurer);
	    	}
	    }
	    
	    // Finalise mappers & results
	    mappers = mappersList.toArray(Object[]::new);
	    configurers = configurersList.toArray(Integer[]::new);
	    
	    // Get constructor
		Constructor<?>[] constructors = (Constructor<?>[])cls.getDeclaredConstructors();
	    
	    // VALIDATE: is there only 1 constructor?
	    if (constructors.length != 1)
	    	throw new RuntimeException("API:refl:output[" + cls.getSimpleName() + "]: Has 0 or more than 1 constructors.");
	    constructor = constructors[0];
	}
	
	/**
	 * Executes this configuration.
	 * @param statement	The executed statement.
	 * @return			An output record, with associated lists of output results records if appropriate.
	 */
	public APIOutput execute(CallableStatement statement) {
		// Map values out of the statement and into an Object[] array
		Object[] values = new Object[mappers.length];
		for (int i = 0; i < mappers.length; i++) {
			Object mapper = mappers[i];
			if (mapper instanceof OutputMapper)
				try {
					values[i] = ((OutputMapper)mapper).invoke(i + offset, statement);
				}
				catch (Exception e) {
					throw new RuntimeException("API:exec:output[" + constructor.getDeclaringClass().getSimpleName() + "]: Could not invoke output mapper.");
				}
			else if (mapper instanceof OutputResultsConfiguration)
				values[i] = ((OutputResultsConfiguration)mapper).execute(statement);
		}
		
		// Construct the output record & return
		Object output;
		try {
			output = constructor.newInstance(values);
		}
		catch (Exception e) {
			throw new RuntimeException("API:exec:output[" + constructor.getDeclaringClass().getSimpleName() + "]: Could not construct output record.");	
		}
		return (APIOutput)output;
	}
	
	/**
	 * Configures a statement according to its output elements. 
	 * @param statement		The statement.
	 */
	public void configureStatement(CallableStatement statement) {
		int i = offset;
		for (Integer type : configurers) {
			try {
				statement.registerOutParameter(i, type);
			}
			catch (Exception e) {
				throw new RuntimeException("API:statement: Couldn't configure the callable statement's output parameters | " + String.valueOf(offset) + " |" + String.valueOf(type) + " | " + String.valueOf(i) + " | " + e.toString());
			}
			i++;
		}
	}
}

/**
 * Classifies the information retrieved from scanning an output results record.
 */
class OutputResultsConfiguration {
	// An array of mappers that take a result set row, and return an object. 
	public OutputResultsMapper[] mappers;
	// Names
	public String[] names;
	// The declared constructor of appropriate type
	public Constructor<?> constructor;
	// The number of parameters to pass to the constructor
	public int size;
	
	/**
	 * Scans an output results record and builds a configuration object around it.
	 * @param cls	The record to scan.
	 */
	public OutputResultsConfiguration(Class<?> cls) {
		// Inject the APIService
		APIService _apiService = APIServiceSingleton.INSTANCE.get();
		
		// VALIDATE: is it a record?
	    if (!cls.isRecord())
	        throw new RuntimeException("API:refl:outputResults[" + cls.getSimpleName() + "]: Is not a record class.");

	    // Get components & scan
	    RecordComponent[] fields = cls.getRecordComponents();
	    size = fields.length;
	    mappers = new OutputResultsMapper[size];
	    names = new String[size];
	    
	    for (int i = 0; i < size; i++) {
	    	// Get data about field
	    	RecordComponent field = fields[i];
	    	Class<?> fieldCls = field.getType();
	    	String fieldName = field.getName();
	    	
	    	// Put into array
	    	names[i] = fieldName;
	    	// VALIDATE: is mapper recognised?
	    	mappers[i] = _apiService.classToOutputResultsMapper.get(fieldCls);
	    	if (mappers[i] == null)
	    		throw new RuntimeException("API:refl:outputResults[" + cls.getSimpleName() + "]: " + fieldName + ": Field is of unsupported type " + fieldCls.getSimpleName() + ".");
	    }
	    
	    // Get constructor
		Constructor<?>[] constructors = (Constructor<?>[])cls.getDeclaredConstructors();
	    
	    // VALIDATE: is there only 1 constructor?
	    if (constructors.length != 1)
	    	throw new RuntimeException("API:refl:outputResults[" + cls.getSimpleName() + "]: Has 0 or more than 1 constructors.");
	    constructor = constructors[0];
	}

	/**
	 * Executes this configuration.
	 * @param statement	The executed statement.
	 * @return			An array list of output result records, one for each row.
	 */
	public ArrayList<APIOutputResult> execute(CallableStatement statement) {
		// Get a result set
		ResultSet resultSet;
		try {
			resultSet = statement.getResultSet();
		}
		catch (Exception e) {
			throw new RuntimeException("API:exec:outputResults[" + constructor.getDeclaringClass().getSimpleName() + "]: Couldn't get result set.");
		}
		
		// Make an ArrayList
		ArrayList<APIOutputResult> list = new ArrayList<>();
		
		// For each result
		try {
			while (resultSet.next()) {
				// Map values out of the result set and into an Object[] array
				Object[] values = new Object[names.length];
				for (int i = 0; i < names.length; i++)
					try {
						values[i] = mappers[i].invoke(names[i], resultSet);
					}
					catch (Exception e) {
						throw new RuntimeException("API:exec:outputResults[" + constructor.getDeclaringClass().getSimpleName() + "]: Could not invoke outputResults mapper.");
					}
	
				// Construct the output record & add to list
				try {
					list.add((APIOutputResult)constructor.newInstance(values));
				}
				catch (Exception e) {
					throw new RuntimeException("API:exec:outputResults[" + constructor.getDeclaringClass().getSimpleName() + "]: Could not construct a row record.");
				}
			}
		}
		catch (Exception e) {
			throw new RuntimeException("API:exec:outputResults[" + constructor.getDeclaringClass().getSimpleName() + "]: Error retrieving result set.");
		}
		
		// Return list
		return list;
	}
}

/**
 * An Input Mapper puts an item into a statement that has not yet been executed by index.
 */
abstract class InputMapper {
	public abstract void invoke(int i, Object object, CallableStatement statement) throws SQLException;
}	
/**
 * An Output Mapper retrieves an item from an executed statement by index.
 */
abstract class OutputMapper {
	public abstract Object invoke(int i, CallableStatement statement) throws SQLException;
}

/**
 * An Output Results mapper retrieves an item from a row of a result set by name.
 */
abstract class OutputResultsMapper {
	public abstract Object invoke(String name, ResultSet resultSet) throws SQLException;
}