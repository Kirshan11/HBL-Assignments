package com.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static Connection con = null;

	static {
		final String url = "jdbc:as400://10.200.65.227/KFILPKC";
		final String username = "TSTOWNER";
		final String password = "TSTOWNER";

		try {

			Class.forName("com.ibm.as400.access.AS400JDBCDriver");
			System.out.println("**** Driver Loaded Successfully..! ****");

			con = DriverManager.getConnection(url, username, password);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		if (con != null)
			System.out.println("Connection Connected Successfully!!");
		else
			System.out.println("COnnection Not Conneted Successfully!!");
		return con;
	}
}
