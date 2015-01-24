package klem.clamshellcli.clamit.tests;

import klem.clamshellcli.clamit.impl.PortScanner;
import klem.clamshellcli.clamit.utils.Utils;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanPorts {
	
	private static int MIN_PORT = 0;
	private static int MAX_PORT = 65536;
	
	public static void main(String[] args) {
		int startPort =1;
		int endPort = 8080;
		
		String ip = "192.168.0.21";
		
		if(!Utils.validateIpFormat(ip)) {
			System.err.println(String.format("%n%s is not a valid IP adress", ip));
			return;
		}
		
		
		if(startPort == MIN_PORT && (endPort == MIN_PORT || endPort == MAX_PORT)) {
			endPort = MAX_PORT;
			System.out.print(String.format("%nNo port range was specified! Scanning all ports can take very long. Are you sure? Y/N"));
		}
		
		if(startPort < MIN_PORT || startPort > MAX_PORT) {
			System.err.print(String.format("%n%s is not a valid port number. (%s - %s)", startPort, MIN_PORT, MAX_PORT));
			return;
		}
		
		if(endPort < MIN_PORT || endPort > MAX_PORT) {
			System.err.print(String.format("%n%s is not a valid port number. (%s - %s)", endPort, MIN_PORT, MAX_PORT));
			return;
		}
		
		if(endPort < startPort) {
			System.err.print(String.format("%nInvalid port range specified! [startPort] must be less than [endPort]"));
			return;
		}
		
		if(endPort == startPort) {
			System.err.print(String.format("%nInvalid port range specified! If you want to scan a single port, use the [checkPort] command"));
			return;
		}



		
		try {
		InetAddress address = InetAddress.getByName(ip);
		long currentTime = System.currentTimeMillis();
		long ping = 0L;
		if (address.isReachable(5000)) {
			ping = System.currentTimeMillis() - currentTime;
			System.out.print(String.format("%nSuccess:: %s found in %s ms", ip, ping));
			System.out.println(String.format("%nScanning port from %s to %s", startPort, endPort));
			int currentPort;

			ExecutorService executor = Executors.newFixedThreadPool(64);
			for ( currentPort = startPort; currentPort < endPort; currentPort++) {
				PortScanner scanner = new PortScanner(address, currentPort);
				executor.execute(scanner);
			}

			executor.shutdown();

			while (!executor.isTerminated()) {
			}
		//	System.out.println(String.format("%n%s port open", OPEN));
		//	System.out.println(String.format("%n%s port closed ", totalPorts - OPEN));
		} else {
			System.out.print(String.format("%nPort scan failed, %s is unreachable", ip));
			return;
		}
		} catch(IOException ioe) {
			ioe.printStackTrace();
			
			return;
		}
	}

}
