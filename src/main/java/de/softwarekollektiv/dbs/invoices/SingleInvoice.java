package de.softwarekollektiv.dbs.invoices;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import de.softwarekollektiv.dbs.app.MenuItem;
import de.softwarekollektiv.dbs.dbcon.DbConnection;

public class SingleInvoice implements MenuItem {

	private PrintStream out;
	private DbConnection dbcon;
	private BufferedReader in;

	SingleInvoice(PrintStream out, InputStream in, DbConnection dbcon) {
		this.out = out;
		this.in = new BufferedReader(new InputStreamReader(in));
		this.dbcon = dbcon;
	}
	
	@Override
	public String getTitle() {
		return "Single rental (by ID)";
	}

	@Override
	public String getDescription() {
		return "Prints an invoice for a specific rental given its rental id.";
	}

	@Override
	public boolean run() throws Exception {
		out.print("Insert rental ID: ");
		String line = in.readLine();
		try {
			int id = Integer.parseInt(line);
		} catch (NumberFormatException e) {
			out.print("Not a valid rental ID.");
		}
		
		return true;
	}

}
