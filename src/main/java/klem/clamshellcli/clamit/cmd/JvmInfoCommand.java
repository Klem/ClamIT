package klem.clamshellcli.clamit.cmd;

import klem.clamshellcli.clamit.ClamITController;
import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class JvmInfoCommand implements Command {

	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	private static final String ACTION_NAME = "jvminfo";
	private IOConsole console;

	@Override
	public void plug(Context ctx) {
		console = ctx.getIoConsole();
	}

	@Override
	public Object execute(Context arg0) {
		int mb = 1024 * 1024;

		// Getting the runtime reference from system
		Runtime runtime = Runtime.getRuntime();

		console.writeOutput(String.format("%n==========JVM INFOS==========%n"));

		console.writeOutput(String.format("%nAvailable processors (cores): %s",
				Runtime.getRuntime().availableProcessors()));
		// Print used memory
		console.writeOutput(String.format(" %nUsed Memory	: %s Mb",
				(runtime.totalMemory() - runtime.freeMemory()) / mb));

		// Print free memory
		console.writeOutput(String.format(" %nFree Memory	: %s Mb",
				(runtime.freeMemory() / mb)));

		// Print total available memory
		console.writeOutput(String.format(" %nTotal Memory	: %s Mb",
				(runtime.totalMemory() / mb)));

		// Print Maximum available memory
		console.writeOutput(String.format(" %nMax Memory		: %s Mb",
				(runtime.maxMemory() / mb)));

		Properties properties = System.getProperties();

		Iterator<Object> iterator = properties.keySet().iterator();

		console.writeOutput(String.format("%n==========VARIABLES==========%n"));
		
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			
			console.writeOutput(String.format("%-30s :: %-30s\n", key, properties.get(key)));
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
				return "Display the JVM informations";

			}

			@Override
			public String getUsage() {
				return "Type jvminfo";

			}

			@Override
			public Map<String, String> getArguments() {
				return Collections.EMPTY_MAP;
			}

		};
	}

}
