package no.uio.inf5040.obl1.client;

import no.uio.inf5040.obl1.tasteprofile.Profiler;
import no.uio.inf5040.obl1.tasteprofile.ProfilerHelper;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class ProfilerClient {

	private static final String INPUT_ARG = "-in";

	public static void main(String[] args) {
		String inputfile = null;

		for (int i = 0; i < args.length - 1 && inputfile == null; ++i) {
			if (INPUT_ARG.equals(args[i])) {
				inputfile = args[i + 1];
			}
		}

		if (inputfile == null) {
			System.out.println("Error: Must supply input file.");
			System.out.println("Usage: this -ORBInitialPort <port>"
					+ " -ORBInitialHost <host> " + INPUT_ARG
					+ " <path to input file>");
			return;
		}

		System.out.println("Starting client...");

		ORB orb = ORB.init(args, null);

		try {
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			String name = "Profiler";
			Profiler profiler = ProfilerHelper.narrow(ncRef.resolve_str(name));

			boolean cacheUsers = false;
			RemoteInvocator invocator = new RemoteInvocator(profiler,
					cacheUsers);

			FileHandler infile = new FileHandler(inputfile);
			FileHandler outfile = new FileHandler("output.txt");

			infile.readTo(invocator);

			outfile.write(invocator.getResults());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
