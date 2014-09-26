package no.uio.inf5040.obl1.server;

import no.uio.inf5040.obl1.tasteprofile.Song;
import no.uio.inf5040.obl1.tasteprofile.User;

public class UserImpl extends User {

	private static final long serialVersionUID = 1L;

	public UserImpl() {
	}

	public UserImpl(String id, Song[] songs) {
		this.id = id;
		this.songs = songs;
	}
}
