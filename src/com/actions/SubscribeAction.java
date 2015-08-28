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
import org.codehaus.jettison.json.JSONObject;

import ws.Hp_POC;
import ws.response.parser.JsonParser;

import com.dao.StoreDataDAO;
import com.subscribe.SubscribeActionForm;
import com.subscribe.UserDetailsActionForm;

public class SubscribeAction extends Action{

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		SubscribeActionForm subscribeForm = (SubscribeActionForm)form;
		HttpSession session = request.getSession();
		String forward = null;
		String token_id = (String)session.getAttribute("token");
		UserDetailsActionForm userDetailsForm =(UserDetailsActionForm)session.getAttribute("userDetails");
		JSONObject offerings =(JSONObject) session.getAttribute("offerings");
		int strLen = subscribeForm.getProdsel().length();
		subscribeForm = JsonParser.parseJson(offerings,Integer.valueOf(subscribeForm.getProdsel().substring(strLen-1)));
		String sub_id = null;
		int row_count = 0;
		 String user_id=StoreDataDAO.getUserId(userDetailsForm.getUserEmail());
		 if(user_id==null){
			 row_count = StoreDataDAO.storeUserDetails(userDetailsForm,sub_id);
		 }
			 if(row_count>0||user_id!=null){
			
				 String sub_name = subscribeForm.getDisplayName();
				 subscribeForm.setDisplayName(user_id+"_"+sub_name);
				 //ArrayList<String> subNames= StoreDataDAO.getSubscriptionNames(userDetailsForm.getUserEmail());
					sub_id  = createSubscription(token_id,subscribeForm);
				int sub_row= StoreDataDAO.storeSubscriptionId(sub_id,user_id, subscribeForm.getDisplayName());
				if(sub_row>0) {
					forward = "success";			
				}
				
			 }
		else{
			request.setAttribute("error", "");
			forward = "failure";
		}
		return mapping.findForward(forward);
	}

private static String createSubscription(String token, SubscribeActionForm subscribeForm){
	
	String sub_id = Hp_POC.createSubscription(token, subscribeForm.getSvcId(), subscribeForm.getCatalogId(), subscribeForm.getCategoryName(),subscribeForm.getDisplayName());
	return sub_id;
}
}
