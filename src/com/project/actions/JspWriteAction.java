package com.project.actions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.project.forms.JspWriteForm;
import com.project.util.Constants;

public class JspWriteAction extends Action {
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		JspWriteForm jspForm = (JspWriteForm)form;
		String fwd = processWrite(mapping, jspForm, request, response);
		
		return mapping.findForward(fwd);
		//return super.execute(mapping, form, request, response);
	}
	
	private String processWrite(ActionMapping mapping, JspWriteForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String forward="success";
		String jsp_name = "test.jsp";
		HashMap<String,String> params_list = new HashMap<String, String>();
		HashMap<String,String> buttons_list = new HashMap<String, String>();
		String[] captions =  form.getCaption();
		String[] events = form.getEvent();
		for(String param_name:form.getValues()){
			params_list.put(param_name, Constants.TEXT);
		}
		for(int i= 0; i < captions.length; i++){
			buttons_list.put(captions[i], events[i]);
		    }
		forward = writeToJsp(params_list,buttons_list, jsp_name,request,response);
		if(forward.equalsIgnoreCase("success")){
			request.setAttribute("jsp_write", "true");
		}else if(forward.equalsIgnoreCase("error")){
			request.setAttribute("jsp_write", "false");
		}
		return forward;
	}

/**
 * Write a jsp with input params and buttons	
 * @param params
 * @param buttons_map
 * @param jsp_loc
 * @param request
 * @return
 */
	public String writeToJsp(HashMap<String, String> params,
			HashMap<String, String> buttons_map, String jsp_loc,HttpServletRequest request,HttpServletResponse response) {
		String[] variableNames = params.keySet().toArray(new String[0]);
		StringBuilder content = new StringBuilder();
		BufferedReader br = null;
		Properties prop = null;
		InputStream input = null;
		String forward = "";
		int i=1; //counter
		
		try {
			prop = getPropertiesFromClasspath(Constants.PROP_LOCATION);
			input =request.getServletContext().getResourceAsStream(Constants.TEMPLATE_LOCATION);
			br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			String line = br.readLine();
			while (line != null) {
				content.append(line);
				content.append(System.getProperty("line.separator"));
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuilder html = new StringBuilder();
		html.append("<table>");
		for (String var : variableNames) {
			html.append("<tr>");
			html.append("<td>");html.append(var + ": ");html.append("</td>");
			html.append("<td>");html.append(MessageFormat.format((String) prop.get(Constants.TEXT_TAG), var, var) + "\n");html.append("</td>");
			html.append("</tr><br>"+"\n");
		}
		html.append("</table>");
		for(Map.Entry<String, String> entry: buttons_map.entrySet()){
			html.append(MessageFormat.format((String) prop.get(Constants.BUTTON_TAG), Constants.BUTTON+i, entry.getKey(),entry.getValue()) + "\n");
			//html.append(MessageFormat.format((String) prop.get("eventTag"), Constants.EVENT+i, entry.getValue()) + "\n");
			i++;
		}
		for (int index = 0; index < content.length(); index++) {
		    if (index == content.lastIndexOf(Constants.TAG)) {
		    	content.replace(index, index+Constants.TAG.length(), html.toString());
		    	break;
		    }
		}
		String created_jsp = createJspLocation(jsp_loc,request);
		File jsp = new File(created_jsp);
		if (jsp.canWrite()) {
			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				fw = new FileWriter(jsp.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				//response.setContentLength( fileSize); 
				//response.setContentType("application/x-download");
				//response.setHeader("Content-disposition", "attachment; filename="
				//+ jsp_loc);
				/*response.setHeader("Cache-Control",
				"max-age=" + TIMEOUT);*/
				/*ServletOutputStream outStream = response.getOutputStream();
				outStream.write(content.toString().getBytes());
				outStream.flush();
				outStream.close();*/
				bw.write(content.toString());
				bw.close();
				forward = "success";
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			forward="error";
		}
		return forward;
	}

/**
 * Load properties file	
 * @param propFileName
 * @return
 * @throws IOException
 */
	private Properties getPropertiesFromClasspath(String propFileName)
			throws IOException {
		Properties props = new Properties();
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream(propFileName);

		if (inputStream == null) {
			throw new FileNotFoundException("property file '" + propFileName
					+ "' not found in the classpath");
		}

		props.load(inputStream);
		return props;
	}

/**
 * Create a jsp before writing	
 * @param jspName
 * @param request
 * @return
 */
	public String createJspLocation(String jspName,HttpServletRequest request) {
		boolean flag = false;
		File jsp = null;
		String web =request.getServletContext().getContextPath();
		File web_dir = new File(web);

		if (!web_dir.isDirectory()) {
			flag = web_dir.mkdirs();
		}
		jsp = new File(web_dir, jspName);
		if (jsp != null && !jsp.exists()) {
			try {
				jsp.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("File already created: "+ jspName);
		}
		return jsp.getPath();
	}
}
