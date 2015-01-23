package klem.clamshellcli.clamit.utils;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {

	private static final String IP_PATTERN = 
	        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public static boolean validateIpFormat(final String ip){          

	      Pattern pattern = Pattern.compile(IP_PATTERN);
	      Matcher matcher = pattern.matcher(ip);
	      return matcher.matches();             
	}

	public static String dumpToString(Collection c, String separator) {
		String retval = "";
		for (Iterator it = c.iterator(); it.hasNext();) {
			retval += String.valueOf(it.next());
			retval += separator;
		}
		return retval;
	}

	public static long ipToLong(String strIP) {
		long[] ip = new long[4];
		String[] ipSec = strIP.split("\\.");
		for (int k = 0; k < 4; k++) {
			ip[k] = Long.valueOf(ipSec[k]);
		}
	
		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
	}

	public static String longToIP(long longIP) {
		StringBuffer sb = new StringBuffer("");
		sb.append(String.valueOf(longIP >>> 24));
		sb.append(".");
		sb.append(String.valueOf((longIP & 0x00FFFFFF) >>> 16));
		sb.append(".");
		sb.append(String.valueOf((longIP & 0x0000FFFF) >>> 8));
		sb.append(".");
		sb.append(String.valueOf(longIP & 0x000000FF));
	
		return sb.toString();
	}
	
	public static String getFileContentAsString(String dir, String filename) {
		
		StringBuilder builder = new StringBuilder();
		File f = new File(dir + System.getProperty("file.separator")+ filename).getAbsoluteFile();
		System.out.println("Opening file : "+f.getAbsolutePath());
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			while((line = br.readLine()) != null) {
			builder.append(line+"\n");	
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return builder.toString();
	}
	
public static String getFileContentAsString(File file) {
		
		StringBuilder builder = new StringBuilder();
		
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while((line = br.readLine()) != null) {
			builder.append(line+"\n");	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return builder.toString();
	}

	public static void writeContentToFile(String dir, String filename, byte[] content) {
		
		String filePath =new File(dir + System.getProperty("file.separator")+ filename).getAbsolutePath();
		File f =  new File(filePath);
		
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Writing to file file : "+filePath);
		OutputStream out;

		try {
			out = new FileOutputStream(f);
			out.write(content);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	

}
