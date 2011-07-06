package de.softwarekollektiv.dbs.parser;

import java.io.IOException;
import java.sql.SQLException;

public interface Parser {
	public void parse() throws IOException, SQLException;
}
