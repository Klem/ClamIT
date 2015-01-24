package klem.clamshellcli.clamit.utils;

public class Constants {
	
	public static class Dir {
		public static final String HTTP = "http";
		public static final String FTP = "ftp";
		public static final String IN = "in";
		public static final String OUT = "out";
		public static final String HELP = "help";
		public static final String LICENCE = "licence";
		public static final String PROXIES = "proxies";
	}
	
	public static class Files {
		public static final String README ="readme";
		public static final String MOCKFTP ="mockftp";
		public static final String MOCKHTTP ="mockhttp";
		public static final String HTTPGET = "httpget";
		public static final String PROXYCONFIG = "proxyconfig";
	}
	
	public static class Input {
		public static final String YES ="y";
		public static final String NO ="n";
	}
	
	public static class Default {
		public static final int MIN_PORT =0;
		public static final int MAX_PORT =65535;
		public static final String USER ="user";
		public static final String PASSWORD ="pass";
		public static final String HOST ="localhost";
		public static final String PROXY_CONFIG = "PROXY_CONFIG";
		
	}
	
	public class Context {
		public static final String HTTPD_INSTANCE = "HTTPD_INSTANCE";
		public static final String FTP_INSTANCE = "FTP_INSTANCE";
		public static final String PROXY_CONFIG = "PROXY_CONFIG";
	}
	
}
