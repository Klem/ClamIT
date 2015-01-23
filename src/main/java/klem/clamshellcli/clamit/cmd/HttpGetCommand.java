package klem.clamshellcli.clamit.cmd;


import klem.clamshellcli.clamit.ClamITController;
import klem.clamshellcli.clamit.impl.proxy.ProxyConfig;
import klem.clamshellcli.clamit.utils.Constants;
import klem.clamshellcli.clamit.utils.Utils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

// TODO HANDLE PROXY
//HttpClient client = new DefaultHttpClient();
//HttpHost proxy = new HttpHost("10.61.184.32", 8080);
//client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

public class HttpGetCommand implements Command {
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	private static final String HELP = "help";
	private static final String ACTION_NAME = "httpget";
	
	private IOConsole console;

	@Override
	public void plug(Context ctx) {
		console = ctx.getIoConsole();
	}

	@Override
	public Object execute(Context ctx) {

		String[] values = (String[]) ctx.getValue(Context.KEY_COMMAND_LINE_ARGS);

		if (values == null || values.length != 1) {
			console.writeOutput(String.format("%s%n%n", "This command has arguments : " + getDescriptor().getUsage()));
			return null;
		}
		
		String action = values[0];
		
		if(action.equals(HELP)) {
			 console.writeOutput(Utils.getFileContentAsString(Constants.Dir.HELP, Constants.Files.HTTPGET));
			 return null;
		}

		String url = values[0];

		HttpClient client = new DefaultHttpClient();
		ProxyConfig proxyConfig = (ProxyConfig) ctx.getValue(Constants.Context.PROXY_CONFIG);
			
		// CHECK FOR PROXY
		if(ctx.getValue(Constants.Context.PROXY_CONFIG) != null) {
			console.writeOutput(String.format("%s%n", "Proxy in use "+proxyConfig.getName()));
			
			HttpHost proxy = new HttpHost(proxyConfig.getHost(), Integer.valueOf(proxyConfig.getPort()));
			
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		
		try {
			HttpGet request = new HttpGet(url);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			

			HttpResponse response = client.execute(request);
			String responseBody = responseHandler.handleResponse(response);
			Header[] responseHeaders = response.getAllHeaders();
			HttpParams params = request.getParams();
			

			console.writeOutput(String.format("Requesting %s%n", request.getURI()));
			console.writeOutput("----------------------------------------\n");
			console.writeOutput(String.format("%s%n", response.getStatusLine().toString()));
			console.writeOutput("----------------------------------------\n");
			displayHeaders(responseHeaders);
			console.writeOutput("----------------------------------------\n");
			console.writeOutput(String.format("%s%n",responseBody));
			console.writeOutput("----------------------------------------\n");
		
		} catch (ClientProtocolException cpe) {
            cpe.printStackTrace();
		} catch (IOException ioe) {
            ioe.printStackTrace();
		} finally {
			
			client.getConnectionManager().shutdown();
		}
	
		
		
		return null;
	}
	
	private void displayHeaders(Header[] headers) {
		for (int i = 0; i < headers.length; i++) {
			Header h = headers[i];
			console.writeOutput(String.format(" %-15s : %s %n", h.getName(), h.getValue()));
		}
	}

	@Override
	public Descriptor getDescriptor() {
		return new Command.Descriptor() {
			@Override
			public String getNamespace() {
				return NAMESPACE;
			}

			@Override
			public String getName() {
				return ACTION_NAME;

			}

			@Override
			public String getDescription() {
				return "Get the HTTP headers and response body from an Url. Support Url with parameters";

			}

			@Override
			public String getUsage() {
				return "Type: httpget [ip || hostname] help";

			}

			Map<String, String> args;

			@Override
			public Map<String, String> getArguments() {
				if (args != null)
					return args;
				args = new LinkedHashMap<String, String>();
				args.put(ClamITController.KEY_ARGS_URL + ":<host>", "the http host to query");
			
				return args;
			}

		};
	}

}	