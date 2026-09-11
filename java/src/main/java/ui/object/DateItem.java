package ui.object;

import java.sql.Date;

import util.StringUtil;

public class DateItem extends Identifier<Date> {
	Date date;
	String format;
	
	/**
	 * Constructs a DateItem instance.
	 * @param date	The date.
	 */
	public DateItem(Date date) {
		if (date == null) {
			date = null;
			format = "No open days were found.";
			return;
		}
		this.date = date;
		this.format = StringUtil.formatDate(date);
	}
	
	@Override
	public Date id() {
		return date;
	}

	@Override
	public String toString() {
		return format;
	}

}
