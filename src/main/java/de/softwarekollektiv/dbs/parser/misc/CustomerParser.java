package de.softwarekollektiv.dbs.parser.misc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.AbstractParser;


public class CustomerParser extends AbstractParser {

	public CustomerParser(DbConnection dbcon, String file) {
		super(dbcon, file);
		super.delimiter = ",";
		super.skipFirstPart = false;
	}

	@Override
	public void newLine(String[] lineParts, PreparedStatement st) {
		int id = Integer.parseInt(lineParts[0]);
		String name = lineParts[1];
		String surname = lineParts[2];
		String street = lineParts[3];
		String zip = lineParts[4];
		String city = lineParts[5];
		String phone = lineParts[6];
		
		try {
			st.setInt(0, id);
			st.setString(1, name);
			st.setString(2, surname);
			st.setString(3, street);
			st.setString(4, zip);
			st.setString(5, city);
			st.setString(6, phone);	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
