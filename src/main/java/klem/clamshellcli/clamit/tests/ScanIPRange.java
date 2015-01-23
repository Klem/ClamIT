package klem.clamshellcli.clamit.tests;

import klem.clamshellcli.clamit.tests.ScanIPCidr.InetRange;
import klem.clamshellcli.clamit.utils.Utils;

import java.net.InetAddress;
import java.util.StringTokenizer;

public class ScanIPRange {
	 public static void main(String[] args) {
		 
		 String ipFrom = "10.61.148.1";
		 String ipTo = "10.61.148.255";
		 
	        try {
	        	int ipStart;
		        int ipEnd;
	            if(Utils.validateIpFormat(ipFrom)) {
	    			if(Utils.validateIpFormat(ipTo)) {
	    				System.out.print(String.format("About to scan IP within range : %s ==> %s....%n", ipFrom, ipTo));
	    				 
	    				ipStart = host2int(ipFrom);
	    		        ipEnd = host2int(ipTo);
	    		        
	    		        for (int i=ipStart; i<=ipEnd; i++) {
	    		        	
	    		        	String address = InetRange.intToIp(i);
	    					InetAddress ip = InetAddress.getByName(address);
	    					String hostname;
	    					long currentTime = System.currentTimeMillis();
	    					long ping = 0L;
	    					if (ip.isReachable(2000)) {
	    						ping = System.currentTimeMillis() - currentTime;
	    						hostname = ip.getCanonicalHostName();
	    					} else {
	    						hostname = "unreachable";
	    					}
	    					System.out.print(String.format("%n|	%s	|	%s	|	%s ms	|", ip, hostname, ping ));
	    	            }
	    		        
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


	    private static long ip2long(InetAddress ip) {
	        long l=0;
	        byte[] addr = ip.getAddress();
	        if (addr.length == 4) { //IPV4
	            for (int i=0;i<4;++i) {
	                l += (((long)addr[i] &0xFF) << 8*(3-i));
	            }
	        } else { //IPV6
	            return 0;  // I dont know how to deal with these
	        }
	        return l;
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

	    private static String long2dotted(long address) {
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0, shift = 24; i < 4; i++, shift -= 8) {
	            long value = (address >> shift) & 0xff;
	            sb.append(value);
	            if (i != 3) {
	                sb.append('.');
	            }
	        }
	        return sb.toString();
	    }

}
