package klem.clamshellcli.clamit.tests;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

public class NetworkInterfaces {

	public static void main(String args[]) throws SocketException {
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		for (NetworkInterface netint : Collections.list(nets))
			displayInterfaceInformation(netint);
	}

	static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {

		System.out.println("==========" + netint.getName().toUpperCase() + "==========");
		System.out.print(String.format("Name		: %s %n", netint.getDisplayName()));
		System.out.print(String.format("MAC address	: %s %n", Arrays.toString(netint.getHardwareAddress())));

		Enumeration inets = netint.getInetAddresses();
		String ips = "";
		while (inets.hasMoreElements()) {
			InetAddress inet = (InetAddress) inets.nextElement();
			ips += inet.getHostAddress() + ", ";
			// System.out.print(String.format("			: %s %n",
			// inet.getHostName()));
		}

		System.out.print(String.format("IP adresses	: %s %n", ips));
		System.out.print(String.format("IP adresses	: %s %n", dumpToString(netint.getInterfaceAddresses(), "  , ")));
		System.out.print(String.format("Loopback	: %b %n", netint.isLoopback()));
		System.out.print(String.format("Active		: %s %n", netint.isUp()));
		System.out.print(String.format("MTU			: %s %n", netint.getMTU()));
		System.out.print(String.format("Virtual		: %s %n", netint.isVirtual()));
		System.out.print(String.format("Point2Point	: %s %n", netint.isPointToPoint()));
		System.out.print(String.format("Multicast	: %s %n", netint.supportsMulticast()));

		// Details if is virtual
		if (netint.isVirtual()) {
			Enumeration<NetworkInterface> subInterfaces = netint.getSubInterfaces();

			while (subInterfaces.hasMoreElements()) {
				NetworkInterface sub = subInterfaces.nextElement();
				System.out.print(String.format("\n	Know subinterfaces		: %s %n", netint.isVirtual()));
				displayInterfaceInformation(sub);
			}
		}

	}

	private static String dumpToString(Collection c, String separator) {
		String retval = "";
		for (Iterator it = c.iterator(); it.hasNext();) {
			retval += String.valueOf(it.next());
			retval += separator;
		}
		return retval;
	}

}
