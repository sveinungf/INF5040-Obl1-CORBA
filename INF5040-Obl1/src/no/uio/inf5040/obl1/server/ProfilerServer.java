package no.uio.inf5040.obl1.server;

import no.uio.inf5040.obl1.tasteprofile.Profiler;
import no.uio.inf5040.obl1.tasteprofile.ProfilerHelper;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class ProfilerServer {

	public static void main(String[] args) {
		if (args.length < 6) {
			System.out
					.println("Usage: java –jar .jar -ORBInitialPort <port> -ORBInitialHost <host>");
			System.exit(1);
		}

		try {
			ORB orb = ORB.init(args, null);
			POA rootpoa = POAHelper.narrow(orb
					.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			ProfilerServant profilerServant = new ProfilerServant(args[4],
					Boolean.getBoolean(args[3]));
			org.omg.CORBA.Object ref = rootpoa
					.servant_to_reference(profilerServant);
			Profiler pref = ProfilerHelper.narrow(ref);

			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			String name = "Profiler";
			NameComponent path[] = ncRef.to_name(name);
			ncRef.rebind(path, pref);

			orb.run();
		}

		catch (Exception e) {
			System.err.println("ERROR: " + e.getMessage());
			e.printStackTrace(System.out);
		}
	}
}
