package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import org.apache.log4j.Logger;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.Parser;

/*
 * TODO es werden nur korrekt formatierte Datumse zugelassen
 * <Tag:int> <Month:String> <year:int>
 */
public class ReleaseDateParser extends AbstractImdbParser implements Parser {
	private static final Logger log = Logger.getLogger(ReleaseDateParser.class);
	private PreparedStatement updateDateStatement;

	DbConnection dbcon;

	public ReleaseDateParser(DbConnection dbcon, String file)
			throws SQLException {
		super(dbcon, file);
		super.firstStop = "==================";
		super.delimiter = "\t+";
		this.dbcon = dbcon;
		updateDateStatement = dbcon
				.getConnection()
				.prepareStatement(
						"UPDATE movies SET release_Date = ?, region = ? WHERE title = ?");
	}

	@Override
	protected void newLine(String[] lineParts) {

		if (!(lineParts[0].contains("2010") || lineParts[0].contains("2011"))) {
			return;
		}

		if (!lineParts[1].contains("US")) {
			return;
		}

		String dateString = lineParts[1].split(":")[1];
		String region = lineParts[1].split(":")[0];
		String[] dateParts = dateString.split(" ");
		log.debug(Arrays.toString(lineParts));
		log.debug(Arrays.toString(dateParts));
		if (dateParts.length == 3) {
			try {

				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParts[0]));
				java.util.Date date = null;
				try {
					date = new SimpleDateFormat("MMM", Locale.ENGLISH)
							.parse(dateParts[1].substring(0, 3));
				} catch (ParseException e) {
				}
				Calendar month = Calendar.getInstance();
				month.setTime(date);
				cal.set(Calendar.MONTH, month.get(Calendar.MONTH));
				cal.set(Calendar.YEAR, Integer.parseInt(dateParts[2]));

				updateDateStatement.setDate(1, new Date(cal.getTimeInMillis()));
				updateDateStatement.setString(2, region.substring(0, 2));
				updateDateStatement.setString(3, lineParts[0]);
				updateDateStatement.execute();
			} catch (SQLException se) {

			} catch (StringIndexOutOfBoundsException sie) {
				log.debug(Arrays.toString(lineParts), sie);
			}
		}

	}

	@Override
	public void close() {
		try {
			Statement delete = dbcon.getConnection().createStatement();
			delete.execute("DELETE FROM movies WHERE region != 'US'");
			Statement alter = dbcon.getConnection().createStatement();
			alter.execute("ALTER TABLE movies DROP COLUMN region");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.close();
	}

}
