package de.softwarekollektiv.dbs;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;



public class RegExTest {

	@Test
	public void simpleRegExTest(){
		assertEquals(true, "\"World News Now\" (1992) {(2010-08-04)}".matches("\".+?\".*"));
	
		String manyTabsInTheBeginning = "\t\t\tbla";
		
		assertEquals("",manyTabsInTheBeginning.split("\t+")[0]);
		assertEquals("bla", manyTabsInTheBeginning.split("\t+")[1]);
		String aMovie= "A Movie Name (2010)";
		assertEquals("A Movie Name", aMovie.split(" \\(\\d+.*?\\)")[0]);
	
	}
	
	@Test
	public void dateRegExTest(){
		String text = "Fluch (der) Karibik (2010)";
		
		Pattern pattern = Pattern.compile("\\(\\d{4}-?(\\d{4}|\\?{4})?\\)");
		
		Matcher matcher = pattern.matcher(text);
		matcher.find();
		assertEquals("2010", matcher.group().substring(1,5));
	}
	
	@Test
	public void moreDateRegExTest(){
		Pattern datePattern = Pattern
				.compile("(\\d{1,2})? ?((\\w+)? ?(\\d{4}))");
		String year = "2000";
		String monthYear = "January 2000";
		String dayMonthYear = "1 January 2000";
		
		Matcher mYear = datePattern.matcher(year);
		Matcher mMonthYear = datePattern.matcher(monthYear);
		Matcher mDayMonthYear = datePattern.matcher(dayMonthYear);
		mYear.find();
		mMonthYear.find();
		mDayMonthYear.find();
		System.out.println(mYear.groupCount());
		System.out.println(mMonthYear.groupCount());
		System.out.println(mDayMonthYear.groupCount());
		
		System.out.println(mYear.group(1));
		System.out.println(mMonthYear.group(2));
		System.out.println(mYear.group(3));
		System.out.println(mYear.group(4));
		
		
		
		
		
	}

	
}
