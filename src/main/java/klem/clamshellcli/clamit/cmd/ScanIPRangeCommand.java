package klem.clamshellcli.clamit.cmd;

import klem.clamshellcli.clamit.ClamITController;
import klem.clamshellcli.clamit.impl.IpScanner;
import klem.clamshellcli.clamit.utils.IPUtils;
import klem.clamshellcli.clamit.utils.Utils;
import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO configure timeout via cli
// TODO CHOOSE SINGLE OR MULTI TREADED VIA CLI
// TODO CHOOSE TO DISPLAY UNREACABLE ADRESS VIA CLI
public class ScanIPRangeCommand implements Command {
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	private static final String ACTION_NAME = "iprange";
	private static final int MAX_THREADS = 64;
	public static int REACHED = 0;
	private IOConsole console;
	private int TIMEOUT = 5000;

	Collection<IpScanner> jobs = new ArrayList<IpScanner>();

	@Override
	public void plug(Context ctx) {
		console = ctx.getIoConsole();
	}

	@Override
	public Object execute(Context ctx) {
		int ipStart = 0;
		int ipEnd = 0;
		int totalIps = 0;


		// USER INPUT VALIDATION START

		String[] values = (String[]) ctx.getValue(Context.KEY_COMMAND_LINE_ARGS);

		if (values == null || values.length != 2) {
			console.writeOutput(String.format("%s%n%n", "This command has arguments : " + getDescriptor().getUsage()));
			return null;
		}

		String ipFrom = values[0];
		String ipTo = values[1];

		if (!Utils.validateIpFormat(ipFrom)) {
			console.writeOutput(String.format("%n%s is not a valid IP adress", ipFrom));
			return null;
		}
		if (!Utils.validateIpFormat(ipTo)) {
			console.writeOutput(String.format("%n%s is not a valid IP adress", ipTo));
			return null;
		}
		ipStart = IPUtils.ipToInt(ipFrom);
		ipEnd = IPUtils.ipToInt(ipTo);
		totalIps = ipEnd - ipStart;

		if (ipStart > ipEnd) {
			console.writeOutput(String.format("%n invalid ip range!"));
			return null;
		}

		// USER INPUT VALIDATION OK

		System.out.println(String.format("About to scan IP within range : %s ==> %s with a %s ms timeout using %s thread(s) %n", ipFrom, ipTo, TIMEOUT,MAX_THREADS));
		System.out.println(String.format("%s adresses to scan", totalIps));
		//console.writeOutput(String.format("%n|%-15s | %-15s | %-15s | %-5s |", "       IP      ", "   HOSTNAME   ", "    STATUS    ", "PING"));
		REACHED = 0;
		ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
		for (int i=ipStart; i<=ipEnd; i++) {
			IpScanner scanner = new IpScanner(i, TIMEOUT);
			executor.execute(scanner);

		}

		executor.shutdown();

		while (!executor.isTerminated()) {
		}
		System.out.println(String.format("%n%s adresses reached", REACHED));
		System.out.println(String.format("%n%s adresses unreached", totalIps - REACHED));

		return null;
	}



	@Override
	public Descriptor getDescriptor() {
		return new Command.Descriptor() {
			@Override
			public String getNamespace() {
				return NAMESPACE;
			}

			@Override
			public String getName() {
				return ACTION_NAME;

			}

			@Override
			public String getDescription() {
				return "Scan a range of IP addresses, need ICMP ECHO REQUEST privileges";

			}

			@Override
			public String getUsage() {
				return "Type: iprange startIP endIp";

			}

			Map<String, String> args;

			@Override
			public Map<String, String> getArguments() {
				if (args != null)
					return args;
				args = new LinkedHashMap<String, String>();
				args.put(ClamITController.KEY_ARGS_URL + ":<startIP>", "The lower limit address");
				args.put(ClamITController.KEY_ARGS_URL + ":<endIP>", "The upper limit address");

				return args;
			}
		};
	}
}
