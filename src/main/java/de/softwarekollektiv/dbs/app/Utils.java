package de.softwarekollektiv.dbs.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Utils {
	public static String fileToString(String file) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader in = new BufferedReader(new FileReader(file));

		String line = null;
		while ((line = in.readLine()) != null) {
			sb.append(line + "\n");
		}
		in.close();

		return sb.toString();
	}
}
