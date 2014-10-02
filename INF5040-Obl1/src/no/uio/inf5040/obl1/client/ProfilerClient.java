package no.uio.inf5040.obl1.client;

import no.uio.inf5040.obl1.tasteprofile.Profiler;
import no.uio.inf5040.obl1.tasteprofile.ProfilerHelper;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class ProfilerClient {

	private static final String ARG_CACHE = "-cache";
	private static final String ARG_INPUT = "-in";
	private static final String ARG_OUTPUT = "-out";

	public static void main(String[] args) {
		boolean cacheUsers = false;
		String inputfile = null;
		String outputfile = "output.txt";

		for (int i = 0; i < args.length; ++i) {
			switch (args[i]) {
			case ARG_CACHE:
				cacheUsers = true;
				break;
			case ARG_INPUT:
				++i;
				inputfile = args[i];
				break;
			case ARG_OUTPUT:
				++i;
				outputfile = args[i];
				break;
			}
		}

		if (inputfile == null) {
			System.out.println("Error: Must supply input file.");
			System.out.println("Usage: this -ORBInitialPort <port>"
					+ " -ORBInitialHost <host> " + ARG_INPUT
					+ " <path to input file> [" + ARG_CACHE + " " + ARG_OUTPUT
					+ " <path to output file>]");
			return;
		}

		ORB orb = ORB.init(args, null);

		try {
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			String name = "Profiler";
			Profiler profiler = ProfilerHelper.narrow(ncRef.resolve_str(name));

			RemoteInvocator invocator = new RemoteInvocator(profiler,
					cacheUsers);

			FileHandler infile = new FileHandler(inputfile);
			FileHandler outfile = new FileHandler(outputfile);

			infile.readTo(invocator);

			outfile.write(invocator.getResults());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
