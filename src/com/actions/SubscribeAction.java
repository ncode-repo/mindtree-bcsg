package com.actions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.codehaus.jettison.json.JSONObject;

import ws.Hp_POC;
import ws.response.parser.JsonParser;

import com.db.DbConnect;
import com.subscribe.SubscribeActionForm;
import com.subscribe.UserDetailsActionForm;

public class SubscribeAction extends Action{

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		SubscribeActionForm subscribeForm = (SubscribeActionForm)form;
		HttpSession session = request.getSession();
		String token_id = (String)session.getAttribute("token");
		UserDetailsActionForm userDetailsForm =(UserDetailsActionForm)session.getAttribute("userDetails");
		JSONObject offerings =(JSONObject) session.getAttribute("offerings");
		int strLen = subscribeForm.getProdsel().length();
		subscribeForm = JsonParser.parseJson(offerings,Integer.valueOf(subscribeForm.getProdsel().substring(strLen-1)));
		int row_count = storeDetails(userDetailsForm);
		if(row_count>0)
		{
			createSubscription(token_id,subscribeForm);
		return mapping.findForward("success");
		}
		else{
			request.setAttribute("error", "");
			return mapping.findForward("failure");
		}
	}

private static int storeDetails(UserDetailsActionForm userDetailsForm){
	DbConnect connect = new DbConnect();
	Connection dbConnection = connect.getConnection();
	PreparedStatement preparedStatement = null;
	int i = 0;
	if(dbConnection!=null){
		try {
		preparedStatement = dbConnection
		          .prepareStatement("insert into user_details(`first_name`,`last_name`,`email_address`,`telephone`) values (?, ?, ?, ?)");
		preparedStatement.setString(1, userDetailsForm.getFirstName());
		preparedStatement.setString(2, userDetailsForm.getLastName());
		preparedStatement.setString(3, userDetailsForm.getUserEmail());
		preparedStatement.setString(4, userDetailsForm.getTelephone());
		i = preparedStatement.executeUpdate();
		
		}catch(SQLException s){
			System.out.println("SQL Exception while inserting details: "+s.getMessage());
			return i;
		}catch(Exception e){
			System.out.println("Exception while inserting details: "+e.getMessage());
			return i;
		}finally{
			try {
				dbConnection.close();
			} catch (SQLException e) {
				System.out.println("Exception while closing the connection: "+e.getMessage());
				e.printStackTrace();
			}
		}
	}
	return i;
}

private static void createSubscription(String token, SubscribeActionForm subscribeForm){
	
	Hp_POC.createSubscription(token, subscribeForm.getSvcId(), subscribeForm.getCatalogId(), subscribeForm.getCategoryName());
}
}
