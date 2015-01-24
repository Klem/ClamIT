package klem.clamshellcli.clamit.cmd;

// TODO :: offer all the "SHOW" commands


import com.mysql.jdbc.Connection;
import klem.clamshellcli.clamit.ClamITController;
import org.clamshellcli.api.Command;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;
import org.clamshellcli.core.ShellException;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

public class MysqlCheckCommand implements Command {
	private static final String NAMESPACE = ClamITController.CLAMIT_NAMESPACE;
	private static final String ACTION_NAME = "mysqlcheck";
	private static final String CHARSET = "charset";
	private static final String COLLATION = "collation";
	private static final String DATABASES = "databases";
	private static final String STATUS = "status";
	private static final String VARIABLES = "variables";
	private static final String DISCONNECT = "disconnect";
	private IOConsole console;

	@Override
	public void plug(Context ctx) {
		console = ctx.getIoConsole();
	}

	@Override
	public Object execute(Context ctx) {

		String host = console.readInput("Host: ");
		String port = console.readInput("Port: ");
		String user = console.readInput("User: ");
		String password = console.readInput("Password: ");

		

		String url;
		
		console.writeOutput(String.format(
				"%nAttempting to connect to database %s on port %s using credential %s/%s",
			     host, port, user, password));

		Connection connection = null;
		try {

			Class.forName("com.mysql.jdbc.Driver");
			console.writeOutput(String.format("%nMySQL JDBC Driver Registered!"));

			port = ":"+port;
			
			url = "jdbc:mysql://" + host + port + "/";
			
			console.writeOutput(String.format("%nConnecting to : %s", url));
			
			connection = (Connection) DriverManager.getConnection(url, user, password);
			


		if (connection != null) {
			console.writeOutput(String.format("%nConnected to server!"));
			
			askForCommand(connection);
			
			return null;
		} else {
			console.writeOutput(String.format("%nUnable to connect!"));
			return null;
		}
		
		} catch (SQLException ex) {
			throw new ShellException(ex.getMessage(), ex.getCause());

		} catch (ClassNotFoundException ex) {
			throw new ShellException(ex.getMessage(), ex.getCause());
		}

	}
	
	private void askForCommand(Connection connection) throws SQLException {
		String getStatus = console.readInput("\nNow What : \ncharset, collation, databases, status, variables, disconnect ? : ");
		
		if(getStatus.equalsIgnoreCase(CHARSET)) {
			showCharset(connection);
			askForCommand(connection);
		} 
		else if(getStatus.equalsIgnoreCase(COLLATION)) {
			showCollation(connection);
			askForCommand(connection);
		} 
		else if(getStatus.equalsIgnoreCase(DATABASES)) {
			showDatabases(connection);
			askForCommand(connection);
		}
		else if(getStatus.equalsIgnoreCase(STATUS)) {
			showStatus(connection);
			askForCommand(connection);
		}
		else if(getStatus.equalsIgnoreCase(VARIABLES)) {
			showVariables(connection);
			askForCommand(connection);
		}
		else if(getStatus.equalsIgnoreCase(DISCONNECT)) {
			disconnect(connection);
		}
		else {
			console.writeOutput(String.format("%nInvalid input : %s", getStatus));
			askForCommand(connection);
		}
		
		
	}
	
	private void showCharset(Connection connection) throws SQLException {
		String query = "SHOW CHARSET";
		PreparedStatement ps;
		ResultSet rs;
		ps = connection.prepareStatement(query);
		rs = ps.executeQuery(query);


		while (rs.next())
		{
			String charset = rs.getString("Charset");
			String desc = rs.getString("Description");
			String def = rs.getString("Default collation");
			String max = rs.getString("Maxlen");
			console.writeOutput(String.format("%n|%-10s|%-40s|%-20s|%-10s", charset, desc, def, max));
			
		}
	}
	
	private void showCollation(Connection connection) throws SQLException {
		String query = "SHOW COLLATION";
		PreparedStatement ps;
		ResultSet rs;
		ps = connection.prepareStatement(query);
		rs = ps.executeQuery(query);


		while (rs.next())
		{
			String collation = rs.getString("Collation");
			String charset = rs.getString("Charset");
			String id = rs.getString("Id");
			String def = rs.getString("Default");
			String compile = rs.getString("Compiled");
			String sortlen = rs.getString("Sortlen");
			console.writeOutput(String.format("%n|%-30s|%-10s|%-5s|%-10s|%-10s", collation, charset, id, def, compile, sortlen));
			
		}
	}
	
	private void showDatabases(Connection connection) throws SQLException {
		String query = "SHOW DATABASES";
		PreparedStatement ps;
		ResultSet rs;
		ps = connection.prepareStatement(query);
		rs = ps.executeQuery(query);


		while (rs.next()) // are there more 'Variable_name'?
		{
			String database = rs.getString("Database");
			console.writeOutput(String.format("%n|%-30s|", database));
			
		}
	}

	
	private void showStatus(Connection connection) throws SQLException {
		String query = "SHOW STATUS";
		PreparedStatement ps;
		ResultSet rs;
		ps = connection.prepareStatement(query);
		rs = ps.executeQuery(query);


		while (rs.next()) // are there more 'Variable_name'?
		{
			String key = rs.getString("Variable_name");
			String value = rs.getString("Value");
			console.writeOutput(String.format("%n|%-60s|%-10s|", key, value));
			
		}
	}
	
	private void showVariables(Connection connection) throws SQLException {
		String query = "SHOW VARIABLES";
		PreparedStatement ps;
		ResultSet rs;
		ps = connection.prepareStatement(query);
		rs = ps.executeQuery(query);


		while (rs.next()) // are there more 'Variable_name'?
		{
			String key = rs.getString("Variable_name");
			String value = rs.getString("Value");
			console.writeOutput(String.format("%n|%-60s|%-30s|", key, value));
			
		}
	}
	
	
	
	private void disconnect(Connection connection) throws SQLException {
		connection.close();
		console.writeOutput(String.format("%nDisconnected"));
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
				return "Connect to a MysqlServer and display server status";

			}

			@Override
			public String getUsage() {
				return "Type 'mysqlcheck'. Connection information will be asked along the way";

			}

			@Override
			public Map<String, String> getArguments() {
				return Collections.emptyMap();

			}

		};

	}

}
