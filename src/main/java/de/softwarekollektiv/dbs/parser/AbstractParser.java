package de.softwarekollektiv.dbs.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import de.softwarekollektiv.dbs.dbcon.DbConnection;

public abstract class AbstractParser implements Parser {
	protected static final Logger log = Logger.getLogger(AbstractParser.class);
	private final DbConnection dbcon;
	private final String file;
	
	private LineNumberReader in;

	protected String delimiter;
	protected long stopAfter;
	protected int skipLines;
	
	/**
	 * This method is called before the parsing begins. Subclasses should
	 * create their statements here.
	 * 
	 * @throws SQLException
	 */
	protected abstract void prepareStatements() throws SQLException;
	
	/**
	 * This method is called for each line in the file. The extending class should
	 * extract the information from it and insert it into the database.
	 * 
	 * @param lineParts line split by delimiter
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
		this.stopAfter = 0; 
		this.skipLines = 0;
	}

	/**
	 * Start parsing previously opened file
	 * 
	 * @throws IOException if reading from file fails
	 * @throws SQLException if Postgres fails us
	 */
	public void parse() throws IOException, SQLException {
		
		// Open file with correct character encoding
		in = new LineNumberReader(new InputStreamReader(
				new FileInputStream(file), "ISO-8859-15"));
		
		// Skip meaningless lines
		in.setLineNumber(skipLines);
		
		// Prepare the SQL channels
		prepareStatements();
			
		// Parse (with caching)
		int round = 5000;
		long lineCount = 0;
		String line;
		String[][] lines = new String[round][];
		while ((line = in.readLine()) != null && 
				((stopAfter == 0) || (lineCount < stopAfter))) {
			lines[(int) (lineCount++ % round)] = line.split(delimiter);
			
			if((lineCount % round) == 0) {
				newLines(lines, round);
				lineCount = 0;
			}
		}
		if((lineCount % round) > 0)
			newLines(lines, (int) (lineCount % round));	
		
		// Close
		in.close();
		closeStatements();			
	}
	
	protected void newLines(String[][] lines, int n) throws SQLException {
		for(int i = 0; i < n; ++i)
				newLine(lines[i]);

		dbcon.getConnection().commit();
	}
}
