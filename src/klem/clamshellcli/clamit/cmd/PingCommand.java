package klem.clamshellcli.clamit.cmd;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

import klem.clamshellcli.clamit.ClamITController;
import klem.clamshellcli.clamit.utils.IPUtils;

import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;
import org.clamshellcli.core.ShellException;

public class PingCommand implements Command {
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	private static final String ACTION_NAME = "ping";
	private static final int TRIES = 4;
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

		if (values == null || values.length != 1) {
			console.writeOutput(String.format("%s%n%n", "This command has arguments : " + getDescriptor().getUsage()));
			return null;
		}

		String arg = values[0];

		// if (!Utils.validateIpFormat(arg)) {
		// console.writeOutput(String.format("%n%s is not a valid IP adress",
		// arg));
		// return null;
		// }
		InetAddress ip;
		try {
			ip = InetAddress.getByName(arg);
			int i = 0;
			
			while (i < TRIES) {
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
				i++;
			}
		} catch (UnknownHostException uhe) {
			throw new ShellException(uhe.getMessage(), uhe.getCause());
		} catch (IOException ioe) {
			throw new ShellException(ioe.getMessage(), ioe.getCause());
		}
		console.writeOutput("Done");
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
				return "Ping ip or hostname";

			}

			@Override
			public String getUsage() {
				return "Type: ping [ip || hostname]";

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
