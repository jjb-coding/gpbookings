package services.practiceProvider;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import api.input.DownloadDoctors;
import api.input.DownloadPractice;
import api.input.DownloadTitles;
import api.output.DownloadDoctorsOutput;
import api.output.DownloadPracticeOutput;
import api.output.DownloadTitlesOutput;
import mappers.output.DoctorDataList;
import mappers.output.TitleDataList;
import services.mapperService.MapperService;
import services.mapperService.MapperServiceSingleton;
import ui.object.DateItem;
import ui.object.EnumeratedItem;
import ui.object.Identifier;
import util.StringUtil;

/**
 * Provides read-only data retrieved from the database
 * on startup.
 */
public class PracticeProvider {
	// Lists of data types
	final ArrayList<DoctorData> doctors;
	final ArrayList<TitleData> titles;
	final Map<String,DoctorData> doctorIDToDoctor;
	final PracticeData practice;
	
	/**
	 * Starts the PracticeProvider.
	 */
	public PracticeProvider() {
		MapperService _mapperService = MapperServiceSingleton.INSTANCE.get();
    	doctors = _mapperService.map(
    			new DownloadDoctors().execute(),
    			DownloadDoctorsOutput.class,
    			DoctorDataList.class)
    			.list();
    	titles = _mapperService.map(
    			new DownloadTitles().execute(),
    			DownloadTitlesOutput.class,
    			TitleDataList.class)
    			.list();
    	practice = _mapperService.map(
    			new DownloadPractice().execute(),
    			DownloadPracticeOutput.class, 
    			PracticeData.class);
    	
    	doctorIDToDoctor = new HashMap<>();
    	for (DoctorData doctor : doctors)
    		doctorIDToDoctor.put(doctor.id(), doctor);
	}
	
	/**
	 * Gets the Doctors array in the Identifiers format.
	 * @return The Doctors array.
	 */
	public ArrayList<? extends Identifier<String>> getDoctorsAsIdentifiers() {
		return (ArrayList<? extends Identifier<String>>)doctors;
	}
	
	/**
	 * Gets the Titles array in the Identifiers format.
	 * @return The Titles array.
	 */
	public ArrayList<? extends Identifier<String>> getTitlesAsIdentifiers() {
		return (ArrayList<? extends Identifier<String>>)titles;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public DoctorData getDoctorByID(String id) {
		return doctorIDToDoctor.get(id);
	}
	
	/**
	 * 
	 * @param time
	 * @return
	 */
	public boolean canBookingBeRescheduled(Timestamp time) {
		Timestamp now = Timestamp.from(Instant.now());
		Timestamp total = new Timestamp(time.getTime() - practice.gracePeriod().getTime());
		return (total.after(now));
	}
	
	/**
	 * 
	 * @param time
	 * @return
	 */
	public boolean isBookingOver(Timestamp time) {
		Timestamp now = Timestamp.from(Instant.now());
		Timestamp total = new Timestamp(time.getTime() + practice.slotLength().getTime());
		return (total.after(now));
	}
	
	/**
	 * Determines if all days have no description.
	 */
	public boolean anyDayDescriptions() {
		boolean allNulls = true;
		for (PracticeDayDescriptionData data : practice.days())
			allNulls &= (data == null);
		return !allNulls;
	}
	
	/**
	 * Get days on which the practice is open.
	 */
	public ArrayList<Identifier<Date>> getDays(int n) {		
		// Get today
		LocalDate date = LocalDate.now();
		int day = date.getDayOfWeek().getValue() - 1;
		
		// Test
		ArrayList<Identifier<Date>> list = new ArrayList<>();
		if (!anyDayDescriptions()) {
			list.add(new DateItem(null));
			return list;
		}
		
		// Get n days
		while (n > 0) {
			PracticeDayDescriptionData dayDescription = practice.days().get(day);
			day++;
			if (day == 7)
				day = 0;
			if (dayDescription == null)
				continue;
			// Add
			n--;
			date = date.plusDays(1);
			Date sqlDate = Date.valueOf(date);
			list.add(new DateItem(sqlDate));
		}
		
		return list;
	}
	
	/**
	 * Get the booking slots for a given day.
	 */
	public ArrayList<EnumeratedItem> getBookingSlots(Date date) {
		ArrayList<EnumeratedItem> list = new ArrayList<>();
		LocalDate dateTime = date.toLocalDate();
		int day = dateTime.getDayOfWeek().getValue() - 1;
		
		PracticeDayDescriptionData dayDescription = practice.days().get(day);
		if (dayDescription == null)
			return list;
		
	    LocalTime current = dayDescription.openTime().toLocalTime();
	    Duration slotLength = Duration.ofMillis(practice.slotLength().getTime());

	    // Over total number
		for (int i = 0; i < dayDescription.numberOfSlots(); i++) {
			LocalTime next = current.plus(slotLength);
			
	        // Only not in lunch
	        if (i < dayDescription.lunchStart() || i > dayDescription.lunchEnd())
	            list.add(new EnumeratedItem(i, 
	                StringUtil.getFormattedTime(current) + " - " + 
	                StringUtil.getFormattedTime(next)));
	        
	        // Iterate
	        current = next;
		}
		
		return list;
	}
}