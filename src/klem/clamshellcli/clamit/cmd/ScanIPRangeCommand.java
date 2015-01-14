package klem.clamshellcli.clamit.cmd;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

import klem.clamshellcli.clamit.ClamITController;
import klem.clamshellcli.clamit.utils.IPUtils;
import klem.clamshellcli.clamit.utils.Utils;

import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;
import org.clamshellcli.core.ShellException;

public class ScanIPRangeCommand implements Command {
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	private static final String ACTION_NAME = "iprange";
	public int ipStart;
	public int ipEnd;
	private IOConsole console;

	@Override
	public void plug(Context ctx) {
		console = ctx.getIoConsole();
	}

	@Override
	public Object execute(Context ctx) {

		String[] values = (String[]) ctx.getValue(Context.KEY_COMMAND_LINE_ARGS);

		if (values == null || values.length != 2) {
			console.writeOutput(String.format("%s%n%n", "This command has arguments : " + getDescriptor().getUsage()));
			return null;
		}

		String ipFrom = values[0];
		String ipTo = values[1];

		if (!Utils.validateIpFormat(ipFrom)) {
			console.writeOutput(String.format("%n%s is not a valid IP adress", ipTo));
			return null;
		}
		if (!Utils.validateIpFormat(ipTo)) {
			console.writeOutput(String.format("%n invalid ip range!"));
			return null;
		}
		ipStart = IPUtils.ipToInt(ipFrom);
		ipEnd = IPUtils.ipToInt(ipTo);

		if (ipStart > ipEnd) {
			console.writeOutput(String.format("%n invalid ip range!"));
			return null;
		}

		console.writeOutput(String.format("About to scan IP within range : %s ==> %s....%n", ipFrom, ipTo));
		console.writeOutput(String.format("%n|%-15s | %-15s | %-15s | %-5s |", "       IP      ", "   HOSTNAME   ", "    STATUS    ", "PING"));
		for (int i = ipStart; i <= ipEnd; i++) {

			String address = IPUtils.intToIp(i);
			InetAddress ip;
			try {
				ip = InetAddress.getByName(address);
				
				String status;
				long currentTime = System.currentTimeMillis();
				long ping = 0L;
				if (ip.isReachable(2000)) {
					ping = System.currentTimeMillis() - currentTime;
					status = "reached";
					ip.getHostAddress();
					ip.getHostName();

				} else {
					status = "unreachable";
				}
				console.writeOutput(String.format(" %-15s | %-15s | %-15s | %-5s |%n", ip.getHostAddress(), ip.getHostName(), status, ping + " ms"));
			} catch (UnknownHostException uhe) {
				throw new ShellException(uhe.getMessage(), uhe.getCause());
			} catch (IOException ioe) {
				throw new ShellException(ioe.getMessage(), ioe.getCause());
			}
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
