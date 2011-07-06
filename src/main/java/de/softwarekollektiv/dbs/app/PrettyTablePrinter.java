package de.softwarekollektiv.dbs.app;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrettyTablePrinter {
	
	public static void print(PrintStream out, ResultSet rs, String[] fields) throws SQLException {
		// calculate column width
		// In case ResultSet is not scrollable, we fall back to variable column width
		// indicated by widths == {0, 0, 0, ... }
		int[] widths;
		if(rs.getType() == ResultSet.TYPE_FORWARD_ONLY)
			widths = new int[fields.length];
		else
			widths = calculateColumnWidth(rs, fields);
				
		out.print("# ");
		for(int i = 0; i < fields.length; ++i) {
			out.print(padRight(fields[i], widths[i]));
			out.print(" # ");
		}
		out.println();
		
		
		while(rs.next()) {
			out.print("# ");
			for(int i = 0; i < fields.length; ++i) {
				String value = rs.getString(fields[i]);
				out.print(padRight(value, widths[i]));
				out.print(" # ");
			}
			out.println();
		}
	}
	
	private static String padRight(String s, int n) {
		if(n > 0)
			return String.format("%1$-" + n + "s", s); 
		else
			return s;
	}


	private static int[] calculateColumnWidth(ResultSet rs, String[] fields) throws SQLException {
		int[] retval = new int[fields.length];
		for(int i = 0; i < fields.length; ++i)
			retval[i] = fields[i].length();
				
		while(rs.next()) {
			for(int i = 0; i < fields.length; ++i) {
				String value = rs.getString(fields[i]);
				if(retval[i] < value.length())
					retval[i] = value.length();
			}
		}
		rs.beforeFirst();
				
		return retval;
	}	
}
