package klem.clamshellcli.clamit.tests;

import klem.clamshellcli.clamit.impl.IpScanner;
import klem.clamshellcli.clamit.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanIPRange {
	 static int TIMEOUT = 5000;
	 public static int REACHED = 0;
	 public static void main(String[] args) {
		 
		 String ipFrom = "152.160.0.1";
		 String ipTo = "152.168.0.255";
		 Collection<IpScanner> jobs = new ArrayList<IpScanner>();

	        try {
	        	int ipStart = 0;
		        int ipEnd = 0;
				int totalIps = 0;
	            if(Utils.validateIpFormat(ipFrom)) {
	    			if(Utils.validateIpFormat(ipTo)) {

						ipStart = host2int(ipFrom);
						ipEnd = host2int(ipTo);
						totalIps = ipEnd - ipStart;

						System.out.print(String.format("Scanning started for IPs within range : %s ==> %s with a %s ms timeout....%n", ipFrom, ipTo, TIMEOUT));
						System.out.print(String.format("%s adresses to scan", totalIps));

						ExecutorService executor = Executors.newFixedThreadPool(64);
	    		        for (int i=ipStart; i<=ipEnd; i++) {
							IpScanner scanner = new IpScanner(i, TIMEOUT);
							executor.execute(scanner);

	    	            }

						executor.shutdown();

						while (!executor.isTerminated()) {
						}
						System.out.print(String.format("%n%s adresses reached", REACHED));
						System.out.print(String.format("%n%s adresses unreached",totalIps - REACHED));


					}else {
	    				System.err.println("Invalid IP adress "+ipTo);
	    				return;
	    			}
	    		} else {
	    			System.err.println("Invalid IP adress "+ipFrom);
	    			return;
	    		}

	           
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }





	    private static int host2int(String host) {
	        int ip=0;
	        if (!Character.isDigit(host.charAt(0))) return -1;
	        int[] addr = ip2intarray(host);
	        if (addr == null) return -1;
	        for (int i=0;i<addr.length;++i) {
	            ip += ((long)(addr[i]>=0 ? addr[i] : 0)) << 8*(3-i);
	        }
	        return ip;
	    }

	    private static int[] ip2intarray(String host) {
	        int[] address = {-1,-1,-1,-1};
	        int i=0;
	        StringTokenizer tokens = new StringTokenizer(host,".");
	        if (tokens.countTokens() > 4) return null;
	        while (tokens.hasMoreTokens()) {
	            try {
	                address[i++] = Integer.parseInt(tokens.nextToken()) & 0xFF;
	            } catch(NumberFormatException nfe) {
	                return null;
	            }
	        }
	        return address;
	    }
}
