package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.db.DbConnect;
import com.subscribe.UserDetailsActionForm;

public class StoreDataDAO {
	

	public static int storeUserDetails(UserDetailsActionForm userDetailsForm, String id) {
		Connection dbConnection = DbConnect.getConnection();
		PreparedStatement preparedStatement = null;
		int i = 0;
		if (dbConnection != null) {
			try {
				preparedStatement = dbConnection
						.prepareStatement("insert into user_details(`first_name`,`last_name`,`email_address`,`telephone`) values (?, ?, ?, ?)");
				preparedStatement.setString(1, userDetailsForm.getFirstName());
				preparedStatement.setString(2, userDetailsForm.getLastName());
				preparedStatement.setString(3, userDetailsForm.getUserEmail());
				preparedStatement.setString(4, userDetailsForm.getTelephone());
				i = preparedStatement.executeUpdate();
				
				/*if(1>0){
					Statement stmt = dbConnection.createStatement();//("select user_id from subscription_details where subscription_id = 'qqq'");
					//preparedStatement.setString(1, email);
					ResultSet rs =	stmt.executeQuery("select user_id from user_details where email_address = '"+userDetailsForm.getUserEmail()+"'");
				//ResultSet rs = preparedStatement.executeQuery();
				boolean row = rs.next();
				if(row){
					String user_id = String.valueOf(rs.getLong("user_id"));
				
					preparedStatement = dbConnection
							.prepareStatement("insert into subscription_details(`user_id`,`subscription_id`) values ( ?, ?)");
					preparedStatement.setString(1, user_id);

					preparedStatement.setString(2, id);
					i = preparedStatement.executeUpdate();
				}
				}*/
			} catch (SQLException s) {
				System.out.println("SQL Exception while inserting details: "
						+ s.getMessage());
				return i;
			} catch (Exception e) {
				System.out.println("Exception while inserting details: "
						+ e.getMessage());
				return i;
			} finally {
				try {
					preparedStatement.close();
					DbConnect.closeConnection();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return i;
	}

	public static int storeSubscriptionId(String id,String user_id,String name) {
		Connection dbConnection = DbConnect.getConnection();
		PreparedStatement preparedStatement = null;
		int i = 0;
		if (dbConnection != null) {
			try {
				//String user_id = getUserId(email);
				preparedStatement = dbConnection
						.prepareStatement("insert into subscription_details(`user_id`,`subscription_id`,`subscription_name`) values ( ?, ?,?)");
				preparedStatement.setString(1, user_id);

				preparedStatement.setString(2, id);
				preparedStatement.setString(3, name);
				i = preparedStatement.executeUpdate();
			} catch (SQLException s) {
				System.out.println("SQL Exception while inserting subscription ID: "
						+ s.getMessage());
				return i;
			} catch (Exception e) {
				System.out.println("Exception while inserting subscription ID: "
						+ e.getMessage());
				return i;
			} finally {
					//DbConnect.closeConnection();
			}
		}
		return i;
	}

	public static String getUserId(String email) {
		PreparedStatement preparedStatement = null;
		Connection dbConnection = DbConnect.getConnection();
		int i = 0;
		String user_id = null;
		if (dbConnection != null) {
			try {
				preparedStatement = dbConnection
						.prepareStatement("select user_id from user_details where email_address = ?");
				preparedStatement.setString(1, email);
				ResultSet rs = preparedStatement.executeQuery();
				boolean row = rs.next();
				user_id = String.valueOf(rs.getLong("user_id"));
			} catch (SQLException s) {
				System.out.println("SQL Exception while fetching user_id: "
						+ s.getMessage());
				s.printStackTrace();
			} catch (Exception e) {
				System.out.println("Exception while fetching user_id: "
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
		return user_id;
	}
	public static ArrayList<String> getSubscriptionIds(String email) {
		Connection dbConnection = DbConnect.getConnection();
		PreparedStatement preparedStatement = null;
		int i = 1;
		ArrayList<String> sub_ids = new ArrayList<String>();
		if (dbConnection != null) {
			try {
				preparedStatement = dbConnection
						.prepareStatement("select subscription_id from subscription_details where user_id in (select user_id from user_details where email_address = ?)");
				preparedStatement.setString(1, email);
				ResultSet rs = preparedStatement.executeQuery();
				while(rs.next()){
					sub_ids.add(rs.getString(i));
					i++;
				}
			} catch (SQLException s) {
				System.out.println("SQL Exception while fetching subscription_id: "
						+ s.getMessage());
			} catch (Exception e) {
				System.out.println("Exception while fetching subscription_id: "
						+ e.getMessage());
			} finally {
				DbConnect.closeConnection();
			}

		}
		return sub_ids;
	}
	
	public static ArrayList<String> getSubscriptionNames(String email) {
		Connection dbConnection = DbConnect.getConnection();
		PreparedStatement preparedStatement = null;
		int i = 1;
		ArrayList<String> sub_ids = new ArrayList<String>();
		if (dbConnection != null) {
			try {
				preparedStatement = dbConnection
						.prepareStatement("select subscription_name from subscription_details where user_id in (select user_id from user_details where email_address = ?)");
				preparedStatement.setString(1, email);
				ResultSet rs = preparedStatement.executeQuery();
				while(rs.next()){
					sub_ids.add(rs.getString(i));
				}
			} catch (SQLException s) {
				System.out.println("SQL Exception while fetching subscription_id: "
						+ s.getMessage());
			} catch (Exception e) {
				System.out.println("Exception while fetching subscription_id: "
						+ e.getMessage());
			} finally {
				DbConnect.closeConnection();
			}

		}
		return sub_ids;
	}
}