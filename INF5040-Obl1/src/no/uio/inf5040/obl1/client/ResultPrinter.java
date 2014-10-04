package no.uio.inf5040.obl1.client;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that generates outputs based on results from server.
 * 
 * @author Sveinung
 *
 */
public class ResultPrinter {

	private long totalTimeInMs;
	private List<String> results;

	public ResultPrinter() {
		totalTimeInMs = 0L;
		results = new ArrayList<String>();
	}

	/**
	 * Generates the output and prints it to the console.
	 * 
	 * @param method
	 *            - The method that was invoked.
	 * @param songId
	 *            - The ID of the song.
	 * @param userId
	 *            - The ID of the user.
	 * @param timesPlayed
	 *            - The result from the server.
	 * @param timeInMs
	 *            - Invocation time in milliseconds.
	 * @param fromCache
	 *            - {@code true} if result was retrieved from client cache,
	 *            {@code false} otherwise.
	 */
	public void onResponse(String method, String songId, String userId,
			int timesPlayed, long timeInMs, boolean fromCache) {
		StringBuilder sb = new StringBuilder();

		sb.append("Song ");
		sb.append(songId);
		sb.append(" played ");
		sb.append(timesPlayed);
		sb.append(timesPlayed == 1 ? " time" : " times");

		if ("getTimesPlayedByUser".equals(method)) {
			sb.append(" by user ");
			sb.append(userId);
		}

		sb.append(". (");
		sb.append(timeInMs);
		sb.append(" ms");

		totalTimeInMs += timeInMs;

		if (fromCache) {
			sb.append(", from client cache");
		}

		sb.append(")");

		printResult(sb.toString());
	}

	/**
	 * Prints the average invocation time based on previous results.
	 */
	public void printAverageInvocationTime() {
		double averageTimeInMs = totalTimeInMs / (double) results.size();
		printResult("Average invocation time: " + averageTimeInMs + " ms");
	}

	private void printResult(String result) {
		System.out.println(result);
		results.add(result);
	}

	/**
	 * Writes all the generated outputs to file.
	 * 
	 * @param filepath
	 *            - Path to the file.
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void printResultsToFile(String filepath)
			throws FileNotFoundException, UnsupportedEncodingException {
		FileHandler file = new FileHandler(filepath);
		file.write(results);
	}
}
