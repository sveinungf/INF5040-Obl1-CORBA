package no.uio.inf5040.obl1.client;

import java.util.HashMap;
import java.util.Map;

import no.uio.inf5040.obl1.tasteprofile.Profiler;
import no.uio.inf5040.obl1.tasteprofile.ProfilerHelper;
import no.uio.inf5040.obl1.tasteprofile.Song;
import no.uio.inf5040.obl1.tasteprofile.User;
import no.uio.inf5040.obl1.tasteprofile.UserHolder;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class ProfilerClient {

	private boolean cacheUsers;
	private Map<String, User> cache;
	private Profiler profiler;

	public ProfilerClient(Profiler profiler, boolean cacheUsers) {
		this.cacheUsers = cacheUsers;
		cache = new HashMap<String, User>();
		this.profiler = profiler;
	}

	public String getOutput(String method, String arg1, String arg2) {
		String userId, songId;
		StringBuilder sb = new StringBuilder();
		int timesPlayed = 0;
		long before = System.nanoTime();
		long after;

		switch (method) {
		case "getTimesPlayed":
			songId = arg1;

			timesPlayed = profiler.getTimesPlayed(songId);
			after = System.nanoTime();

			sb.append("Song ");
			sb.append(songId);
			sb.append(" played ");
			sb.append(timesPlayed);
			sb.append(" times. (");
			sb.append((after - before) / 1000);
			sb.append(" ms)");
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
			sb.append(". (");
			sb.append((after - before) / 1000);
			sb.append(" ms)");
			break;
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		ORB orb = ORB.init(args, null);

		try {
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			String name = "Profiler";
			Profiler profiler = ProfilerHelper.narrow(ncRef.resolve_str(name));

			boolean cacheUsers = false;
			ProfilerClient client = new ProfilerClient(profiler, cacheUsers);

			// Read input.txt
			String method = "getTimesPlayed";
			String arg1 = "SOFRDND12A58A7D6C5";
			String arg2 = null;

			String output = client.getOutput(method, arg1, arg2);
			System.out.println(output);
			// Write to file
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
