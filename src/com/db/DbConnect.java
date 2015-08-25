package com.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DbConnect {

	public Connection getConnection(){
		  Connection con = null;
		  InputStream in = getClass().getResourceAsStream("/conf/conf.properties");
		  Properties properties = new Properties();
		  try {
			properties.load(in);
		        Class.forName(properties.getProperty("jdbc.driverClassName"));
		        con = DriverManager.getConnection(properties.getProperty("jdbc.url"), properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));
		        if (con == null) {
		            System.out.println("Connection cannot be established");
		        }
		        return con;
		    } catch (Exception e) {
		        System.out.println(e);
		    }
		    return null;
	}
	
}
