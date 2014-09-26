package no.uio.inf5040.obl1.tasteprofile;

/**
 * no/uio/inf5040/obl1/tasteprofile/Song.java . Generated by the IDL-to-Java
 * compiler (portable), version "3.2" from tasteprofile.idl Thursday, September
 * 25, 2014 5:32:42 PM CEST
 */

/* valuetype containing info about a song */
public abstract class Song implements org.omg.CORBA.portable.StreamableValue {
	public String id = null;
	public int play_count = (int) 0;

	private static String[] _truncatable_ids = { no.uio.inf5040.obl1.tasteprofile.SongHelper
			.id() };

	public String[] _truncatable_ids() {
		return _truncatable_ids;
	}

	public void _read(org.omg.CORBA.portable.InputStream istream) {
		this.id = istream.read_string();
		this.play_count = istream.read_long();
	}

	public void _write(org.omg.CORBA.portable.OutputStream ostream) {
		ostream.write_string(this.id);
		ostream.write_long(this.play_count);
	}

	public org.omg.CORBA.TypeCode _type() {
		return no.uio.inf5040.obl1.tasteprofile.SongHelper.type();
	}
} // class Song