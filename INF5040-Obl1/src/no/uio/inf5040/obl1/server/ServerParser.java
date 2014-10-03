package no.uio.inf5040.obl1.server;

import java.util.*;
import java.io.*;

import no.uio.inf5040.obl1.server.SongImpl;
import no.uio.inf5040.obl1.server.UserImpl;
import no.uio.inf5040.obl1.tasteprofile.Song;
import no.uio.inf5040.obl1.tasteprofile.User;
import no.uio.inf5040.obl1.tasteprofile.UserHolder;

class ServerParser {
	private String fileName;
	Scanner scan;

	ServerParser(String fileName) {
		this.fileName = fileName;
		initScanner();
	}

	private void initScanner() {
		try {
			scan = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
		}
	}

	void parseAndCache(HashMap<String, Integer> songCache,
			HashMap<String, User> userCache) {

		int userTimesPlayed = 0;
		int totalPlayCount, playCount;
		String[] parts = null;
		String lastUserId = null;
		ArrayList<Song> userSongs = null;

		CachePriority pl = new CachePriority();

		while (scan.hasNextLine()) {

			parts = scan.nextLine().split("\\s+");
			// parts[0] = userId, parts[1] = songId, parts[2] = playCount

			if (lastUserId == null) {
				lastUserId = parts[0];
				userSongs = new ArrayList<Song>();
			}

			playCount = Integer.parseInt(parts[2]);

			/* caching of songs */
			if (songCache.containsKey(parts[1])) {
				// song already exists in cache, update play count
				totalPlayCount = songCache.get(parts[1]).intValue() + playCount;
				songCache.put(parts[1], totalPlayCount);
			}

			else {
				// new song - cache
				songCache.put(parts[1], playCount);
			}

			/* caching of users */
			if (!lastUserId.equals(parts[0])) {
				// new userId - cache or throw away user data

				if (userCache.size() < 1000) {
					// sufficient space - cache data of old user
					userCache.put(lastUserId, new UserImpl(lastUserId,
							userSongs.toArray(new Song[0])));
					pl.add(lastUserId, userTimesPlayed);
				}

				else if (userTimesPlayed > pl.getLowest()) {
					// more active user - cache and remove least active user
					String toRemove = pl.pop();
					userCache.remove(toRemove);
					userCache.put(lastUserId, new UserImpl(lastUserId,
							userSongs.toArray(new Song[0])));
					pl.add(lastUserId, userTimesPlayed);
				}

				userSongs = new ArrayList<Song>();
				userTimesPlayed = playCount;
			}

			else {
				// old userId - update users times played and add song to users
				userSongs.add(new SongImpl(parts[1], playCount));
				userTimesPlayed += playCount;
			}

			lastUserId = parts[0];
		}
	}

	int parseGetTimesPlayed(String songId) {
		initScanner();
		int timesPlayed = 0;
		String[] parts = null;

		while (scan.hasNextLine()) {
			parts = scan.nextLine().split("\\s+");

			if (songId.equals(parts[1])) {
				timesPlayed += Integer.parseInt(parts[2]);
			}
		}

		return timesPlayed;
	}

	int parseGetTimesPlayedByUser(String userId, String songId) {
		initScanner();
		int timesPlayed = 0;
		String lastUserId = null;
		String[] parts;

		while (scan.hasNextLine()) {
			parts = scan.nextLine().split("\\s+");

			if (userId.equals(lastUserId) && !userId.equals(parts[0]))
				break;

			if (userId.equals(parts[0]) && songId.equals(parts[1]))
				timesPlayed += Integer.parseInt(parts[2]);

			lastUserId = parts[0];
		}

		return timesPlayed;
	}

	int parseGetUserProfile(String userId, String songId, UserHolder user) {
		initScanner();
		int timesPlayed = 0;
		String lastUserId = null;
		String[] parts;

		ArrayList<Song> userSongs = new ArrayList<Song>();

		while (scan.hasNextLine()) {
			parts = scan.nextLine().split("\\s+");

			if (userId.equals(lastUserId) && !userId.equals(parts[0]))
				break;

			if (userId.equals(parts[0])) {
				if (songId.equals(parts[1]))
					timesPlayed = Integer.parseInt(parts[2]);

				userSongs
						.add(new SongImpl(parts[1], Integer.parseInt(parts[2])));
			}

			lastUserId = parts[0];
		}

		user.value = new UserImpl(userId, userSongs.toArray(new Song[0]));

		return timesPlayed;
	}
}
