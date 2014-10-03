package no.uio.inf5040.obl1.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * A simple class for reading and writing {@code String} objects to files.
 * 
 * @author Sveinung
 *
 */
public class FileHandler {

	private String filepath;

	public FileHandler(String filepath) {
		this.filepath = filepath;
	}

	/**
	 * Method for reading files with fields separated by whitespace. Reads a
	 * file line by line and sends a {@code String} array to the supplied
	 * {@link LineReadListener} implementation for each line read.
	 * 
	 * @param callback
	 *            - Callback for each line read.
	 * @throws IOException
	 */
	public void readTo(LineReadListener callback) throws IOException {
		FileReader in = new FileReader(filepath);
		BufferedReader reader = new BufferedReader(in);

		String line;
		while ((line = reader.readLine()) != null) {
			callback.onLineRead(line.split("\\s+"));
		}

		reader.close();
	}

	/**
	 * Method for writing lines to a file. Each {@code String} in {@code lines}
	 * is written to a separate line sequentially.
	 * 
	 * @param lines
	 *            - An array of the lines to write to the file.
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void write(String[] lines) throws FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(filepath, "UTF-8");

		for (String line : lines) {
			writer.println(line);
		}

		writer.close();
	}
}
