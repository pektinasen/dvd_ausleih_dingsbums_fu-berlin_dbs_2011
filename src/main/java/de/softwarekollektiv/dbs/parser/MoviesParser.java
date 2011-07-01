package de.softwarekollektiv.dbs.parser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import de.softwarekollektiv.dbs.Registry;
import de.softwarekollektiv.dbs.model.Movie;




public class MoviesParser extends AbstractImdbParser implements ImdbParser{

	private String firstStop = "title	year	category";
	
	public MoviesParser() {
		super();
		/*
		 * mehrere tabs
		 */
		super.delimiter = "\t+";
		super.firstStop = this.firstStop;
	}
	
	
	@Override
	public void newLine(String[] lineParts, PreparedStatement st){
		Movie m = new Movie();
		m.setTitle(lineParts[0]);
		m.setReleaseDate(lineParts[1]);
		try {
		st.setString(1, m.getTitle());
		st.setInt(2, m.getReleaseDate());
			st.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


}
