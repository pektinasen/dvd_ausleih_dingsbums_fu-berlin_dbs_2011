package de.softwarekollektiv.dbs.queries.complex;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.softwarekollektiv.dbs.app.MenuItem;
import de.softwarekollektiv.dbs.app.Utils;
import de.softwarekollektiv.dbs.dbcon.DbConnection;

public class QueryFHack implements MenuItem {

	private PrintStream out;

	public QueryFHack(PrintStream out, DbConnection dbcon) {
		// TODO Auto-generated constructor stub
		this.out = out;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Shortest Path Hack";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return getTitle();
	}

	@Override
	public boolean run() throws Exception {
		String[][] pairs = { { "Johnny Depp", "Timothy Dalton" },
				{ "Johnny Depp", "August Diehl" },
				{ "Bill Murray (I)", "Sylvester Stallone" },
				{ "Edward Norton (I)", "Don Cheadle" } };

		for (String[] pair : pairs) {
			out.println("");
			shortestPath(pair[0], pair[1]);
			out.println("");
		}

		out.println();

		return true;
	}

	private void shortestPath(String a, String b) throws Exception {

		String postParams = "game=0&a="+a+"&b="+b;

		URL url = new URL("http://oracleofbacon.org/cgi-bin/movielinks");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.addRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.addRequestProperty("Content-Length",
				Integer.toString(postParams.length()));
		conn.setDoInput(true);
		conn.setDoOutput(true);

		// Send request
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();

		// Get Response
		if (conn.getResponseCode() == 200) {
			String response = Utils.toString(conn.getInputStream());
			List<String> results = findActors(response);
			for (int i = 0; i < results.size(); i++) {
				if (i % 2 == 0) {
					if (i < results.size() - 1) {
						out.println("Actor: " + results.get(i));
						out.println("in\t" + results.get(i + 1));
						i++;
					}
				} else {
					i++;
				}
			}
			out.print("Actor: " + results.get(results.size() - 1));
		} else {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				out.println(line);
			}
		}
	}

	private List<String> findActors(String response) {
		Pattern actor = Pattern
				.compile("(?:<span class=\"actor\">[^<]*<a[^>]*?>([^<]+)<\\/a>)[^<]*<\\/span>");
		Pattern film = Pattern
				.compile("(?:<span class=\"film\">[^<]*<a[^>]*?>([^<]+)<\\/a>)[^<]*<\\/span>");

		Matcher aMatcher = actor.matcher(response);
		Matcher fMatcher = film.matcher(response);

		List<String> result = new ArrayList<String>();
		while (aMatcher.find()) {
			result.add(aMatcher.group(1));
			if (fMatcher.find()) {
				result.add(fMatcher.group(1));

			}
		}
		return result;
	}

}
