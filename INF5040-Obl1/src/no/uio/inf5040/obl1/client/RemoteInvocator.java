package no.uio.inf5040.obl1.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.uio.inf5040.obl1.tasteprofile.Profiler;
import no.uio.inf5040.obl1.tasteprofile.Song;
import no.uio.inf5040.obl1.tasteprofile.User;
import no.uio.inf5040.obl1.tasteprofile.UserHolder;

public class RemoteInvocator implements LineReadListener {

	private boolean cacheUsers;
	private List<String> results;
	private Map<String, User> cache;
	private Profiler profiler;

	public RemoteInvocator(Profiler profiler, boolean cacheUsers) {
		this.cacheUsers = cacheUsers;
		results = new ArrayList<String>();
		cache = new HashMap<String, User>();
		this.profiler = profiler;
	}

	public String[] getResults() {
		return results.toArray(new String[0]);
	}

	@Override
	public void onLineRead(String[] fields) {
		String method = fields[0];
		String arg1 = fields[1];
		String arg2 = fields.length > 2 ? fields[2] : null;

		String output = getOutput(method, arg1, arg2);
		results.add(output);
		System.out.println(output);
	}

	private String getOutput(String method, String arg1, String arg2) {
		String userId, songId;
		StringBuilder sb = new StringBuilder();
		int timesPlayed = 0;
		long before = System.nanoTime();
		long after = 0L;

		switch (method) {
		case "getTimesPlayed":
			songId = arg1;

			timesPlayed = profiler.getTimesPlayed(songId);
			after = System.nanoTime();

			sb.append("Song ");
			sb.append(songId);
			sb.append(" played ");
			sb.append(timesPlayed);
			sb.append(" times.");
			break;
		case "getTimesPlayedByUser":
			userId = arg1;
			songId = arg2;

			if (cacheUsers) {
				User user = cache.get(userId);

				if (user == null) {
					UserHolder userHolder = new UserHolder();
					timesPlayed = profiler.getUserProfile(userId, songId,
							userHolder);

					cache.put(userId, userHolder.value);
				} else {
					for (Song song : user.songs) {
						if (song.id.equals(songId)) {
							timesPlayed = song.play_count;
							break;
						}
					}
				}
			} else {
				timesPlayed = profiler.getTimesPlayedByUser(userId, songId);
			}

			after = System.nanoTime();

			sb.append("Song ");
			sb.append(songId);
			sb.append(" played ");
			sb.append(timesPlayed);
			sb.append(" times by user ");
			sb.append(userId);
			sb.append(".");
			break;
		}
		
		sb.append(" (");
		sb.append((after - before) / 1000);
		sb.append(" ms)");

		return sb.toString();
	}
}
