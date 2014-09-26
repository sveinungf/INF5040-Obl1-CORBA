package no.uio.inf5040.obl1.server;


import java.util.*;
import java.io.*;

import no.uio.inf5040.obl1.server.SongImpl;
import no.uio.inf5040.obl1.server.UserImpl;
import no.uio.inf5040.obl1.tasteprofile.Song;
import no.uio.inf5040.obl1.tasteprofile.User;
import no.uio.inf5040.obl1.tasteprofile.UserHolder;

class ServerParser {
	Scanner scan;

	ServerParser(String fileName) {
		try {
			scan = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
		}
	}

	// implementasjon av caching gjenstår
	void parseAndCache(HashMap<String, Integer> songCache,
			HashMap<String, User> userCache) {

	}

	int parseGetTimesPlayed(String songId) {
		int timesPlayed = 0;
		String[] parts = null;

		while (scan.hasNextLine()) {
			parts = scan.nextLine().split("\\s+");

			if (songId.compareTo(parts[1]) == 0) {
				timesPlayed += Integer.parseInt(parts[2]);
			}
		}

		return timesPlayed;
	}

	int parseGetTimesPlayedByUser(String userId, String songId) {
		int timesPlayed = 0;
		String[] parts;

		while (scan.hasNextLine()) {
			parts = scan.nextLine().split("\\s+");

			if ((userId.compareTo(parts[0]) == 0)
					&& (songId.compareTo(parts[1]) == 0))
				timesPlayed += Integer.parseInt(parts[2]);
		}

		return timesPlayed;
	}

	int parseGetUserProfile(String userId, String songId, UserHolder user) {
		int timesPlayed = 0;
		String[] parts;

		ArrayList<Song> userSongs = new ArrayList<Song>();

		while (scan.hasNextLine()) {
			parts = scan.nextLine().split("\\s+");

			if ((userId.compareTo(parts[0]) == 0)
					&& (songId.compareTo(parts[1]) == 0))
				timesPlayed += Integer.parseInt(parts[2]);

			userSongs.add(new SongImpl(parts[1], Integer.parseInt(parts[2])));
		}

		user.value = new UserImpl(userId, (Song[]) userSongs.toArray());

		return timesPlayed;
	}
}