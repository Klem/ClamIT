package klem.clamshellcli.clamit.cmd;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import klem.clamshellcli.clamit.ClamITController;
import klem.clamshellcli.clamit.utils.Utils;

import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;
import org.clamshellcli.core.ShellException;

public class NetworkInfoCommand implements Command {
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	private static final String ACTION_NAME = "netinfo";
	private static final String sACTIVE = "active";
	private static final String sINACTIVE = "inactive";
	private static final String sALL = "all";
	private boolean bACTIVE = false;
	private boolean bINACTIVE = false;
	private boolean bALL = false;
	private IOConsole console;

	@Override
	public void plug(Context ctx) {
		console = ctx.getIoConsole();
	}

	@Override
	public Object execute(Context ctx) {

		String[] values = (String[]) ctx.getValue(Context.KEY_COMMAND_LINE_ARGS);
		// CHECK INPUT
		if (values == null || values.length != 1) {
			console.writeOutput(String.format("%s%n%n", "This command has argument : " + getDescriptor().getUsage()));
			return null;
		}
		String arg = values[0].toLowerCase();

		// ASSIGN VARS ACCORDINGLY
		String lbl = "";
		if (arg.equals(sACTIVE)) {
			bACTIVE = true;
			bINACTIVE = false;
			bALL = false;
			lbl = sACTIVE;
		} else if (arg.equals(sINACTIVE)) {
			bINACTIVE = true;
			bACTIVE = false;
			bALL = false;
			lbl = sINACTIVE;
		} else if (arg.equals(sALL)) {
			bALL = true;
			bINACTIVE = false;
			bACTIVE = false;
			lbl = sALL;
		} else {
			console.writeOutput(String.format("%s%n%n", "Invalid argument : " + getDescriptor().getUsage()));
			return null;
		}

		console.writeOutput(String.format("%s%n%n", "Displaying " + lbl + " network interfaces..."));

		Enumeration<NetworkInterface> nets;
		int i = 0;
		try {
			nets = NetworkInterface.getNetworkInterfaces();

			for (NetworkInterface netint : Collections.list(nets)) {
				if (bALL) {
					displayInterfaceInformation(netint);
					i++;
				}

				if (bACTIVE) {
					if (netint.isUp()) {
						displayInterfaceInformation(netint);
						i++;
					}
				}

				if (bINACTIVE) {
					if (!netint.isUp()) {
						displayInterfaceInformation(netint);
						i++;
					}
				}
			}
			console.writeOutput(String.format("%n%s%n", i + " " + lbl + " interfaces found"));

		} catch (SocketException e) {
			throw new ShellException(e.getMessage(), e.getCause());
		}

		return null;
	}

	public void displayInterfaceInformation(NetworkInterface netint) throws SocketException {

		console.writeOutput(String.format("%n==========%s==========%n", netint.getDisplayName().toUpperCase()));
		writeLine("Name", netint.getName());
		writeLine("MAC address", Arrays.toString(netint.getHardwareAddress()));
		writeLine("IP adresses", Utils.dumpToString(netint.getInterfaceAddresses(), "  , "));
		writeLine("Loopback", netint.isLoopback());
		writeLine("Active", netint.isUp());
		writeLine("MTU", netint.getMTU());
		writeLine("Virtual", netint.isVirtual());
		writeLine("Point2Point", netint.isPointToPoint());
		writeLine("Multicast", netint.supportsMulticast());

		// Details if is virtual
		if (netint.isVirtual()) {
			Enumeration<NetworkInterface> subInterfaces = netint.getSubInterfaces();

			while (subInterfaces.hasMoreElements()) {
				NetworkInterface sub = subInterfaces.nextElement();
				console.writeOutput(String.format("\n	Know subinterfaces		: %s %n", netint.isVirtual()));
				displayInterfaceInformation(sub);
			}
		}

	}

	private void writeLine(String key, Object value) {
		console.writeOutput(String.format("%n|%-15s | %-15s %n", key, value));
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
				return "Display the network interfaces";

			}

			@Override
			public String getUsage() {
				return "Type netinfo ["+sACTIVE+" || "+sINACTIVE+" || "+sALL+" display according to status]";

			}

			Map<String, String> args;

			@Override
			public Map<String, String> getArguments() {
				if (args != null)
					return args;
				args = new LinkedHashMap<String, String>();
				args.put(ClamITController.KEY_ARGS_URL + ":-status", "The lower limit address");
				args.put(ClamITController.KEY_ARGS_URL + ":<endIP>", "The upper limit address");

				return args;
			}

		};
	}

}
