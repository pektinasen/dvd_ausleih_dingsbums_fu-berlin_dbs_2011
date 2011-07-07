package de.softwarekollektiv.dbs.queries.listings;

import java.io.PrintStream;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.queries.AbstractSQLQuery;

abstract class AbstractListing extends AbstractSQLQuery {
	
	/**
	 * Must be implemented by subclasses.
	 * 
	 * @return the table to SELECT from
	 */
	protected abstract String getTable();
	
	/**
	 * Must be implemented by subclasses.
	 * 
	 * @return the fields to SELECT from the table
	 */
	protected abstract String[] getFields();
	
	protected String getQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");

		String[] fields = getFields();
		for (int i = 0; i < fields.length - 1; ++i) {
			sb.append(fields[i]);
			sb.append(",");
		}
		sb.append(fields[fields.length - 1]);

		sb.append(" FROM ");
		sb.append(getTable());
		sb.append(";");
		
		return sb.toString();
	}

	AbstractListing(PrintStream out, DbConnection dbcon) {
		super(out, dbcon);
	}
}
