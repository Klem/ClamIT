package klem.clamshellcli.clamit.tests;

import java.io.IOException;
import java.net.InetAddress;

public class ScanIPCidr {

	public static void main(String[] args) throws Exception {
		ScanIPCidr.start("10.61.148.1/24");
	}

	public static void start(String cidrAddress) throws IOException {
		int[] bounds = ScanIPCidr.rangeFromCidr(cidrAddress);
		
		System.out.print(String.format("%n|	%s	|%n|	IP	|	HOSTNAME	|", cidrAddress ));
		
		for (int i = bounds[0]; i <= bounds[1]; i++) {
			String address = InetRange.intToIp(i);
			InetAddress ip = InetAddress.getByName(address);
			String hostname;
			if (ip.isReachable(2000)) {
				hostname = ip.getCanonicalHostName();
			} else {
				hostname = "unreachable";
			}
			System.out.print(String.format("%n|	%s	|	%s	|", ip, hostname ));
		}

	}


	private static int[] rangeFromCidr(String cidrIp) {
		int maskStub = 1 << 31;
		String[] atoms = cidrIp.split("/");
		int mask = Integer.parseInt(atoms[1]);

		int[] result = new int[2];
		result[0] = InetRange.ipToInt(atoms[0]) & (maskStub >> (mask - 1)); // lower
																			// bound
		result[1] = InetRange.ipToInt(atoms[0]); // upper bound

		return result;
	}

	static class InetRange {
		public static int ipToInt(String ipAddress) {
			try {
				byte[] bytes = InetAddress.getByName(ipAddress).getAddress();
				int octet1 = (bytes[0] & 0xFF) << 24;
				int octet2 = (bytes[1] & 0xFF) << 16;
				int octet3 = (bytes[2] & 0xFF) << 8;
				int octet4 = bytes[3] & 0xFF;
				int address = octet1 | octet2 | octet3 | octet4;

				return address;
			} catch (Exception e) {
				e.printStackTrace();

				return 0;
			}
		}

		public static String intToIp(int ipAddress) {
			int octet1 = (ipAddress & 0xFF000000) >>> 24;
			int octet2 = (ipAddress & 0xFF0000) >>> 16;
			int octet3 = (ipAddress & 0xFF00) >>> 8;
			int octet4 = ipAddress & 0xFF;

			return new StringBuffer().append(octet1).append('.').append(octet2)
					.append('.').append(octet3).append('.').append(octet4)
					.toString();
		}
	}
}
