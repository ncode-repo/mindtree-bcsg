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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		System.out.println("Inside execute method");
		JspWriteForm form1 = (JspWriteForm)form;
		String fwd = processWrite(mapping, form1, request, response);
		request.setAttribute("jsp_write", "true");
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
		forward = writeToJsp(params_list,buttons_list, jsp_name,request);
		
		return forward;
	}
	
	public String writeToJsp(HashMap<String, String> params,
			HashMap<String, String> buttons_map, String jsp_loc,HttpServletRequest request) {
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
			html.append("<td>");html.append(MessageFormat.format((String) prop.get("textTag"), var, var) + "\n");html.append("</td>");
			html.append("</tr><br>"+"\n");
		}
		html.append("</table>");
		for(Map.Entry<String, String> entry: buttons_map.entrySet()){
			html.append(MessageFormat.format((String) prop.get("buttonTag"), Constants.BUTTON+i, entry.getKey()) + "\n");
			html.append(MessageFormat.format((String) prop.get("eventTag"), Constants.EVENT+i, entry.getValue()) + "\n");
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
				bw.write(content.toString());
				bw.close();
				System.out.println("Content is written to jsp");
				forward = "success";
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("Jsp cannot be written");
			forward="error";
		}
		return forward;
	}

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

	public String createJspLocation(String jspName,HttpServletRequest request) {
		boolean flag = false;
		String proj_location = "";
		File jsp = null;
		String web =request.getServletContext().getContextPath();//"WebContent\\mockUI";
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
			System.out.println("File " + jspName + " is already created");
		}
		return jsp.getPath();
	}
}
