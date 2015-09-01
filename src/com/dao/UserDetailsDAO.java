package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.codehaus.jettison.json.JSONObject;

import com.db.DbConnect;

public class UserDetailsDAO {
	public static JSONObject getUserDetails(String email) {
		PreparedStatement preparedStatement = null;
		Connection dbConnection = DbConnect.getConnection();
		JSONObject userDetails = null;
		if (dbConnection != null) {
			try {
				preparedStatement = dbConnection
						.prepareStatement("SELECT `user_id`,`first_name`,`last_name`,`telephone`,`email_address` FROM `user_details` where email_address = ?");
				preparedStatement.setString(1, email);
				ResultSet rs = preparedStatement.executeQuery();
				boolean row = rs.next();
				//user_id = String.valueOf(rs.getLong("user_id"));
				if(row){
					userDetails = new JSONObject();
					userDetails.put("user_id",String.valueOf(rs.getLong("user_id")));
				userDetails.put("first_name", rs.getString("first_name"));
				userDetails.put("last_name", rs.getString("last_name"));
				userDetails.put("telephone", rs.getString("telephone"));
				userDetails.put("email_address", rs.getString("email_address"));
				}				
			} catch (SQLException s) {
				System.out.println("SQL Exception while fetching user details: "
						+ s.getMessage());
			} catch (Exception e) {
				System.out.println("Exception while fetching user details: "
						+ e.getMessage());
			} finally {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return userDetails;
	}
}
