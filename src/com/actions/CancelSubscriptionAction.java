package com.actions;

import java.io.IOException;

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

import com.subscribe.CancelActionForm;

public class CancelSubscriptionAction extends Action{
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		CancelActionForm cancelActionForm = (CancelActionForm)form;
		HttpSession session = request.getSession();
		String token_id = Hp_POC.generateToken();
		if(cancelActionForm.getEvent().equalsIgnoreCase("modify_page")){
			JSONObject sub_details = Hp_POC.getSubscriptionDetails(token_id, cancelActionForm.getSvcId());
			session.setAttribute("modify_form", cancelActionForm);
			session.setAttribute("subDetails", sub_details);
			return mapping.findForward("modify");
		}else if(cancelActionForm.getEvent().equalsIgnoreCase("modify")){
			CancelActionForm modifyActionForm = (CancelActionForm)session.getAttribute("modify_form");
			String modify_id = Hp_POC.modifySubscription(token_id, modifyActionForm.getSvcId(), modifyActionForm.getCatalogId(), cancelActionForm.getCpu());
			session.setAttribute("modify_id", modify_id);
			session.setAttribute("modifySvcName", modifyActionForm.getSubName());
			return mapping.findForward("success");
		}
		else{
		String cancel_id = cancelSubscription(token_id,cancelActionForm,session);
		session.setAttribute("cancel_id", cancel_id);
		session.setAttribute("cancelSvcName", cancelActionForm.getSubName());
		return mapping.findForward("success");
		}
	}
	
	private static String cancelSubscription(String token_id,CancelActionForm cancelActionForm,HttpSession session){
		String cancel_id = null;
		
		if(token_id!=null){
			session.setAttribute("token", token_id);
			cancel_id = Hp_POC.cancelSubscription(token_id, cancelActionForm.getSvcId(), cancelActionForm.getCatalogId());
		}
		return cancel_id;
	}
	
}
