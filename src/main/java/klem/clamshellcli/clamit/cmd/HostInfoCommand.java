package klem.clamshellcli.clamit.cmd;


import klem.clamshellcli.clamit.ClamITController;
import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class HostInfoCommand implements Command {
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	private static final String ACTION_NAME = "hostinfo";
	private IOConsole console;
	@Override
	public void plug(Context ctx) {
		 console = ctx.getIoConsole();
	}

	@Override
	public Object execute(Context arg0) {
		
		console.writeOutput(String.format("%n==========SYSTEM INFOS==========%n"));
		
		Map<String, String> getenv = System.getenv();
		
		Iterator<String> iterator = getenv.keySet().iterator();
		
		while(iterator.hasNext()) {
			String key = iterator.next();
			console.writeOutput(String.format("%-30s :: %-30s\n", key, getenv.get(key)));

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
				return "Display system information";

			}

			@Override
			public String getUsage() {
				return "Type sysinfo";

			}

			@Override
            public Map<String, String> getArguments() {
                return Collections.EMPTY_MAP;
            }

		};
	}

}
