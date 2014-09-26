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
			// implementasjon gjenstår
			parser.parseAndCache(songCache, userCache);
		}

	}

	@Override
	public int getTimesPlayed(String song_id) {

		if (cacheEnabled && songCache.containsKey(song_id))
			return songCache.get(song_id);

		else
			return parser.parseGetTimesPlayed(song_id);

	}

	@Override
	public int getTimesPlayedByUser(String user_id, String song_id) {
		if (cacheEnabled && userCache.containsKey(user_id)) {
			return getUserPlayCount(userCache.get(user_id), song_id);

		}

		else
			return parser.parseGetTimesPlayedByUser(user_id, song_id);
	}

	@Override
	public int getUserProfile(String user_id, String song_id, UserHolder user) {

		if (cacheEnabled && userCache.containsKey(user_id))
			return getUserPlayCount(userCache.get(user_id), song_id);

		else
			return parser.parseGetUserProfile(user_id, song_id, user);
	}

	int getUserPlayCount(User usr, String songId) {
		for (Song s : usr.songs) {
			if (s.id.compareTo(songId) == 0)
				return s.play_count;
		}
		return -1;
	}
}