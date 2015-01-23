package klem.clamshellcli.clamit.utils;

import java.net.InetAddress;

public class IPUtils {

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
