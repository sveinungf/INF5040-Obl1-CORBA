package no.uio.inf5040.obl1.server;

import java.util.HashMap;

import no.uio.inf5040.obl1.tasteprofile.ProfilerPOA;
import no.uio.inf5040.obl1.tasteprofile.Song;
import no.uio.inf5040.obl1.tasteprofile.User;
import no.uio.inf5040.obl1.tasteprofile.UserHolder;

/**
 * @author halvor
 *
 */
public class ProfilerServant extends ProfilerPOA {

	boolean cacheEnabled;

	ServerParser parser;
	HashMap<String, Integer> songCache;
	HashMap<String, User> userCache;

	ProfilerServant(String fileName, boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
		parser = new ServerParser(fileName);
		songCache = new HashMap<String, Integer>();
		userCache = new HashMap<String, User>();
		init();
	}

	/**
	 * Initiates the ProfilerServant <br>
	 * Computes cache of songs and most active users if caching is enabled
	 */
	private void init() {
		if (cacheEnabled) {
			System.out.println("Caching data...");

			try {
				parser.parseAndCache(songCache, userCache);
			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
			}

			System.out.println("Caching done");
		}
	}

	@Override
	public int getTimesPlayed(String song_id) {
		addDelay();
		System.out.print("getTimesPlayed: id:" + song_id);

		if (cacheEnabled && songCache.containsKey(song_id)) {
			System.out.println(" - Returning cached value");
			return songCache.get(song_id);
		}

		else {
			System.out.println(" - Searching source file");
			int timesPlayed = -1;

			try {
				timesPlayed = parser.parseGetTimesPlayed(song_id);
			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			}

			return timesPlayed;

		}

	}

	@Override
	public int getTimesPlayedByUser(String user_id, String song_id) {
		addDelay();
		System.out.print("getTimesPlayedByUser: userId:" + user_id
				+ "\tsongId:" + song_id);

		if (cacheEnabled && userCache.containsKey(user_id)) {
			System.out.println(" - Returning cached value");
			int timesPlayed = getUserPlayCount(userCache.get(user_id), song_id);
			System.out.println("Returning value: " + timesPlayed);
			return timesPlayed;

		}

		else {
			System.out.println(" - Searching source file");
			int timesPlayed = -1;
			
			try {
				timesPlayed = parser.parseGetTimesPlayedByUser(user_id, song_id);
			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Returning value: " + timesPlayed);
			
			return timesPlayed;
		}
	}

	@Override
	public int getUserProfile(String user_id, String song_id, UserHolder user) {
		addDelay();
		System.out.print("getUserProfile: userId: " + user_id + " songId: "
				+ song_id);

		if (cacheEnabled && userCache.containsKey(user_id)) {
			System.out.println(" - Returning cached value");
			user.value = userCache.get(user_id);
			return getUserPlayCount(user.value, song_id);
		}

		else {
			System.out.println(" - Searching source file");
			int timesPlayed = -1;
			try {
				timesPlayed = parser.parseGetUserProfile(user_id, song_id, user);
			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
				e.printStackTrace();
			}
			System.out.println("getUserProfile: timesPlayed: " + timesPlayed);
			
			return timesPlayed;
		}
	}

	/**
	 * Gets a users play count for a given song
	 * 
	 * @param user
	 *            - {@link User} object containing user
	 * @param songId
	 *            - ID of song to lookup
	 * @return play count for the given song
	 */
	int getUserPlayCount(User user, String songId) {
		for (Song s : user.songs) {
			if (songId.equals(s.id))
				return s.play_count;
		}
		return 0;
	}

	/**
	 * Adds a delay of 60 ms to simulate network latency
	 */
	private void addDelay() {
		try {
			Thread.sleep(60);
		} catch (InterruptedException e) {
			System.err.println("InterruptedException: " + e.getMessage());
			e.printStackTrace(System.out);
		}
	}
}