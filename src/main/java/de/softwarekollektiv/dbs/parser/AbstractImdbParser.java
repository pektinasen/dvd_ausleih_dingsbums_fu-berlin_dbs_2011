package de.softwarekollektiv.dbs.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.softwarekollektiv.dbs.Registry;

public abstract class AbstractImdbParser implements ImdbParser {

	protected String delimiter = " ";

	protected String firstStop;

	private BufferedReader in;

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

		this.in = new BufferedReader(new InputStreamReader(new FileInputStream(
				file), "ISO-8859-15"));
		System.out.println(firstStop);
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
		try {
			String line;
			System.out.println(delimiter);
			PreparedStatement st = Registry.getConnection().prepareStatement(
					"INSERT INTO movies VALUES (?, ?);");
			while ((line = in.readLine()) != null) {
				newLine(line.split(delimiter), st);
			}
			Registry.getConnection().commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public abstract void newLine(String[] lineParts, PreparedStatement st) throws SQLException;

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
