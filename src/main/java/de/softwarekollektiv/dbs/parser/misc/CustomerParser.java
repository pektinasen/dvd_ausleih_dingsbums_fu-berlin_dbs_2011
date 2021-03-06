package de.softwarekollektiv.dbs.parser.misc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.AbstractParser;

public class CustomerParser extends AbstractParser {
	private final DbConnection dbcon;
	
	private PreparedStatement customerStatement;

	public CustomerParser(DbConnection dbcon, String file) throws SQLException {
		super(dbcon, file);
		super.delimiter = ",";
		super.skipLines = 1;
		
		this.dbcon = dbcon;
	}

	@Override
	protected void newLine(String[] lineParts) throws SQLException {
		int id = Integer.parseInt(lineParts[0]);
		String surename = lineParts[1];
		String forename = lineParts[2];
		String street = lineParts[3];
		String zip = lineParts[4];
		String city = lineParts[5];
		String phone = lineParts[6];
		
		customerStatement.setInt(1, id);
		customerStatement.setString(2, surename);
		customerStatement.setString(3, forename);
		customerStatement.setString(4, street);
		customerStatement.setString(5, zip);
		customerStatement.setString(6, city);
		customerStatement.setString(7, phone);	
		
		customerStatement.addBatch();
	}

	@Override
	protected void prepareStatements() throws SQLException {
		customerStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO customers VALUES (?, ?, ?, ?, ?, ?, ?)"
			);
	}

	@Override
	protected void closeStatements() throws SQLException {
		customerStatement.close();
	}

	@Override
	protected void executeBatchStatements() throws SQLException {
		customerStatement.executeBatch();
	}
}
