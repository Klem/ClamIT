package klem.clamshellcli.clamit.cmd;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import klem.clamshellcli.clamit.ClamITController;
import klem.clamshellcli.clamit.utils.Constants;
import klem.clamshellcli.clamit.utils.Constants.Default;
import klem.clamshellcli.clamit.utils.Utils;

import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;
import org.clamshellcli.core.ShellException;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.mockftpserver.fake.filesystem.WindowsFakeFileSystem;

public class MockFtpCommand implements Command {
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	public static final String ACTION_NAME = "mockftp";
	private static final String START = "start";
	private static final String STOP = "stop";
	private static final String HELP = "help";
	private static final String LICENCE = "licence";
	private static final String WIN = "win";
	private static final String NIX = "nix";
	private FileSystem fileSystem;
	private UserAccount userAccount;
	private FakeFtpServer server = null;
	private int port;
	private String ftppath;

	private IOConsole console;

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
			String fs;
			
			
			// IMPLEMENTATION
		try {	
			// SERVER START
			if(action.equals(START)) {
				// CHECK INSTANCE RUNNING
				FakeFtpServer existingInstance = (FakeFtpServer) ctx.getValue(Constants.Context.FTP_INSTANCE);
				if(existingInstance != null) {
					console.writeOutput(String.format("%n%s", "Cannot start server : Server already started "));
					displayProperties();
					return null;
				} 
				//CHECK PORT PARAMETER
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
				// CHECK PORT VALUE
				if (port < Constants.Default.MIN_PORT || port > Constants.Default.MAX_PORT) {
					console.writeOutput(String.format("%n%s is not a valid port number. (%s - %s)",port, Constants.Default.MIN_PORT, Constants.Default.MAX_PORT));
					return null;
				}
	
				// CHECK FILESYSTEM PRESENCE
				try {
					fs = values[2];
					} catch (ArrayIndexOutOfBoundsException aioobe) {
						console.writeOutput(String.format("%n%s","Filesystem is missing : "+ getDescriptor().getUsage()));
						return null;
				}
				//CHECK FIESTSYEM VALIDITY
				if(WIN.equals(fs)) {
					fileSystem = new WindowsFakeFileSystem();
				}
				else if(NIX.equals(fs)) {
					fileSystem = new UnixFakeFileSystem();
				} else {
					console.writeOutput(String.format("%s%n","Invalid fileSystem "+fs+" : "+ getDescriptor().getUsage()));
					return null;
				}
				
				
				return start(ctx);
				
			}
			
			//SERVER STOP
			else if(action.equals(STOP)) {
				return stop(ctx);
			}
			
			
			else if(action.equals(HELP)) {
				 console.writeOutput(Utils.getFileContentAsString(Constants.Dir.HELP, Constants.Files.MOCKFTP));
				 return null;
			}
			
			else if(action.equals(LICENCE)) {
				 console.writeOutput(Utils.getFileContentAsString(Constants.Dir.LICENCE, Constants.Files.MOCKFTP));
				 return null;
			}
			
			
			// WRONG PARAMETERS
			else {
				console.writeOutput(String.format("%n%s", "Unknown parameter : "+ action));
				console.writeOutput(String.format("%n%s", getDescriptor().getUsage()));
	
				return null;
			}
			
		}catch(Throwable t) {
			throw new ShellException(t);
		}
			
	}
	

	

	private Object start(Context ctx) {
		server = new FakeFtpServer();
		ftppath = new File(Constants.Dir.FTP).getAbsolutePath();
		userAccount = new UserAccount(Constants.Default.USER, Constants.Default.PASSWORD, ftppath);
		
		// LIST FILES AND ADD THEM TO FILESYSTEM
		fileSystem.add(new DirectoryEntry(ftppath));
		File[] files = new File(ftppath).listFiles();
		
		console.writeOutput(String.format("%n%s", "Adding files...."));
		int i = 0;
		for (File file : files) {
			console.writeOutput(String.format("%s%n", file.getAbsolutePath()));
			fileSystem.add(new FileEntry(file.getAbsolutePath(), Utils.getFileContentAsString(file)));
		}
		console.writeOutput(String.format("%s%n file(s) added.", i));
		
		server.setFileSystem(fileSystem);
		
		server.addUserAccount(userAccount);
		server.setServerControlPort(port);
		server.start();
		
		ctx.putValue(Constants.Context.FTP_INSTANCE, server);
		
		console.writeOutput( server.toString()+" \n Server started....." );
		displayProperties();
		console.writeOutput( "\nUse "+MockFtpCommand.ACTION_NAME +" stop to end session" );
		
		return null;
		
	}

	private Object stop(Context ctx) {
		
		FakeFtpServer existingInstance = (FakeFtpServer) ctx.getValue(Constants.Context.FTP_INSTANCE);
		if(existingInstance == null) {
			console.writeOutput(String.format("%n%s", "Cannot stop server : Server not started"));
			return null;
		}
		
		existingInstance.stop();
		ctx.removeValue(Constants.Context.FTP_INSTANCE);
		displayProperties();
		console.writeOutput(" \n Server stopped....." );
		return null;
		
	}
	
	public void displayProperties() {
		console.writeOutput(String.format("%n%s", "Host : localhost"));
		console.writeOutput(String.format(" %s", "Port : "+port));
		console.writeOutput(String.format(" %s", "User : "+userAccount.getUsername()));
		console.writeOutput(String.format(" %s", "Pass : "+userAccount.getPassword()));
		console.writeOutput(String.format(" %s%n", "Base dir : "+ftppath));
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
				return "Launch an ftp server instance";

			}

			@Override
			public String getUsage() {
				return "Type "+ACTION_NAME+" [start port filesystem ["+WIN+" || "+NIX+"] || stop] help licence";

			}

			@Override
			public Map<String, String> getArguments() {
				return Collections.emptyMap();

			}

		};

	}

}
