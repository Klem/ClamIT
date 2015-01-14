package klem.clamshellcli.clamit.cmd;

import java.util.Collections;
import java.util.Map;

import klem.clamshellcli.clamit.ClamITController;
import klem.clamshellcli.clamit.utils.Constants;
import klem.clamshellcli.clamit.utils.Utils;

import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;

public class ReadmeCommand implements Command {
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	private static final String ACTION_NAME = "readme";

	@Override
	public void plug(Context arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object execute(Context ctx) {
		IOConsole console = ctx.getIoConsole();
		
		console.writeOutput(Utils.getFileContentAsString(Constants.Dir.HELP, Constants.Files.README));
		
		
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
				return "Duh!";

			}

			@Override
			public String getUsage() {
				return "Type 'readme'";

			}

			@Override
			public Map<String, String> getArguments() {
				return Collections.emptyMap();

			}

		};

	}

}
