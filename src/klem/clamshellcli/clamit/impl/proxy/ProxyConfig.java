package klem.clamshellcli.clamit.impl.proxy;

import java.io.Serializable;

public class ProxyConfig implements Serializable {
	private String name;
	private String host;
	private String port;
	private String user;
	private String pass;
	
	private ProxyConfig() {
		
	}

	public ProxyConfig(String name, String host, String port, String user, String pass) {
		super();
		this.name = name;
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
	}
	
	public ProxyConfig(String name, String host, String port) {
		super();
		this.name = name;
		this.host = host;
		this.port = port;

	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getPort() {
		return port;
	}
	
	public void setPort(String port) {
		this.port = port;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getPass() {
		return pass;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProxyConfig [name=");
		builder.append(name);
		builder.append(", host=");
		builder.append(host);
		builder.append(", port=");
		builder.append(port);
		builder.append(", user=");
		builder.append(user);
		builder.append(", pass=");
		builder.append(pass);
		builder.append("]");
		return builder.toString();
	}
}
