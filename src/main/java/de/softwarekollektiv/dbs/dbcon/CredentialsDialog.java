package de.softwarekollektiv.dbs.dbcon;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import de.softwarekollektiv.dbs.app.MenuItem;

public class CredentialsDialog implements MenuItem {

	DbConnection dbcon;
	PrintStream out;
	BufferedReader in;
	
	CredentialsDialog(PrintStream out, InputStream in, DbConnection dbcon) {
		this.dbcon = dbcon;
		this.out = out;
		this.in = new BufferedReader(new InputStreamReader(in));
	}
	
	@Override
	public String getTitle() {
		return "Edit credentials";
	}

	@Override
	public String getDescription() {
		return "A short dialog will ask you for username and password for the database.";
	}

	@Override
	public boolean run() throws Exception {
		out.print("Enter username: ");
		String user = in.readLine();
		out.print("Enter password: ");
		String password = in.readLine();
		
		dbcon.setCredentials(user, password);
		return false;
	}

}
