package no.uio.inf5040.obl1.client;

import java.util.HashMap;
import java.util.Map;

import no.uio.inf5040.obl1.tasteprofile.Profiler;
import no.uio.inf5040.obl1.tasteprofile.Song;
import no.uio.inf5040.obl1.tasteprofile.User;
import no.uio.inf5040.obl1.tasteprofile.UserHolder;

/**
 * The class that invokes methods on the server using CORBA.
 * 
 * @author Sveinung
 *
 */
public class RemoteInvocator implements LineReadListener {

	private boolean cacheUsers;
	private Map<String, User> cache;
	private Profiler profiler;
	private ResultPrinter resultPrinter;

	/**
	 * Constructor for {@code RemoteInvocator}.
	 * 
	 * @param profiler
	 *            - A {@link Profiler} implementation.
	 * @param cacheUsers
	 *            - {@code true} if the client should cache users, {@code false}
	 *            otherwise.
	 * @param resultPrinter
	 *            - An object that prints output from the results.
	 */
	public RemoteInvocator(Profiler profiler, boolean cacheUsers,
			ResultPrinter resultPrinter) {
		this.cacheUsers = cacheUsers;
		cache = new HashMap<String, User>();
		this.profiler = profiler;
		this.resultPrinter = resultPrinter;
	}

	@Override
	public void onLineRead(int lineNumber, String[] fields) {
		String method = fields[0];
		String arg1 = fields[1];
		String arg2 = fields.length > 2 ? fields[2] : null;

		System.out.println(lineNumber + ": Calling " + method);
		invoke(method, arg1, arg2);
	}

	/**
	 * Calls on a method on the remote server. The result is sent to the
	 * {@link ResultPrinter} object.
	 * 
	 * @param method
	 *            - The name of the method to be called.
	 * @param args
	 *            - The arguments to the method.
	 */
	private void invoke(String method, String... args) {
		String userId = null;
		String songId = null;
		boolean fromCache = false;
		int result = 0;
		long before = System.nanoTime();

		switch (method) {
		case "getTimesPlayed":
			songId = args[0];
			result = profiler.getTimesPlayed(songId);
			break;
		case "getTimesPlayedByUser":
			userId = args[0];
			songId = args[1];

			if (cacheUsers) {
				User user = cache.get(userId);

				if (user == null) {
					UserHolder userHolder = new UserHolder();

					result = profiler
							.getUserProfile(userId, songId, userHolder);

					cache.put(userId, userHolder.value);
				} else {
					fromCache = true;

					for (Song song : user.songs) {
						if (songId.equals(song.id)) {
							result = song.play_count;
							break;
						}
					}
				}
			} else {
				result = profiler.getTimesPlayedByUser(userId, songId);
			}

			break;
		}

		long after = System.nanoTime();
		long timeInMs = (after - before) / 1000000;

		resultPrinter.onResponse(method, songId, userId, result, timeInMs,
				fromCache);
	}
}
