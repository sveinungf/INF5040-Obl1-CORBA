package no.uio.inf5040.obl1.client;

import no.uio.inf5040.obl1.tasteprofile.Profiler;
import no.uio.inf5040.obl1.tasteprofile.ProfilerHelper;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

public class ProfilerClient {

	public static void main(String[] args) {
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

			FileHandler infile = new FileHandler("input.txt");
			FileHandler outfile = new FileHandler("output.txt");

			infile.readTo(invocator);

			outfile.write(invocator.getResults());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
