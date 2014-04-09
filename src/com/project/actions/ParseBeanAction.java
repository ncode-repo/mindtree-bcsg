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

import com.project.forms.ParseBeanForm;

/**
 * Struts File Upload Action Form.
 * 
 */
public class ParseBeanAction extends Action {

	@SuppressWarnings("unused")
	private String processParseBean(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Inside processParseBean method");
		ParseBeanForm myForm = (ParseBeanForm) form;

		String forward = "success";

		// Process the FormFile
		FormFile myFile = myForm.getTheFile();
		// Get the file name
		String fileName = myFile.getFileName();
		File fileToCreate = null;
		System.out.println("fileName "+fileName);
		HashMap<String,String> params = new HashMap<String,String>();
		// Get the servers upload directory real path name
		String filePath = "";
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
				fileOutStream.write(myFile.getFileData());
				fileOutStream.flush();
				fileOutStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			 params = parse(fileToCreate);
			 forward ="success";
		}
		else{
			forward ="error";
		}
		
		request.setAttribute("params", params);
		fileToCreate.delete();
		return forward;
	}
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
				if (strLine.trim().startsWith("private")) {
					String[] token = strLine.split(" ");
					if (token.length >= 3
							&& (strLine.contains("=") || strLine.contains(";"))) {
						if (token[0].trim().equalsIgnoreCase("private")) {
							boolean flag = true;
							for (int i = 1; i < token.length; i++) {
								String dataType = token[i];
								String variableName = "";
								if (i < token.length - 1 && flag) {
									if (token[i + 1].trim().endsWith("=")
											|| token[i + 1].trim()
													.endsWith(";")) {
										int j = token[i + 1].trim()
												.indexOf("=");
										if (j == -1) {
											j = token[i + 1].trim()
													.indexOf(";");
										}
										variableName = token[i + 1].trim()
												.substring(0, j);
									} else {
										variableName = token[i + 1].trim();
									}
									paramsMap.put(variableName, dataType);
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
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Inside execute method");
		String fwd =processParseBean(mapping, form, request, response);
		return mapping.findForward(fwd);
		//return super.execute(mapping, form, request, response);
	}
}
