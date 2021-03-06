package no.uio.inf5040.obl1.tasteprofile;

/**
 * no/uio/inf5040/obl1/tasteprofile/ProfilerOperations.java . Generated by the
 * IDL-to-Java compiler (portable), version "3.2" from tasteprofile.idl Friday,
 * October 3, 2014 12:28:57 PM CEST
 */

/*
 * The service interface with the methods that can be invoked remotely by
 * clients
 */
public interface ProfilerOperations {

	/* Returns how many times a given song was played by all the users */
	int getTimesPlayed(String song_id);

	/* Returns how many times a given song was played by a given user */
	int getTimesPlayedByUser(String user_id, String song_id);

	/* Returns a users complete profile */
	int getUserProfile(String user_id, String song_id,
			no.uio.inf5040.obl1.tasteprofile.UserHolder user);
} // interface ProfilerOperations
