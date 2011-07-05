package de.softwarekollektiv.dbs.parser;

import java.io.IOException;

public interface ImdbParser {
	
	public void open() throws IOException;
	
	public void parse();

	public void close();
}
