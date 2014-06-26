package com.project.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.project.form.LoggerForm;

public class LoggerAction extends Action {
	private static final int BUFFER_SIZE = 4096;

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoggerForm logform = (LoggerForm) form;
		String rpath = request.getServletContext().getRealPath("/");
		String fpath = rpath + "input";// path of the source file
		String fname = fpath + File.separator + "log.txt"; // source file name
		String savePath = rpath + "output";// path to destination file
		downloadSingleFile(logform, fname, savePath);
		ActionForward forward = mapping.findForward("/success");
		return forward;
	}

	/*
	 * method to read the from source and write into destination file
	 */
	public void downloadSingleFile(LoggerForm form, String fname, String path)
			throws IOException {
		//name of destination file
		String savePath = path + File.separator + "genLog.txt";
		LineNumberReader reader = null;
		try {
			reader = new LineNumberReader(new FileReader(fname));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String from = form.getFromString();
		String to = form.getToString();
		int cnt = 0;
		int cnt2 = 0;
		FileWriter fw = new FileWriter(savePath);
		//String lineRead = "";
		while ((reader.readLine()) != null) {
			String line = reader.readLine();
			if (line.contains(from)) {
				cnt = reader.getLineNumber();
				break;
			}
		}
		while (reader.getLineNumber() >= cnt && cnt != cnt2) {

			String line1 = reader.readLine();
			fw.write(line1);
			if (line1.contains(to)) {
				cnt2 = reader.getLineNumber();
				cnt = cnt2;
				break;
			}
		}

		fw.close();

	}
}
