package services.mapperService;

import java.util.Map;

import util.ReflectionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapperService {
	// Stores mappers
	Map<ClassPair,Mapper<?,?>> map = new HashMap<>();
	
	/**
	 * Gets all mappers. 
	 */
	public MapperService() {
		ArrayList<Class<?>> mappers;
		try {
			mappers = ReflectionUtil.getClasses("mappers");
		}
		catch (Exception e) {
			throw new RuntimeException("MAPPER:init: Couldn't get classes.");
		}
		for (Class<?> mapper : mappers) {
			Mapper<?,?> mapperInstance;
			try {
				mapperInstance = (Mapper<?, ?>)mapper.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new RuntimeException("MAPPER:refl[" + mapper.getSimpleName() + "]: Couldn't instantiate.");
			}
			map.put(mapperInstance.describe(), mapperInstance);
		}
	}
	
	/**
	 * Retrieves a mapper.
	 * @param <T>	The source object type.
	 * @param <U>	The destination object type.
	 * @param t		The source object class.
	 * @param u 	The destination object class.
	 * @return 		The mapper.
	 */
	@SuppressWarnings("unchecked")
	public <T,U> Mapper<T,U> getMapper(Class<T> t, Class<U> u) {
		ClassPair classes = new ClassPair(t, u);
		return (Mapper<T,U>)map.get(classes);
	}
	
	/**
	 * Maps an object directly.
	 * @param <T> 	The source object type.
	 * @param <U>	The destination object type.
	 * @param in	The input to be mapped.
	 * @param t		The source object class.
	 * @param u		The destination object class.
	 * @return		The mapped output.
	 */
	@SuppressWarnings("unchecked")
	public <T,U> U map(T in, Class<T> t, Class<U> u) {
		ClassPair classes = new ClassPair(t, u);
		return ((Mapper<T,U>)map.get(classes)).invoke(in);
	}
	
	/**
	 * Maps a list of objects directly.
	 * @param <T> 	The source object type.
	 * @param <U>	The destination object type.
	 * @param in	The list of inputs to be mapped.
	 * @param t		The source object class.
	 * @param u		The destination object class.
	 * @return		The list of mapped outputs.
	 */
	@SuppressWarnings("unchecked")
	public <T,U> ArrayList<U> map(List<T> in, Class<T> t, Class<U> u) {
		ClassPair classes = new ClassPair(t, u);
		Mapper<T,U> mapper = ((Mapper<T,U>)map.get(classes));
		ArrayList<U> ret = new ArrayList<U>();
		for (T item : in)
			ret.add(mapper.invoke(item));
		return ret;
	}
}
