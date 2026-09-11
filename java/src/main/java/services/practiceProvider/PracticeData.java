package services.practiceProvider;

import java.sql.Time;
import java.util.ArrayList;

public record PracticeData (
		Time gracePeriod,
		Time slotLength, 
		ArrayList<PracticeDayDescriptionData> days
		) {};