package util;

import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalTime;

public class StringUtil {
	static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT);
	
	/**
	 * Gets a number in a sequence format i.e. 1 -> 1st, 2 -> 2nd.
	 * @param number	The number.
	 * @return			The sequence position.
	 */
	public static String getSequence(int number) {
		return String.valueOf(number) + getSuffix(number);
	}
	
	/**
	 * Gets the sequence denotation of a number i.e. th, st.
	 * @param number	The number.
	 * @return			The suffix it implies.
	 */
	static String getSuffix(int number) {
		if (number >= 10 && number <= 20)
			return "th";

		if ((number % 10) == 1)
			return "st";
		else if ((number % 10) == 2)
			return "nd";
		else if ((number % 10) == 2)
			return "rd";
		return "th";
	}
	
	/**
	 * Gets a two-digit representation of a number with leading zeroes.
	 * @param number	The number.
	 * @return			The representation.
	 */
	public static String getTwoDigit(int number) {
		// Trivial case
		if (number == 0)
			return "00";
		
		// Otherwise, parse and optionally pad
		String value = String.valueOf(number);
		if (value.length() == 1)
			value = "0" + value;
		
		// Return
		return value;
	}
	
	/**
	 * Formats a time.
	 * @param time	The time.
	 * @return		A string in the format 00:00.
	 */
	public static String getFormattedTime(LocalTime time) {
		return getTwoDigit(time.getHour()) + ":" + getTwoDigit(time.getMinute());
	}
	
	/**
	 * Replaces a null with an empty string, or does nothing.
	 * @param value		The string or null value.
	 * @return			A string.
	 */
	public static String unNull(String value) {
		if (value == null)
			return "";
		return value;
	}
	
	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}
}
