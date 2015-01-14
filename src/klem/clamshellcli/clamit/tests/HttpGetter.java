package klem.clamshellcli.clamit.tests;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

public class HttpGetter {

	public static void main(String[] args){
		
		listProxies();

		HttpClient client = new DefaultHttpClient();
		HttpGet request= null;
		HttpResponse response;
		ResponseHandler<String> responseHandler = new BasicResponseHandler();


		
		try {
			request = new HttpGet("http://www.youtube.com/watch?v=VrUVLpFaUoM&feature=g-all-u");

			System.out.println("executing request " + request.getURI());


			response = client.execute(request);
			String responseBody = responseHandler.handleResponse(response);

			Header[] responseHeaders = response.getAllHeaders();
			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine().toString());
			System.out.println("----------------------------------------");
			displayHeaders(responseHeaders);
			System.out.println("----------------------------------------");
			System.out.println(responseBody);
			System.out.println("----------------------------------------");
	
		}catch (IOException ioe) {
			ioe.printStackTrace();
			
		} catch (RuntimeException ex) {

	         request.abort();
	         throw ex;
	

		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			client.getConnectionManager().shutdown();
		}
	}

	private static void listProxies() {
		 try {  
			  
	            System.setProperty("java.net.useSystemProxies","true");  
	            List l = ProxySelector.getDefault().select(  
	                        new URI("http://www.yahoo.com/"));  
	  
	            for (Iterator iter = l.iterator(); iter.hasNext(); ) {  
	  
	                Proxy proxy = (Proxy) iter.next();  
	  
	                System.out.println("proxy hostname : " + proxy.type());  
	  
	                InetSocketAddress addr = (InetSocketAddress)  
	                    proxy.address();  
	                System.out.println("proxy.address "+proxy.address());  
	                if(addr == null) {  
	  
	                    System.out.println("No Proxy");  
	  
	                } else {  
	  
	                    System.out.println("proxy hostname : " +  
	                            addr.getHostName());  
	  
	                    System.out.println("proxy port : " +  
	                            addr.getPort());  
	  
	                }  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
		
	}

	private static void displayHeaders(Header[] headers) {
		for (int i = 0; i < headers.length; i++) {
			Header h = headers[i];
			System.out.println(h.getName() + ":" + h.getValue());

		}
	}
}
