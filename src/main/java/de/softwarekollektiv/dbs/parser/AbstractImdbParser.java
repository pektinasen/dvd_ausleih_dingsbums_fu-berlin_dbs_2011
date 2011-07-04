package de.softwarekollektiv.dbs.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import de.softwarekollektiv.dbs.DbConnection;

public abstract class AbstractImdbParser implements ImdbParser {

	public static Logger log = Logger.getLogger(AbstractImdbParser.class);
	
	protected String delimiter = " ";

	protected String firstStop; 
	
	private BufferedReader in;

	protected String table;

	protected int values;
	
	

	/**
	 * opens a specific File and jumps to the first Line of data input. The
	 * first Line is specified by the extending class
	 * 
	 * @param file
	 *            The File to read
	 * @throws IOException
	 *             if access to file fails
	 */
	public void open(String file) throws IOException {
		
		if (in != null){
			in.close();
		}
		
		log.debug("first stop: " +firstStop);
		
		/*
		 * new InputReader with the correct Chareacter Encoding
		 */
		in = new BufferedReader(new InputStreamReader(new FileInputStream(
				file), "ISO-8859-15"));
		
		while (!in.readLine().equals(firstStop))
			;
	}

	/**
	 * start parsing previous opened file
	 * 
	 * @throws IOException
	 *             if reading from file fails
	 */
	public void parse() {
		if (in == null) {
			log.warn("got file to parse");
//			throw new IllegalOperationException();
		}
		
		/*
		 * build the preparedStatement string
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO "+ table +" VALUES (");
				
		/*
		 * the last question mark doesn't follow a comma
		 */
		for ( int i =0; i < values - 1; i++){
			sb.append("?,");
		}
		sb.append("?);");
		
		try {
			String line;
			PreparedStatement st = DbConnection.getConnection().prepareStatement(
					sb.toString());
			while ((line = in.readLine()) != null) {
				newLine(line.split(delimiter), st);
			}
			DbConnection.getConnection().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public abstract void newLine(String[] lineParts, PreparedStatement st);

	public void close() {
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

}
