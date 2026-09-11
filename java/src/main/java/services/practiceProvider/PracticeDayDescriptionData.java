package services.practiceProvider;

import java.sql.Time;

public record PracticeDayDescriptionData (
	String identifier,
	Time openTime, 
	int numberOfSlots, 
	int lunchStart, 
	int lunchEnd
	) {};