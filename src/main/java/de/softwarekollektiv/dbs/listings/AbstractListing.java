package de.softwarekollektiv.dbs.listings;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;

import de.softwarekollektiv.dbs.app.MenuItem;
import de.softwarekollektiv.dbs.app.PrettyTablePrinter;
import de.softwarekollektiv.dbs.dbcon.DbConnection;

abstract class AbstractListing implements MenuItem {
	
	private final PrintStream out;
	private final DbConnection dbcon;
	
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

	AbstractListing(PrintStream out, DbConnection dbcon) {
		this.out = out;
		this.dbcon = dbcon;
	}

	@Override
	public boolean run() throws Exception {

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

		Statement sm = dbcon.getConnection().createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = sm.executeQuery(sb.toString());

		PrettyTablePrinter.print(out, rs, fields);

		rs.close();
		sm.close();

		return true;
	}
}
