package klem.clamshellcli.clamit.tests;

import java.util.Iterator;
import java.util.Map;

public class SysInfo {

	public static void main(String[] args) {
		Map<String, String> getenv = System.getenv();
		
		Iterator<String> iterator = getenv.keySet().iterator();
		
		while(iterator.hasNext()) {
			String key = iterator.next();
			
			System.out.print(String.format("%n	%s	::	%s	", key, getenv.get(key)));
		}
		
		
	}
}
