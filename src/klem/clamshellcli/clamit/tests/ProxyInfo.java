package klem.clamshellcli.clamit.tests;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import klem.clamshellcli.clamit.impl.proxy.ProxyConfig;
import klem.clamshellcli.clamit.impl.proxy.ProxyHandler;

public class ProxyInfo {

	public static void main(String[] args) throws Exception {
		ProxyConfig pc = new ProxyConfig("Gefco", "10.61.184.32", "8080");
		System.out.println("Proxy to create "+pc.toString());
		
		ProxyHandler ph = new ProxyHandler();
		
		ph.create(pc);
		
		ProxyConfig prx = new ProxyConfig("Gefco", "prout", "yop");
		
		ph.create(prx);

		
	}
}
