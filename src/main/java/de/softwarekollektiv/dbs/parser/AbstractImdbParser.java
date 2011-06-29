package de.softwarekollektiv.dbs.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract class AbstractImdbParser {

	private String delimiter = " ";

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
		this.in = new BufferedReader(new FileReader((file)));
		while (in.readLine() != firstStop)
			;
	}

	/**
	 * start parsing previous opened file
	 * @throws IOException if reading from file fails
	 */
	public void parse() throws IOException {
		newLine(in.readLine().split(delimiter));
	}

	public abstract void newLine(String[] lineParts);

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

}
