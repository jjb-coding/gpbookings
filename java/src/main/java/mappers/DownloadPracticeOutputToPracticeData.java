package mappers;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import api.output.DownloadPracticeOutput;
import api.outputResult.DownloadPracticePracticeResult;
import services.mapperService.ClassPair;
import services.mapperService.Mapper;
import services.practiceProvider.PracticeData;
import services.practiceProvider.PracticeDayDescriptionData;

public class DownloadPracticeOutputToPracticeData extends Mapper<DownloadPracticeOutput, PracticeData> {
	public PracticeData invoke(DownloadPracticeOutput in) {
		
		// For each day
		HashMap<Long, PracticeDayDescriptionData> map = new HashMap<>();
		/**
		for (DownloadPracticePracticeDayDescriptionResult row : in.practiceDayDescriptions()) {
			PracticeDayDescriptionData practiceDayDescription = new PracticeDayDescriptionData(
				row.Identifier(),
				row.OpenTime(),
				row.NumberOfSlots(),
				row.LunchStart(),
				row.LunchEnd()
				);
			map.put(
				row.PracticeDayDescriptionID(),
				practiceDayDescription
				);
		}
		*/
		{
			DownloadPracticePracticeResult practice = in.practice().get(0);
			PracticeDayDescriptionData weekDay = new PracticeDayDescriptionData("Weekday", Time.valueOf(LocalTime.parse("09:00:00")), 16, 7, 8);
			PracticeDayDescriptionData weekEnd = new PracticeDayDescriptionData("Weekend", Time.valueOf(LocalTime.parse("10:00:00")), 11, 4, 5);
			map.put(practice.MondayID(), weekDay);
			map.put(practice.TuesdayID(), weekDay);
			
			map.put(practice.ThursdayID(), weekDay);
			map.put(practice.FridayID(), weekDay);
			map.put(practice.SaturdayID(), weekEnd);
			map.put(practice.SundayID(), weekEnd);			
		}
		
		// Map to ordered days
		DownloadPracticePracticeResult practice = in.practice().get(0);
		ArrayList<PracticeDayDescriptionData> days = new ArrayList<>();
		days.add(map.get(practice.MondayID()));
		days.add(map.get(practice.TuesdayID()));
		days.add(map.get(practice.WednesdayID()));
		days.add(map.get(practice.ThursdayID()));
		days.add(map.get(practice.FridayID()));
		days.add(map.get(practice.SaturdayID()));
		days.add(map.get(practice.SundayID()));
		
		return new PracticeData (
			in.practice().get(0).GracePeriod(),
			in.practice().get(0).SlotLength(),
			days
			);
	}
	
	@Override
	public ClassPair describe() {
		return new ClassPair(DownloadPracticeOutput.class, PracticeData.class);
	}
}

