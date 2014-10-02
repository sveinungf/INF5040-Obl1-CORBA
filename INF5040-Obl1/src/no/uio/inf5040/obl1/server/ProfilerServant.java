package no.uio.inf5040.obl1.server;

import java.util.HashMap;

import no.uio.inf5040.obl1.tasteprofile.ProfilerPOA;
import no.uio.inf5040.obl1.tasteprofile.Song;
import no.uio.inf5040.obl1.tasteprofile.User;
import no.uio.inf5040.obl1.tasteprofile.UserHolder;

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
	
	
	private void init() {
		if (cacheEnabled) {
			System.out.print("Caching data...");
			parser.parseAndCache(songCache, userCache);
		}
	}

	
	public int getTimesPlayed(String song_id) {
		System.out.println("getTimesPlayed: id:" + song_id);
		if (cacheEnabled && songCache.containsKey(song_id)) {
			System.out.println("getTimesPlayed: Returning cached value");
			return songCache.get(song_id);
		}

		else {
			System.out.println("getTimesPlayed: Searching source file");
			return parser.parseGetTimesPlayed(song_id);
		}

	}

	
	public int getTimesPlayedByUser(String user_id, String song_id) {
		System.out.println("getTimesPlayedByUser: userId:" + user_id + "\tsongId:" + song_id);
		if (cacheEnabled && userCache.containsKey(user_id)) {
			System.out.println("getTimesPlayedByUser: Returning cached value");
			return getUserPlayCount(userCache.get(user_id), song_id);

		}

		else {
			System.out.println("getTimesPlayedByUser: Searching source file");
			return parser.parseGetTimesPlayedByUser(user_id, song_id);
		}
	}

	
	public int getUserProfile(String user_id, String song_id, UserHolder user) {
		System.out.println("getUserProfile: userId: " + user_id + " songId: " + song_id);
		if (cacheEnabled && userCache.containsKey(user_id)) {
			System.out.println("getUserProfile: Searching source file");
			return getUserPlayCount(userCache.get(user_id), song_id);
		}

		else {
			System.out.println("getUserProfile: Returning cached value");
			return parser.parseGetUserProfile(user_id, song_id, user);
		}
	}

	int getUserPlayCount(User usr, String songId) {
		for (Song s : usr.songs) {
			if (songId.equals(s.id))
				return s.play_count;
		}
		return -1;
	}
}