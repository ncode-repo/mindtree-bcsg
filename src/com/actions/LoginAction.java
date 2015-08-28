package com.actions;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import ws.Hp_POC;
import ws.response.parser.JsonParser;

import com.dao.StoreDataDAO;
import com.subscribe.LoginForm;

public class LoginAction extends Action{
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		LoginForm loginForm = (LoginForm)form;
		HttpSession session = request.getSession();
		JSONObject members = getSubscriptions(loginForm,session);
		session.setAttribute("login_details", loginForm);
		session.setAttribute("subscriptionList", members);
		return mapping.findForward("success");
	}
	
	private static JSONObject getSubscriptions(LoginForm loginForm,HttpSession session){
		JSONObject members = null;
		String token_id = Hp_POC.generateToken();
		ArrayList<String> subNames = StoreDataDAO.getSubscriptionNames(loginForm.getUserEmail());
		if(token_id!=null){
			session.setAttribute("token", token_id);
			members = Hp_POC.getSubscriptionList(token_id);
		}
		JSONObject result = JsonParser.parseJson(members,subNames);
		return result;
	}
}
