package de.softwarekollektiv.dbs.queries;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;

import de.softwarekollektiv.dbs.app.PrettyTablePrinter;
import de.softwarekollektiv.dbs.dbcon.DbConnection;

public abstract class AbstractSQLQuery {
	private final PrintStream out;
	private final DbConnection dbcon;
	
	/**
	 * Must be implemented by subclasses.
	 * 
	 * @return the SQL query to execute
	 */
	protected abstract String getQuery();
	
	/**
	 * Must be implemented by subclasses.
	 * 
	 * @return the fields contained in the ResultSet
	 */
	protected abstract String[] getResultFields();
	
	public AbstractSQLQuery(PrintStream out, DbConnection dbcon) {
		this.out = out;
		this.dbcon = dbcon;
	}
	
	public boolean run() throws Exception {
		Statement sm = dbcon.getConnection().createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = sm.executeQuery(getQuery());

		PrettyTablePrinter.print(out, rs, getResultFields());

		rs.close();
		sm.close();

		return true;
	}	
}
