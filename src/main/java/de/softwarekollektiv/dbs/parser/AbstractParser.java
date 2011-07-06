package de.softwarekollektiv.dbs.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import de.softwarekollektiv.dbs.dbcon.DbConnection;

public abstract class AbstractParser implements Parser {

	protected DbConnection dbcon;
	protected String file;
	protected String delimiter = " ";
	
	private BufferedReader in;

	/**
	 * This method is called for each line in the file. The extending class should
	 * extract the information from it and insert it into the database.
	 * 
	 * @param lineParts
	 * @throws SQLException
	 */
	protected abstract void newLine(String[] lineParts) throws SQLException;	
	
	/**
	 * This method is called before parsing the data, so that extending classes
	 * can skip any header parts contained in their respective files.
	 * 
	 * @param in BufferedReader associated to the opened file
	 * @throws IOException should not happen
	 */
	protected abstract void skipHeader(BufferedReader in) throws IOException; 
	
	/**
	 * @param dbcon
	 *            reference to DbConnection object
	 * @param file
	 *            the file to read
	 */
	protected AbstractParser(DbConnection dbcon, String file) {
		this.dbcon = dbcon;
		this.file = file;
	}

	/**
	 * Opens a specific File and jumps to the first Line of data input.
	 * 
	 * @throws IOException if access to file fails
	 */
	public void open() throws IOException {

		if (in != null) {
			in.close();
		}
		
		/*
		 * new InputReader with the correct character encoding
		 */
		in = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "ISO-8859-15"));

		skipHeader(in);
	}

	/**
	 * Start parsing previously opened file
	 * 
	 * @throws IOException
	 *             if reading from file fails
	 */
	public void parse() {
		
		try {
			String line;

			while ((line = in.readLine()) != null) {
				newLine(line.split(delimiter));
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

	/**
	 * Close file.
	 */
	public void close() {
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
