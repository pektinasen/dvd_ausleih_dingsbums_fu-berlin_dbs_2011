package de.softwarekollektiv.dbs;

import static junit.framework.Assert.assertEquals;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;

public class DateFormatTest {
	
//	@Test
//	public void simpleDateTest(){
//		String[] dateParts = {"24", "February", "2010"};
//		
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParts[0]));
//		java.util.Date date = null;
//		try {
//			date = new SimpleDateFormat("MMM", Locale.ENGLISH)
//					.parse(dateParts[1].substring(0, 3));
//		} catch (ParseException e) {}
//		Calendar month = Calendar.getInstance();
//		month.setTime(date);
//		cal.set(Calendar.MONTH, month.get(Calendar.MONTH));
//		cal.set(Calendar.YEAR, Integer.parseInt(dateParts[2]));
//		
//		
//		Date realDate = new Date(cal.getTimeInMillis());
//		
//		Calendar expectedCal = Calendar.getInstance();
//		expectedCal.set(Calendar.YEAR, 2010);
//		expectedCal.set(Calendar.MONTH, 1);
//		expectedCal.set(Calendar.DAY_OF_MONTH, 24);
//		Date expectedDate = new Date(expectedCal.getTimeInMillis());
//		assertEquals(expectedDate, Date.valueOf("2010-02-24"));
//		
//	}
	
}
