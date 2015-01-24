package klem.clamshellcli.clamit.cmd;


// TODO IGNORE OUTPUT

import klem.clamshellcli.clamit.ClamITController;
import klem.clamshellcli.clamit.impl.proxy.ProxyConfig;
import klem.clamshellcli.clamit.impl.proxy.ProxyHandler;
import klem.clamshellcli.clamit.utils.Constants;
import klem.clamshellcli.clamit.utils.Utils;
import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class ProxyConfigCommand implements Command {
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	public static final String ACTION_NAME = "proxyconfig";
	private static final String CREATE = "create";
	private static final String LOAD = "load";
	private static final String UNLOAD = "unload";
	private static final String DELETE = "delete";
	private static final String LIST = "list";
	private static final String HELP = "help";
	private final ProxyHandler handler = new ProxyHandler();
	private IOConsole console;
	private Context context;

	@Override
	public void plug(Context ctx) {
		console = ctx.getIoConsole();
		context = ctx;
	}

	@Override
	public Object execute(Context ctx) {
		String[] values = (String[]) ctx.getValue(Context.KEY_COMMAND_LINE_ARGS);

		// INPUT VERIFICATION
		if (values == null || values.length < 1) {
			console.writeOutput(String.format("%n%s","This command has arguments : "+ getDescriptor().getUsage()));
			return null;
		}

		String action = values[0];
		
		

		// IMPLEMENTATION
		
		// SERVER START
		if(action.equals(CREATE)) {
			String name = console.readInput("Name: ");
			String host = console.readInput("Host: ");
			String port = console.readInput("Port: ");
			String user = console.readInput("User: ");
			String pass = console.readInput("Password: ");
			
			ProxyConfig proxy = new ProxyConfig(name, host, port, user, pass);
			
			try {
				handler.create(proxy);
			} catch (IOException e) {
				console.writeOutput(String.format("%n%s", e.getMessage()));
				console.writeOutput(String.format("%n%s", e.getCause()));
				return null;
			}
			
			console.writeOutput(String.format("%n%s", "Proxy created : "+proxy.toString()));
			
			askApplyNow(proxy);
			
			
			return null;
			
		}
		
		//SERVER STOP
		else if(action.equals(LOAD)) {
			String name;
			ProxyConfig proxy = null;
			try {
				name = values[1];
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				console.writeOutput(String.format("%n%s","ProxyConfig name is missing : "+ getDescriptor().getUsage()));
				return null;
			}
			
			try {
				proxy = handler.load(name);
			} catch (IOException e) {
				console.writeOutput(String.format("%n%s", e.getMessage()));
				console.writeOutput(String.format("%n%s", e.getCause()));
				return null;
			}
			console.writeOutput(String.format("%n%s", "Proxy loaded : "+proxy.toString()));
			context.putValue(Constants.Context.PROXY_CONFIG, proxy);
			return null;
			
		}
		
		
		else if(action.equals(UNLOAD)) {
			String name;
			try {
				name = values[1];
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				console.writeOutput(String.format("%n%s","ProxyConfig name is missing : "+ getDescriptor().getUsage()));
				return null;
			}
			handler.unload(null, context);
			return null;
		}
		
		else if(action.equals(DELETE)) {
			String name;
			try {
				name = values[1];
			} catch (ArrayIndexOutOfBoundsException aioobe) {
				console.writeOutput(String.format("%n%s","ProxyConfig name is missing : "+ getDescriptor().getUsage()));
				return null;
			}
			
			try {
				handler.delete(name);
				console.writeOutput(String.format("%n%s", "Proxy deleted : "+name));
			} catch (IOException e) {
				console.writeOutput(String.format("%n%s", e.getMessage()));
				console.writeOutput(String.format("%n%s", e.getCause()));
				return null;
			}
			return null;
		}
		
		else if(action.equals(LIST)) {
			 String filePath = new File(Constants.Dir.PROXIES).getAbsolutePath();
			 File f = new File(filePath);
			 
			 File[] files = f.listFiles();
			 int i = 0;
			 for (File file : files) {
				 String name = file.getName();
				try {
					ProxyConfig proxyConfig = handler.load(name);
					console.writeOutput(String.format("%n|%-10s|%-40s|", name, proxyConfig.toString()));
					i++;
				} catch (IOException e) {
					console.writeOutput(String.format("%n%s", e.getMessage()));
					console.writeOutput(String.format("%n%s", e.getCause()));
				}
				
			}
			 console.writeOutput(String.format("%n%s", i +" proxy configuration(s) found"));

			 return null;
		}
		
		else if(action.equals(HELP)) {
			 console.writeOutput(Utils.getFileContentAsString(Constants.Dir.HELP, Constants.Files.PROXYCONFIG));
			 return null;
		}
		
		else {
			console.writeOutput(String.format("%n%s", "Unknown parameter : "+ action));
			console.writeOutput(String.format("%n%s", getDescriptor().getUsage()));

			return null;
		}

	}
		
	

	private void askApplyNow(ProxyConfig proxy) {
		String apply = console.readInput("Apply now ? y/n: ");

		if (Constants.Input.YES.equals(apply)) {
			handler.apply(proxy, context);
			return;
		} else if (Constants.Input.NO.equals(apply)) {
			return;
		} else {
			console.writeOutput(String.format("%n%s", "Invalid input : " + apply));
			askApplyNow(proxy);
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
				return "Manage proxies configurations";

			}

			@Override
			public String getUsage() {
				return "Type "+ACTION_NAME+" ["+CREATE+"]  ["+LOAD+" [name] || "+UNLOAD+" [name] || " +DELETE+" [name]] [list] [help]";

			}

			@Override
			public Map<String, String> getArguments() {
				return Collections.emptyMap();

			}

		};

	}

}
