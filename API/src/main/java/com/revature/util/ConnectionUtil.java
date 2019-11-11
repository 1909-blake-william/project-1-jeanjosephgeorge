package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

	public static Connection getConnection() throws SQLException {
		String url = System.getenv("EXPENSE_URL");
		String username = System.getenv("EXPENSE USERNAME");
		String password = System.getenv("EXPENSE_PASSWORD");
		return DriverManager.getConnection(url, username, password);
	}
	
}
