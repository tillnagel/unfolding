package de.fhpotsdam.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringUtils {

	private static final String ISO_FORMAT = "yyyy-MM-dd";
	private static final String ISO_FORMAT_LONG = "yyyy-MM-dd'T'HH:mm:ssZ";

	public static Calendar parseIsoDate(String strDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
		Date date = sdf.parse(strDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	public static Calendar parseIsoDateTime(String strDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT_LONG);
		Date date = sdf.parse(strDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static long daysBetween(Calendar startDate, Calendar endDate) {
		Calendar date = (Calendar) startDate.clone();
		long daysBetween = 0;
		while (date.before(endDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}

}
