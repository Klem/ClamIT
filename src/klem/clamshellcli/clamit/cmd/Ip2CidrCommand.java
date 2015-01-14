package klem.clamshellcli.clamit.cmd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import klem.clamshellcli.clamit.ClamITController;
import klem.clamshellcli.clamit.utils.IPUtils;
import klem.clamshellcli.clamit.utils.Utils;

import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;
import org.clamshellcli.core.ShellException;

public class Ip2CidrCommand implements Command {
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	private static final String ACTION_NAME = "ip2cidr";
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

		try {
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

			int ipStart = IPUtils.ipToInt(ipFrom);
			int ipEnd = IPUtils.ipToInt(ipTo);

			if (ipStart > ipEnd) {
				console.writeOutput(String.format("%n invalid ip range!"));
				return null;
			}
			console.writeOutput(String.format("Computing Cidr adress range from %s to %s....%n", ipFrom, ipTo));
			List<String> cidrList = getCidrList(ipFrom, ipTo);

			if (cidrList == null || cidrList.isEmpty()) {
				console.writeOutput(String.format("No range could be valculated. Check your IP addresses' validity"));
				return null;
			}

			Iterator<String> it = cidrList.iterator();

			while (it.hasNext()) {
				String cidr = it.next();
				console.writeOutput(String.format("%n%s", cidr));
			}

		} catch (Exception e) {
			throw new ShellException(e);
		}

		console.writeOutput(String.format("%n%n"));
		return null;
	}

	private static List<String> getCidrList(String startIp, String endIp) {
		long start = Utils.ipToLong(startIp);
		long end = Utils.ipToLong(endIp);

		ArrayList<String> pairs = new ArrayList<String>();
		while (end >= start) {
			byte maxsize = 32;
			while (maxsize > 0) {
				long mask = CIDR2MASK[maxsize - 1];
				long maskedBase = start & mask;

				if (maskedBase != start) {
					break;
				}

				maxsize--;
			}
			double x = Math.log(end - start + 1) / Math.log(2);
			byte maxdiff = (byte) (32 - Math.floor(x));
			if (maxsize < maxdiff) {
				maxsize = maxdiff;
			}
			String ip = Utils.longToIP(start);
			pairs.add(ip + "/" + maxsize);
			start += Math.pow(2, (32 - maxsize));
		}
		return pairs;
	}

	private static final int[] CIDR2MASK = new int[] { 0x00000000, 0x80000000, 0xC0000000, 0xE0000000, 0xF0000000,
			0xF8000000, 0xFC000000, 0xFE000000, 0xFF000000, 0xFF800000, 0xFFC00000, 0xFFE00000, 0xFFF00000, 0xFFF80000,
			0xFFFC0000, 0xFFFE0000, 0xFFFF0000, 0xFFFF8000, 0xFFFFC000, 0xFFFFE000, 0xFFFFF000, 0xFFFFF800, 0xFFFFFC00,
			0xFFFFFE00, 0xFFFFFF00, 0xFFFFFF80, 0xFFFFFFC0, 0xFFFFFFE0, 0xFFFFFFF0, 0xFFFFFFF8, 0xFFFFFFFC, 0xFFFFFFFE,
			0xFFFFFFFF };

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
				return "Computes the CIDR addresses from an IP range";

			}

			@Override
			public String getUsage() {
				return "Type: ip2cidr startIP endIp";

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
