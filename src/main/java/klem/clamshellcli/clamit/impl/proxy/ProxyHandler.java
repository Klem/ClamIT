package klem.clamshellcli.clamit.impl.proxy;

import klem.clamshellcli.clamit.utils.Constants;
import klem.clamshellcli.clamit.utils.SerializationUtils;
import org.clamshellcli.api.Context;

import java.io.*;

public class ProxyHandler {
	
	ProxyConfig proxy;
	
	public ProxyHandler() {
		
	}
	
	public void create(String name, String host, String port, String user, String pass) throws IOException{
		proxy = new ProxyConfig(name, host, port, user, pass);
		String filePath = new File(Constants.Dir.PROXIES + System.getProperty("file.separator")+ name).getAbsolutePath();
		File f = new File(filePath);
		try {
			save(f);
		} catch (FileNotFoundException e) {
			throw new IOException("Error: cannot write proxy config to fileSystem. Is the /proxies dir present? Do you have the proper rights?");
		}

	}
	
	public void create(ProxyConfig pc) throws IOException{
		proxy = new ProxyConfig(pc.getName(), pc.getHost(), pc.getPort(), pc.getUser(), pc.getPass());
		String filePath = new File(Constants.Dir.PROXIES + System.getProperty("file.separator")+ pc.getName()).getAbsolutePath();
		File f = new File(filePath);
		try {
			save(f);
		} catch (FileNotFoundException e) {
			throw new IOException("Error: cannot write proxy config to fileSystem. Is the /proxies dir present? Do you have the proper rights?");
		}
	}

	public ProxyConfig load(String name) throws IOException {
		
		String filePath = new File(Constants.Dir.PROXIES + System.getProperty("file.separator")+ name).getAbsolutePath();
		File f = new File(filePath);
		try {
			proxy = read(f);
		} catch (FileNotFoundException e) {
			throw new IOException("Error: proxy " + name + " does not exists");
		}
		
		return proxy;
			
	}
	
	public void delete(String name) throws IOException{
		String filePath = new File(Constants.Dir.PROXIES + System.getProperty("file.separator")+ name).getAbsolutePath();
		File f = new File(filePath);
		
		if(f.exists())  {
			f.delete();
		} else {
			throw new IOException("Error: proxy " + name + " does not exists");
		}
		
	}

	// TODO add to jvm
	public void apply(ProxyConfig proxy, Context ctx) {
		ctx.putValue(Constants.Context.PROXY_CONFIG, proxy);
	}

	// TODO remove from jvm
	public void unload(ProxyConfig proxy, Context ctx) {
		ctx.removeValue(Constants.Context.PROXY_CONFIG);
	}

	
	private void save(File f) throws FileNotFoundException {
		SerializationUtils.serialize(proxy, new FileOutputStream(f));
	}
	
	private ProxyConfig read(File f) throws FileNotFoundException {
		return (ProxyConfig) SerializationUtils.deserialize(new FileInputStream(f));

	}

}
