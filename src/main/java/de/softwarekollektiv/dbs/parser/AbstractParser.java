package de.softwarekollektiv.dbs.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import de.softwarekollektiv.dbs.dbcon.DbConnection;

public abstract class AbstractParser implements Parser {

	public static Logger log = Logger.getLogger(AbstractParser.class);
	
	protected DbConnection dbcon;
	protected String file;
	
	protected String delimiter = " ";
	protected boolean skipFirstPart = true;
	protected String firstStop;
	protected String table;
<<<<<<< HEAD

	protected String values;

	protected String ref_id = "";

	protected int valuesSize;
=======
	int values;
>>>>>>> 02a008d60f5c890114d97517a77ec96b835249ba
	
	private BufferedReader in;
	
	/**
	 * @param dbcon reference to DbConnection object
	 * @param file the file to read
	 */
	protected AbstractParser(DbConnection dbcon, String file) {
		this.dbcon = dbcon;
		this.file = file;
	}

	/**
	 * opens a specific File and jumps to the first Line of data input. The
	 * first Line is specified by the extending class
	 * 
	 * @throws IOException
	 *             if access to file fails
	 */
	public void open() throws IOException {
		
		if (in != null){
			in.close();
		}
		
		log.debug("first stop: " + firstStop);
		
		/*
		 * new InputReader with the correct character encoding
		 */
		in = new BufferedReader(new InputStreamReader(new FileInputStream(
				file), "ISO-8859-15"));
		
		if(skipFirstPart) {
			while (!in.readLine().equals(firstStop))
				;
		}
	}

	/**
	 * start parsing previous opened file
	 * 
	 * @throws IOException
	 *             if reading from file fails
	 */
	public void parse() {
		
		/*
		 * build the preparedStatement string
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO "+ table +" VALUES  );
				
		/*
		 * the last question mark doesn't follow a comma
		 */
		for ( int i =0; i < valuesSize; i++){
			sb.append("?,");
		}
		sb.append("?);");
		
		try {
			String line;
			PreparedStatement st = dbcon.getConnection().prepareStatement(
					sb.toString());
			while ((line = in.readLine()) != null) {
				newLine(line.split(delimiter), st);
			}
			dbcon.getConnection().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected abstract void newLine(String[] lineParts, PreparedStatement st);

	public void close() {
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
<<<<<<< HEAD

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	protected Date yearToDate(String year){
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		return new Date(cal.getTimeInMillis());
	}
	
	protected String getTitleFromImdbString(String text) {
		return text.split(" \\(\\d+.*?\\)")[0];
	}

	protected Date getDateFromImdbString(String text) {
		Pattern pattern = Pattern.compile("\\(\\d{4}-?(\\d{4}|\\?{4})?\\)");

		Matcher matcher = pattern.matcher(text);
		String year;
		if (matcher.find()) {
			year = matcher.group().substring(1, 5);
		} else {
			// entries without a year are discarded since we just insert
			// movies from 2010 and 2011
			year = "2000";
		}

		return yearToDate(year);
	}
	
=======
>>>>>>> 02a008d60f5c890114d97517a77ec96b835249ba
}
