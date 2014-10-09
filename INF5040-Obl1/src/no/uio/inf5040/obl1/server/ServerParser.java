package no.uio.inf5040.obl1.server;

import java.util.*;
import java.io.*;

import no.uio.inf5040.obl1.common.SongImpl;
import no.uio.inf5040.obl1.common.UserImpl;
import no.uio.inf5040.obl1.tasteprofile.Song;
import no.uio.inf5040.obl1.tasteprofile.User;
import no.uio.inf5040.obl1.tasteprofile.UserHolder;


/**
 * Class for parsing from input file
 * 
 * @author halvor
 */
class ServerParser {
	private String fileName;
	BufferedReader reader;
	

	ServerParser(String fileName) {
		this.fileName = fileName;
		initReader();
	}

	
	/**
	 * Initiates buffered
	 */
	private void initReader() {
		try {
			reader = new BufferedReader(new FileReader(fileName));
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			e.printStackTrace(System.out);
		}
	}

	
	
	/**
	 * Method that parses from input file and caches relevant data
	 * <br>All songs and the top 1000 most active users are cached
	 * 
	 * @param songCache - {@link HashMap} for storing {@code Song} objects
	 * @param userCache - {@link HashMap} for storing {@code User} objects
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	void parseAndCache(HashMap<String, Integer> songCache,
			HashMap<String, User> userCache) throws NumberFormatException, IOException {

		int userTimesPlayed = 0;
		int totalPlayCount, playCount;
		totalPlayCount = playCount = 0;
		String[] parts = null;
		String line = null;
		String lastUserId = null;
		ArrayList<Song> userSongs = null;

		CachePriority cp = new CachePriority();

		while ((line = reader.readLine()) != null){

			parts = line.split("\\s+");
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
				addUser(lastUserId, userTimesPlayed, userSongs, userCache, cp);
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
		
		if(lastUserId != null)
			addUser(lastUserId, userTimesPlayed, userSongs, userCache, cp);
	}
	
	
	
	/**
	 * Adds a user to cache
	 * <br>Conditions: cache is not full or user is more active than currently least active user in cache
	 * 
	 * @param userId - ID of the user to add
	 * @param userPlayCount - total play count for user
	 * @param userSongs - {@link ArrayList} of users songs
	 * @param userCache - {@link HashMap} containing cached users
	 * @param cp - {@link CachePriority} containing info about cached users
	 */
	private void addUser(String userId, int userPlayCount, ArrayList<Song> userSongs, HashMap<String, User> userCache, CachePriority cp) {
		if (userCache.size() < 1000) {
			// sufficient space - cache user
			userCache.put(userId, new UserImpl(userId,
					userSongs.toArray(new Song[0])));
			cp.add(userId, userPlayCount);
		}

		else if (userPlayCount > cp.getLowest()) {
			// more active user - cache and remove least active user
			String toRemove = cp.pop();
			userCache.remove(toRemove);
			userCache.put(userId, new UserImpl(userId,
					userSongs.toArray(new Song[0])));
			cp.add(userId, userPlayCount);
		}

		
	}

	
	/**
	 * Reads the whole input file and computes how many times a specific song has been played
	 * @param songId - ID of the song
	 * @return total play count of song
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	int parseGetTimesPlayed(String songId) throws NumberFormatException, IOException {
		initReader();
		int timesPlayed = 0;
		String line;
		String parts[];
		while ((line = reader.readLine()) != null) {
			parts = line.split("\\s+");

			if (songId.equals(parts[1])) {
				timesPlayed += Integer.parseInt(parts[2]);
			}
		}
		reader.close();
		return timesPlayed;
	}

	
	/**
	 * Reads the whole input file and computes how many times a specific song has been played by a specific user
	 * @param userId - ID of the user
	 * @param songId - ID of the song
	 * @return play count for this song and user
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	int parseGetTimesPlayedByUser(String userId, String songId) throws NumberFormatException, IOException {
		initReader();
		int timesPlayed = 0;
		String lastUserId = null;
		String[] parts;
		String line;

		while ((line = reader.readLine()) != null) {
			parts = line.split("\\s+");

			if (userId.equals(lastUserId) && !userId.equals(parts[0]))
				break;

			if (userId.equals(parts[0]) && songId.equals(parts[1]))
				timesPlayed += Integer.parseInt(parts[2]);

			lastUserId = parts[0];
		}

		return timesPlayed;
	}

	
	
	/**
	 * Reads the whole input file and computes:
	 * <br>-how many times a specific song has been played by a specific user
	 * <br>-profile of the user
	 * 
	 * @param userId - ID of the user
	 * @param songId - ID of the song
	 * @param user - {@link UserHolder} object for containing profile
	 * @return play count for this song and user
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	int parseGetUserProfile(String userId, String songId, UserHolder user) throws NumberFormatException, IOException {
		initReader();
		int timesPlayed = 0;
		String lastUserId = null;
		String line;
		String[] parts;

		ArrayList<Song> userSongs = new ArrayList<Song>();

		while ((line = reader.readLine()) != null) {
			parts = line.split("\\s+");

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
