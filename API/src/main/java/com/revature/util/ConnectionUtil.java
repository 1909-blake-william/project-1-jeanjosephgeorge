package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	static {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		String url = System.getenv("EXPENSE_URL");
		String username = System.getenv("EXPENSE_USERNAME");
		String password = System.getenv("EXPENSE_PASSWORD");
		System.out.println("Successfully Connected to database");
		return DriverManager.getConnection(url, username, password);
	}

}
