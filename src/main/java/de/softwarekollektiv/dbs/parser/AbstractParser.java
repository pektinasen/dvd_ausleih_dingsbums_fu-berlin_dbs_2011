package de.softwarekollektiv.dbs.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
	int values;
	
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
}
