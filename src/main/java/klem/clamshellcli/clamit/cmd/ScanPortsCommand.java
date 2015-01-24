package klem.clamshellcli.clamit.cmd;

import klem.clamshellcli.clamit.ClamITController;
import klem.clamshellcli.clamit.impl.PortScanner;
import klem.clamshellcli.clamit.utils.Constants;
import klem.clamshellcli.clamit.utils.Utils;
import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;

import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
// TODO MANAGE STEPS
public class ScanPortsCommand implements Command {
	private static final int RANGE_SIZE_TRESHOLD = 1000;
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	private static final String ACTION_NAME = "scanports";
	public static int OPEN = 0;
	public static int MAX_THREADS = 64;
	private static int STEP = 1;
	private IOConsole console;

	@Override
	public void plug(Context ctx) {
		console = ctx.getIoConsole();
	}

	@Override
	public Object execute(Context ctx) {

		String[] values = (String[]) ctx.getValue(Context.KEY_COMMAND_LINE_ARGS);

		if (values == null || values.length != 3) {
			console.writeOutput(String.format("%s%n%n", "This command has arguments : " + getDescriptor().getUsage()));
			return null;
		}

		try {
			String ip = values[0];
			String sPort = values[1];
			String ePort = values[2];

			// try {
			// STEP = Integer.valueOf(values[3]);
			// } catch (ArrayIndexOutOfBoundsException aioobe) {
			// aioobe.getStackTrace();
			// }

			int startPort = 0;
			int endPort = 0;

			try {
				startPort = Integer.valueOf(sPort);
			} catch (NumberFormatException nfe) {
				console.writeOutput(String.format("%s is not a number", endPort));
				return null;
			}

			try {
				endPort = Integer.valueOf(ePort);
			} catch (NumberFormatException nfe) {
				console.writeOutput(String.format("%s is not a number", endPort));
				return null;
			}

			if (!Utils.validateIpFormat(ip)) {
				System.err.println(String.format("%n%s is not a valid IP adress", ip));
				return null;
			}

			if (startPort < Constants.Default.MIN_PORT || startPort > Constants.Default.MAX_PORT) {
				console.writeOutput(String.format("%n%s is not a valid port number. (%s - %s)", startPort, Constants.Default.MIN_PORT,
						Constants.Default.MAX_PORT));
				return null;
			}

			if (endPort < Constants.Default.MIN_PORT || endPort > Constants.Default.MAX_PORT) {
				console.writeOutput(String.format("%n%s is not a valid port number. (%s - %s)", endPort, Constants.Default.MIN_PORT,
						Constants.Default.MAX_PORT));
				return null;
			}

			if (endPort < startPort) {
				console.writeOutput(String
						.format("%nInvalid port range specified! [startPort] must be less than [endPort]"));
				return null;
			}
			int totalPorts = endPort - startPort;

			if (totalPorts >= RANGE_SIZE_TRESHOLD) {
				console.writeOutput(String.format("%nScanning " + totalPorts + " ports can take very long. Are you sure?"));
				
				String getStatus = console.readInput("Y/N");

				Scanner input = new Scanner(console.getInputStream());
				String response = input.nextLine();
				console.writeOutput(response);
				return null;

			}

			if (endPort == startPort) {
				System.out.println(String.format("%nInvalid port range specified! If you want to scan a single port, use the [ping] command"));
				return null;
			}


			System.out.println(String.format("%nScanning %s port(s) from %s to %s every %s port(s) using %s thread(s) ", totalPorts, startPort, endPort, STEP, MAX_THREADS));
			OPEN = 0;
			try {
				InetAddress address = InetAddress.getByName(ip);
				long currentTime = System.currentTimeMillis();
				long ping;
				if (address.isReachable(5000)) {
					ping = System.currentTimeMillis() - currentTime;
					System.out.println(String.format("%nSuccess:: %s found in %s ms", ip, ping));
					System.out.println(String.format("%nScanning port from %s to %s", startPort, endPort));
					int currentPort;

					ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
					for ( currentPort = startPort; currentPort < endPort; currentPort++) {
						PortScanner scanner = new PortScanner(address, currentPort);
						executor.execute(scanner);
					}

					executor.shutdown();

					while (!executor.isTerminated()) {
					}
						System.out.println(String.format("%s port(0) open", OPEN));
						System.out.println(String.format("%s port(0) closed ", totalPorts - OPEN));
				} else {
					System.out.println(String.format("Port scan failed, %s is unreachable", ip));
					return null;
				}
			} catch (IOException ioe) {
				console.writeOutput(ioe.getMessage() + "\n");
				console.writeOutput(ioe.getCause() + "\n");
				return null;
			}

		} catch (Exception e) {
			console.writeOutput(e.getMessage() + "\n");
			console.writeOutput(e.getCause() + "\n");
			return null;
		}

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
				return "Scan a range of port on the specified IP adress, need ICMP ECHO REQUEST privileges";

			}

			@Override
			public String getUsage() {
				return "Type: scanport ip startport endport";

			}

			Map<String, String> args;

			@Override
			public Map<String, String> getArguments() {
				if (args != null)
					return args;
				args = new LinkedHashMap<String, String>();
				args.put(ClamITController.KEY_ARGS_URL + ":<ip>", "The IP address to be scanned");
				args.put(ClamITController.KEY_ARGS_URL + ":<startport>", "The lower port number");
				args.put(ClamITController.KEY_ARGS_URL + ":<endport>", "The upper port number");
				args.put(ClamITController.KEY_ARGS_URL + ":<step>", "optional, scan only every [step] port");

				return args;
			}

		};
	}

}
