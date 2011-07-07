package de.softwarekollektiv.dbs;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

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

	
}
