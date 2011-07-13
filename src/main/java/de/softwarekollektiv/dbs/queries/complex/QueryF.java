package de.softwarekollektiv.dbs.queries.complex;

import java.io.PrintStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import de.softwarekollektiv.dbs.app.MenuItem;
import de.softwarekollektiv.dbs.dbcon.DbConnection;

class QueryF implements MenuItem {

	private final PrintStream out;
	private final DbConnection dbcon;
	
	private PreparedStatement actIdStmt;
	private PreparedStatement hasWorkedWithStmt;
	
	QueryF(PrintStream out, DbConnection dbcon) {
		this.out = out;
		this.dbcon = dbcon;
	}

	@Override
	public String getTitle() {
		return "Shortest path";
	}

	@Override
	public String getDescription() {
		return "Beantwortet Aufgabe 5.2.b:\n[...]\nErmitteln Sie nun die kürzeste Verbindung zwischen folgenden Paaren:\ni. Johnny Depp und Timothy Dalton\nii. Johnny Depp und August Diehl\niii. Bill Murray und Sylvester Stallone\niv. Edward Norton und Don Cheadle";
	}

	@Override
	public boolean run() throws Exception {
		
		String[][] pairs = {
				{"Depp, Johnny", "Dalton, Timothy"},
				{"Depp, Johnny", "Diehl, August"},
				{"Murray, Bill", "Stallone, Sylvester"},
				{"Norton, Edward", "Cheadle, Don"}
		};
		actIdStmt = dbcon.getConnection().prepareStatement("SELECT act_id FROM actors WHERE name = ?");
		hasWorkedWithStmt = dbcon.getConnection().prepareStatement(
				"SELECT act_id FROM features WHERE mov_id IN (SELECT mov_id FROM features WHERE act_id = ?)");
		
		out.println();
		for(String[] pair : pairs) {
			int startId = getId(pair[0]);
			int endId = getId(pair[1]);
			out.println("Shortest path between " + pair[0] + " and " + pair[1] + " is: " + shortestPath(startId, endId));
		}
		
		actIdStmt.close();
		hasWorkedWithStmt.close();
		return true;
	}
	
	/**
	 * BFS
	 */
	private int shortestPath(int startId, int endId) {
		return 0;
	}
	
	private List<Integer> getActorsWhoWorkedWith(int id) throws SQLException {
		return null;
	}
	
	private int getId(String name) throws SQLException {
		actIdStmt.setString(1, name);
		ResultSet rs = actIdStmt.executeQuery();
		// must exist
		rs.next();
		return rs.getInt(1);
	}
}
