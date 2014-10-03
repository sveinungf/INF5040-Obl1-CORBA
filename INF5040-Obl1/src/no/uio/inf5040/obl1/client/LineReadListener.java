package no.uio.inf5040.obl1.client;

/**
 * An interface defining the callback method used by {@link FileHandler} when
 * reading files.
 * 
 * @author Sveinung
 *
 */
public interface LineReadListener {

	/**
	 * The method called when a line has been read from file.
	 * 
	 * @param lineNumber
	 *            - The current line number.
	 * @param fields
	 *            - An array of fields from the line read.
	 */
	void onLineRead(int lineNumber, String[] fields);
}
