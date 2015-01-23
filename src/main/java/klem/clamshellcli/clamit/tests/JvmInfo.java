package klem.clamshellcli.clamit.tests;

import java.util.Iterator;
import java.util.Properties;

public class JvmInfo {
    public static void main(String[] args) {
    	 int mb = 1024*1024;
         
         //Getting the runtime reference from system
         Runtime runtime = Runtime.getRuntime();
          
         System.out.print("#####System Stats #####");
         
         System.out.print(String.format("Available processors (cores): %s", Runtime.getRuntime().availableProcessors()));
         //Print used memory
         System.out.print(String.format(" %nUsed Memory	: %s Mb", (runtime.totalMemory() - runtime.freeMemory()) / mb));
  
         //Print free memory
         System.out.print(String.format(" %nFree Memory	: %s Mb", (runtime.freeMemory() / mb)));
          
         //Print total available memory
         System.out.print(String.format(" %nTotal Memory	: %s Mb", (runtime.totalMemory() / mb)));
  
         //Print Maximum available memory
         System.out.print(String.format(" %nMax Memory		: %s Mb", (runtime.maxMemory() / mb)));
         
 			Properties properties = System.getProperties();
 			
 			Iterator<Object> iterator = properties.keySet().iterator();
 			
 			while(iterator.hasNext()) {
 				String key = (String) iterator.next();
 				
 				System.out.print(String.format("%n	%s	::	%s	", key, properties.get(key)));
 			}
 			
    }
}