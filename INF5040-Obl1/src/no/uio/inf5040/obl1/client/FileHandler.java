package no.uio.inf5040.obl1.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class FileHandler {

	private String filepath;

	public FileHandler(String filepath) {
		this.filepath = filepath;
	}

	public void readTo(LineReadListener callback) throws IOException {
		FileReader in = new FileReader(filepath);
		BufferedReader reader = new BufferedReader(in);

		String line;
		while ((line = reader.readLine()) != null) {
			callback.onLineRead(line.split("\\s+"));
		}

		reader.close();
	}

	public void write(String[] lines) throws FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(filepath, "UTF-8");

		for (String line : lines) {
			writer.println(line);
		}

		writer.close();
	}
}
