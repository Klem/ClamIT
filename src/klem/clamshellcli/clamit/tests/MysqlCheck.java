package klem.clamshellcli.clamit.tests;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.Connection;

public class MysqlCheck {
	
	public static void main(String[] args) {

		System.out.println("-------- MySQL JDBC Connection Testing ------------");
		Connection connection = null;  
		try {

			Class.forName("com.mysql.jdbc.Driver");
	
	
			System.out.println("MySQL JDBC Driver Registered!");


		
			connection = (Connection) DriverManager
					.getConnection("jdbc:mysql://gelabprevalodas01:3306/",
							"ddprev", "ddprev");
			
			showStatus(connection);
	
		} catch (SQLException ex) {
            Logger.getLogger(MysqlCheck.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MysqlCheck.class.getName()).log(Level.SEVERE, null, ex);
        } 

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}

	}
	
	private static void showStatus(Connection connection) throws SQLException {
		String query = "SHOW STATUS";
		PreparedStatement ps;
		ResultSet rs;
		ps = connection.prepareStatement(query);
		rs = ps.executeQuery(query);
		int cnt = 0;

		while (rs.next()) // are there more 'Variable_name'?
		{
			String key = rs.getString("Variable_name");
			String value = rs.getString("Value");
			System.out.println((String.format("%-30s::%-10s", key, value)));
			
		}
	}
	
}

