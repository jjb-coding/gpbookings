package api;

import services.apiService.APIService;
import services.apiService.APIServiceSingleton;

/**
 * Classifies the parameters of an ingoing request to the API. Intended to be applied
 * to a record from the api.input package.
 * @param <T>	The return type, a record object from the api.output package.
 */
public interface APIInput<T extends APIOutput> {
	// *** DEFAULT METHODS
	/**
	 * Passes the execute call along to the API service.
	 * @param <T>	The return type expected.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public default T execute() {
		// Inject the API Service
		APIService _apiService = APIServiceSingleton.INSTANCE.get();
		
		// Execute & cast
		Object object = _apiService.execute(this);
		
		T ret = null;
		try {
			ret = (T)object;
		}
		catch (Exception e) {
			throw new RuntimeException("APIInput[" + this.getClass().getSimpleName() + "]: Output record is of incorrect type. It is " + ret.getClass().getSimpleName());
		}
		return ret;
	}
}