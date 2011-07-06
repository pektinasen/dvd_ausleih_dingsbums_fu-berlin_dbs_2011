package de.softwarekollektiv.dbs.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import de.softwarekollektiv.dbs.dbcon.DbConnection;

public abstract class AbstractParser implements Parser {
	private final DbConnection dbcon;
	private final String file;
	private BufferedReader in;
	
	protected String delimiter;
	
	/**
	 * This method is called before the parsing begins. Subclasses should
	 * create their statements here.
	 * 
	 * @throws SQLException
	 */
	protected abstract void prepareStatements() throws SQLException;
	
	/**
	 * This method is called before parsing the data, so that extending classes
	 * can skip any header parts contained in their respective files.
	 * 
	 * @param in BufferedReader associated to the opened file
	 * @throws IOException should not happen
	 */
	protected abstract void skipHeader(BufferedReader in) throws IOException; 	
	
	/**
	 * This method is called for each line in the file. The extending class should
	 * extract the information from it and insert it into the database.
	 * 
	 * @param lineParts
	 * @throws SQLException 
	 */
	protected abstract void newLine(String[] lineParts) throws SQLException;	
	
	/**
	 * This method is called after the parsing has finished. Subclasses should use
	 * this to close their statements.
	 * 
	 * @throws SQLException
	 */
	protected abstract void closeStatements() throws SQLException;
	
	
	/**
	 * @param dbcon
	 *            reference to DbConnection object
	 * @param file
	 *            the file to read
	 */
	protected AbstractParser(DbConnection dbcon, String file) {
		this.dbcon = dbcon;
		this.file = file;
		this.delimiter = null;
	}

	/**
	 * Start parsing previously opened file
	 * 
	 * @throws IOException if reading from file fails
	 * @throws SQLException if Postgres fails us
	 */
	public void parse() throws IOException, SQLException {
		
		// Open file with correct character encoding
		in = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "ISO-8859-15"));
		
		// Skip meaningless lines
		skipHeader(in);
		
		prepareStatements();
		
		// Parse
		String line;
		while ((line = in.readLine()) != null) {
			newLine(line.split(delimiter));
		}

		closeStatements();
		
		// Finish transaction
		// TODO Wenn autocommit an ist, macht commit() keinen Sinn. 
		// Wo haben wir es ausgemacht?! Nachdenken über transactions usw.
		dbcon.getConnection().commit();		
	}
}
