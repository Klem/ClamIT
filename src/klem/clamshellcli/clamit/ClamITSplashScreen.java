package klem.clamshellcli.clamit;

import java.io.OutputStream;
import java.io.PrintStream;

import org.clamshellcli.api.Context;
import org.clamshellcli.api.SplashScreen;



public class ClamITSplashScreen implements SplashScreen {

	
	

	 private static StringBuilder screen;
	    static{
	    	screen = new StringBuilder();
	    	screen
	    	.append(String.format("%n%n"))
	    	.append(String.format("%n%n"))
			.append(" _____    _            ___        ___  ___             _    _____ ").append(String.format("%n"))
			.append("/  ___|  | |          /   |      /   |/   |           | |  |_   _|").append(String.format("%n")) 
			.append("| |      | |         / /| |     / /|   /| |     __    | |    | |  ").append(String.format("%n")) 
			.append("| |      | |        / / | |    / / |__/ | |   /__/    | |    | |  ").append(String.format("%n"))
			.append("| |____  | |___    / /  | |   / /       | |           | |    | |  ").append(String.format("%n")) 
			.append(" \\_____| |_____|  /_/   |_|  /_/        |_|           |_|    |_|  ").append(String.format("%n"))
	    	.append("Simple Tools for smart developers").append(String.format("%n"))
	    	.append("By Klem klem86@gmail.com").append(String.format("%n%n"))
	    	.append("Powered by Clamshell-Cli framework ").append(String.format("%n"))
	    	.append("http://code.google.com/p/clamshell-cli/").append(String.format("%n%n"))
	    	.append("Java version: ").append(System.getProperty("java.version")).append(String.format("%n"))
	    	.append("OS: ").append(System.getProperty("os.name")).append(", Version: ").append(System.getProperty("os.version")).append(String.format("%n%n"))
	    	.append("Type \"help\" for a list of available commands: ").append(String.format("%n"))
	    	.append("=========================================================================================").append(String.format("%n"))

	            ;
	    }
	    
	    @Override
	    public void render(Context ctx) {
	        PrintStream out = new PrintStream ((OutputStream)ctx.getValue(Context.KEY_OUTPUT_STREAM));
	        out.println(screen);
	    }

	    public void plug(Context plug) {
	    }
	    
}
