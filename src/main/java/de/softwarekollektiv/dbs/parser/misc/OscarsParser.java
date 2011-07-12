package de.softwarekollektiv.dbs.parser.misc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.AbstractParser;

public class OscarsParser extends AbstractParser {

	private final DbConnection dbcon;
	private final Map<String, Integer> movIdCache;
	
	private PreparedStatement oscarInsStmt;
	
	public OscarsParser(DbConnection dbcon, String file, Map<String, Integer> movIdCache) {
		super(dbcon, file);
		super.delimiter = "\t";
		super.skipLines = 1;
		
		this.dbcon = dbcon;
		this.movIdCache = movIdCache;
	}

	@Override
	protected void newLine(String[] lineParts) throws SQLException {
		Integer movId = movIdCache.get(lineParts[0]);
		String category = lineParts[1];
		int year = Integer.parseInt(lineParts[2]);
		
		// Don't need to check movId as I inserted relevant movies manually into
		// the database. Don't need to catch the NumberFormatException either, as
		// there won't be any.

		oscarInsStmt.setInt(1, movId);
		oscarInsStmt.setString(2, category);
		oscarInsStmt.setInt(3, year);
		oscarInsStmt.addBatch();
	}
	
	@Override
	protected void prepareStatements() throws SQLException {
		oscarInsStmt = dbcon.getConnection().prepareStatement(
				"INSERT INTO nominations VALUES(?, ?, ?);");
	}

	@Override
	protected void executeBatchStatements() throws SQLException {
		oscarInsStmt.executeBatch();
	}

	@Override
	protected void closeStatements() throws SQLException {
		oscarInsStmt.close();
	}

}
