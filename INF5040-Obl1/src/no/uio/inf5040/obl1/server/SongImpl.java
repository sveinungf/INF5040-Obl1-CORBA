package no.uio.inf5040.obl1.server;

import no.uio.inf5040.obl1.tasteprofile.Song;

public class SongImpl extends Song {

	private static final long serialVersionUID = 1L;

	public SongImpl() {
	}

	public SongImpl(String id, int playCount) {
		this.id = id;
		play_count = playCount;
	}
}
