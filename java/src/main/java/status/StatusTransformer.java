package status;

import java.util.EnumMap;
import java.util.Map;

public class StatusTransformer {
	static EnumMap<Status, Status> actOnServerMap = new EnumMap<>(Map.of(
			Status.SUCCESS, Status.SUCCESS,
			Status.LOGIN_DETAILS_INVALID, Status.LOG_IN
			));
	
	
	public static Status actOnServer(Status status) {
		return actOnServerMap.get(status);
	}
	
	public static Status specialiseSuccess(Status source, Status specialised) {
		return (source == Status.SUCCESS) ? specialised : source;
	}
	
	public static Status specialise(Status source, Status match, Status specialised) {
		return (source == match) ? specialised : source;
	}
}
