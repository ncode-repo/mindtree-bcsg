package com.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnect {
	private static Connection connection = null;
	public Connection createConnection(){
		  InputStream in = getClass().getResourceAsStream("/conf/conf.properties");
		  Properties properties = new Properties();
		  try {
			properties.load(in);
		        Class.forName(properties.getProperty("jdbc.driverClassName"));
		        connection = DriverManager.getConnection(properties.getProperty("jdbc.url"), properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));
		        if (connection == null) {
		            System.out.println("Connection cannot be established");
		        }
		        return connection;
		    } catch (Exception e) {
		        System.out.println(e);
		    }
		    return null;
	}
	public static Connection getConnection() {
		boolean flag = false;
		if(connection!=null){
			try {
				flag = connection.isClosed();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(!flag)
			return connection;
			else
				return new DbConnect().createConnection();
		}
		else{
			return new DbConnect().createConnection();
		}
	}
	public static void setConnection(Connection dbConnection) {
		DbConnect.connection = dbConnection;
	}
	
	public static void closeConnection(){
		try {
			connection.close();
			connection=null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
