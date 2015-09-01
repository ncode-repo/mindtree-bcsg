package com.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

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

import com.dao.UserDetailsDAO;
import com.subscribe.UserDetailsActionForm;

public class UserDetailsAction extends Action{

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		UserDetailsActionForm subscribeForm = (UserDetailsActionForm)form;
		HttpSession session = request.getSession();
		if("chkEmail".equalsIgnoreCase(subscribeForm.getEvent())){
			JSONObject jsonObject = null;
			jsonObject = getUserDetails(subscribeForm.getUserEmail());
			response.setContentType("application/json");
			// Get the printwriter object from response to write the required json object to the output stream      
			PrintWriter out = response.getWriter();
			// Assuming your json object is **jsonObject**, perform the following, it will return your json object  
			out.print(jsonObject);
			out.flush();
			return null;
		}
		else{
		JSONObject members = getSvcMembers(session);
		session.setAttribute("userDetails", subscribeForm);
		session.setAttribute("offerings", members);
		return mapping.findForward("subscribe");
	}
	}

	private static JSONObject getSvcMembers(HttpSession session){
		JSONObject members = null;
		String token_id = Hp_POC.generateToken();
		if(token_id!=null){
			session.setAttribute("token", token_id);
			members = Hp_POC.listSvcOfferings(token_id, "");
		}
		return members;
	}
	private static JSONObject getUserDetails(String email){
		JSONObject jsonObject =	UserDetailsDAO.getUserDetails(email);
		return jsonObject;
	}
}