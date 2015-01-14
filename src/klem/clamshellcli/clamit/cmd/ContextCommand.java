package klem.clamshellcli.clamit.cmd;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import klem.clamshellcli.clamit.ClamITController;

import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;

public class ContextCommand implements Command {
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	private static final String ACTION_NAME = "context";

	@Override
	public void plug(Context arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object execute(Context ctx) {
		IOConsole console = ctx.getIoConsole();

		Map<String, ? extends Object> values = ctx.getValues();

		Iterator<String> it = values.keySet().iterator();
		console.writeOutput(String.format("%n%-30s | %-40s |%n", "KEY", "VALUE"));
		while (it.hasNext()) {
			String key = it.next();
			Object value = values.get(key);
			console.writeOutput(String.format("%-30s | %-40s |%n", key, value));
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
				return "Show current Clam-IT instance context for debug";

			}

			@Override
			public String getUsage() {
				return "Type 'context'";

			}

			@Override
			public Map<String, String> getArguments() {
				return Collections.emptyMap();

			}

		};

	}

}
