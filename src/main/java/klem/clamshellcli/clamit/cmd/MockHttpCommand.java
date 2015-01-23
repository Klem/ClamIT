package klem.clamshellcli.clamit.cmd;


// TODO IGNORE OUTPUT

import klem.clamshellcli.clamit.ClamITController;
import klem.clamshellcli.clamit.impl.NanoHTTPD;
import klem.clamshellcli.clamit.utils.Constants;
import klem.clamshellcli.clamit.utils.Utils;
import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;
import org.clamshellcli.core.ShellException;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class MockHttpCommand implements Command {
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	public static final String ACTION_NAME = "mockhttp";
	private static final String START = "start";
	private static final String STOP = "stop";
	private static final String HELP = "help";
	private static final String LICENCE = "licence";
	private IOConsole console;
	private NanoHTTPD server = null;
	private int port;
	private File wwwroot;

	@Override
	public void plug(Context ctx) {
		console = ctx.getIoConsole();
	}

	@Override
	public Object execute(Context ctx) {
		String[] values = (String[]) ctx.getValue(Context.KEY_COMMAND_LINE_ARGS);

		// INPUT VERIFICATION
		if (values == null || values.length < 1) {
			console.writeOutput(String.format("%s%n","This command has arguments : "+ getDescriptor().getUsage()));
			return null;
		}

		String action = values[0];

		
		
		// IMPLEMENTATION
		
		// SERVER START
		if(action.equals(START)) {
			try {
				String sPort = values[1];
				port = Integer.valueOf(sPort);
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				console.writeOutput(String.format("%n%s","Port is missing : "+ getDescriptor().getUsage()));
				return null;
			} catch (NumberFormatException nfe) {
				console.writeOutput(String.format("%s%n","Port must be a number : "+ getDescriptor().getUsage()));
				return null;
			}
			
			if (port < Constants.Default.MIN_PORT || port > Constants.Default.MAX_PORT) {
				console.writeOutput(String.format("%n%s is not a valid port number. (%s - %s)",port, Constants.Default.MIN_PORT, Constants.Default.MAX_PORT));
				return null;
			}
			
			wwwroot = new File(Constants.Dir.HTTP).getAbsoluteFile();
			NanoHTTPD existingInstance = (NanoHTTPD) ctx.getValue(Constants.Context.HTTPD_INSTANCE);
			if(existingInstance != null) {
				console.writeOutput(String.format("%n%s", "Cannot start server : Server already started "+existingInstance.toString()));
				return null;
			}
	
			
			try {
				server = new NanoHTTPD(port, wwwroot, console);
				server.start();
				ctx.putValue(Constants.Context.HTTPD_INSTANCE, server);
				console.writeOutput( server.toString()+" \n Server started....." );
				console.writeOutput( "\nNow serving files in port " + server.getMyTcpPort() + " from \"" + server.getMyRootDir() + "\"" );
				console.writeOutput( "\nBrowse at http://localhost:" + server.getMyTcpPort() + "/" );
				console.writeOutput( "\nUse "+MockHttpCommand.ACTION_NAME +" stop to end session" );
	
			} catch (IOException e) {
				throw new ShellException(e);
			}
	
			return null;
		}
		
		//SERVER STOP
		else if(action.equals(STOP)) {
			NanoHTTPD existingInstance = (NanoHTTPD) ctx.getValue(Constants.Context.HTTPD_INSTANCE);
			if(existingInstance == null) {
				console.writeOutput(String.format("%n%s", "Cannot stop server : Server not started"));
				return null;
			}
			
			existingInstance.stop();
			ctx.removeValue(Constants.Context.HTTPD_INSTANCE);
			console.writeOutput( existingInstance.toString()+" \n Server stopped....." );
			return null;
		}
		
		else if(action.equals(HELP)) {
			 console.writeOutput(Utils.getFileContentAsString(Constants.Dir.HELP, Constants.Files.MOCKHTTP));
			 return null;
		}
		
		else if(action.equals(LICENCE)) {
			 console.writeOutput(Utils.getFileContentAsString(Constants.Dir.LICENCE, Constants.Files.MOCKHTTP));
			 return null;
		}
		
		// WRONG PARAMETERS
		else {
			console.writeOutput(String.format("%n%s", "Unknown parameter : "+ action));
			console.writeOutput(String.format("%n%s", getDescriptor().getUsage()));

			return null;
		}

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
				return "Launch an http server instance";

			}

			@Override
			public String getUsage() {
				return "Type "+ACTION_NAME+" [start port|| stop] help licence";

			}

			@Override
			public Map<String, String> getArguments() {
				return Collections.emptyMap();

			}

		};

	}

}
