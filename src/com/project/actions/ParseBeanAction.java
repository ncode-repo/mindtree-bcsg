package com.project.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.project.forms.JspWriteForm;
import com.project.forms.ParseBeanForm;
import com.project.util.Constants;

/**
 * Struts File Upload Action Form.
 * 
 */
public class ParseBeanAction extends Action {

	private String processParseBean(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		JspWriteForm myForm = (JspWriteForm) form;

		String forward = "success";

		// Process the FormFile
		FormFile javaBean = myForm.getJavaBean();
		// Get the file name
		String fileName = javaBean.getFileName();
		File fileToCreate = null;
		HashMap<String, String> params = new HashMap<String, String>();
		// Get the servers upload directory real path name
		String filePath = request.getContextPath();
		if (!"".equals(fileName)) {
			// Create file
			File fileDirectory = new File(filePath);
			if (!fileDirectory.isDirectory()) {
				fileDirectory.mkdir();
			}
			fileName = fileName + "_" + System.currentTimeMillis();

			fileToCreate = new File(filePath, fileName);
			// If file does not exists create file
			FileOutputStream fileOutStream;
			try {
				fileOutStream = new FileOutputStream(fileToCreate);
				fileOutStream.write(javaBean.getFileData());
				fileOutStream.flush();
				fileOutStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			params = parse(fileToCreate);
			forward = "success";
		} else {
			forward = "error";
		}
		fileToCreate.delete();
		request.setAttribute("params", params);
		return forward;
	}
/**
 * Parse the input form bean variables into a map
 * @param formBean
 * @return
 */
	private HashMap<String, String> parse(File formBean) {
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(formBean));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String strLine = "";

		// read file line by line
		try {
			while ((strLine = br.readLine()) != null) {
				//
				strLine = strLine.replaceAll(", ", ","); // To get the hashmap
															// datatypes.
				if (strLine.trim().startsWith(Constants.KEYWORD_PRIVATE)) {
					String[] token = strLine.split(" ");
					if (token.length >= 3
							&& (strLine.contains("=") || strLine.contains(";"))) {
						if (token[0].trim().equalsIgnoreCase(Constants.KEYWORD_PRIVATE)) {
							boolean flag = true;
							for (int i = 1; i < token.length; i++) {
								String dataType = token[i];
								String variableName = "";
								if (i < token.length - 1 && flag) {
									if (token[i + 1].trim().endsWith("=")
											|| token[i + 1].trim()
													.endsWith(";")
											|| token[i + 1].trim().equals(
													Constants.KEYWORD_FINAL)) {
										int j = token[i + 1].trim()
												.indexOf("=");
										if (j == -1) {
											j = token[i + 1].trim()
													.indexOf(";");
										}
										if (j > -1)
											variableName = token[i + 1].trim()
													.substring(0, j);
									} else {
										variableName = token[i + 1].trim();
									}
									if (!variableName.trim().equalsIgnoreCase(
											"")){
										paramsMap.put(variableName, dataType);
									}
									flag = false;
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paramsMap;
	}

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		String fwd = processParseBean(mapping, form, request, response);
		return mapping.findForward(fwd);
		// return super.execute(mapping, form, request, response);
	}
}
