package no.uio.inf5040.obl1.tasteprofiler;

/**
 * TasteProfile/ProfilerPOA.java . Generated by the IDL-to-Java compiler
 * (portable), version "3.2" from tasteprofile.idl Thursday, September 18, 2014
 * 1:26:03 PM CEST
 */

/*
 * The service interface with the methods that can be invoked remotely by
 * clients
 */
public abstract class ProfilerPOA extends org.omg.PortableServer.Servant
		implements no.uio.inf5040.obl1.tasteprofiler.ProfilerOperations,
		org.omg.CORBA.portable.InvokeHandler {

	// Constructors

	private static java.util.Hashtable _methods = new java.util.Hashtable();
	static {
		_methods.put("getTimesPlayed", new java.lang.Integer(0));
		_methods.put("getTimesPlayedByUser", new java.lang.Integer(1));
	}

	public org.omg.CORBA.portable.OutputStream _invoke(String $method,
			org.omg.CORBA.portable.InputStream in,
			org.omg.CORBA.portable.ResponseHandler $rh) {
		org.omg.CORBA.portable.OutputStream out = null;
		java.lang.Integer __method = (java.lang.Integer) _methods.get($method);
		if (__method == null)
			throw new org.omg.CORBA.BAD_OPERATION(0,
					org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

		switch (__method.intValue()) {

		/* Returns how many times a given song was played by all the users */
		case 0: // TasteProfile/Profiler/getTimesPlayed
		{
			String song_id = in.read_string();
			int $result = (int) 0;
			$result = this.getTimesPlayed(song_id);
			out = $rh.createReply();
			out.write_long($result);
			break;
		}

		/* Returns how many times a given song was played by a given user */
		case 1: // TasteProfile/Profiler/getTimesPlayedByUser
		{
			String user_id = in.read_string();
			String song_id = in.read_string();
			int $result = (int) 0;
			$result = this.getTimesPlayedByUser(user_id, song_id);
			out = $rh.createReply();
			out.write_long($result);
			break;
		}

		default:
			throw new org.omg.CORBA.BAD_OPERATION(0,
					org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
		}

		return out;
	} // _invoke

	// Type-specific CORBA::Object operations
	private static String[] __ids = { "IDL:TasteProfile/Profiler:1.0" };

	public String[] _all_interfaces(org.omg.PortableServer.POA poa,
			byte[] objectId) {
		return (String[]) __ids.clone();
	}

	public Profiler _this() {
		return ProfilerHelper.narrow(super._this_object());
	}

	public Profiler _this(org.omg.CORBA.ORB orb) {
		return ProfilerHelper.narrow(super._this_object(orb));
	}

} // class ProfilerPOA
