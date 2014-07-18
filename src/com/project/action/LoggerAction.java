package com.project.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.util.StringTokenizer;

import javax.servlet.ServletRequest;
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
		//downloadSingleFile(logform, fname, savePath);
		
		 File srcFolder = new File("c:\\prasanthi");
	        File destFolder = new File("c:\\prasanthi-new");

	        //make sure source exists
	        if(!srcFolder.exists()){

	           System.out.println("Directory does not exist.");
	           //just exit
	           System.exit(0);

	        }else{

	           try{
	            copyFolder(srcFolder,destFolder);
	           }catch(IOException e){
	            e.printStackTrace();
	            //error, just exit
	                System.exit(0);
	           }
	        }

	        System.out.println("Done");
	
	
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
		String from = form.getFromDate();
		String to = form.getToDate();
		int cnt = 0;
		int cnt2 = 0;
		FileWriter fw = new FileWriter(savePath);
		while ((reader.readLine()) != null) {
			String line = reader.readLine();
			if (line.contains(from)) {
				cnt = reader.getLineNumber();
				break;
			}
		}
		while (reader.getLineNumber() >= cnt) {

			String line1 = reader.readLine();
			fw.write(line1);
			if (line1.contains(to)) {
				cnt2 = reader.getLineNumber();
				break;
			}
		}

		fw.close();

	}
	
	    public void copyFolder(File src, File dest)
	        throws IOException{

	        if(src.isDirectory()){
	        	//if directory not exists, create it
	            if(!dest.exists()){
	               dest.mkdir();
	               System.out.println("Directory copied from " 
	                              + src + "  to " + dest);
	            }
	            String data="";
	            //list all the directory contents
	            String files[] = src.list();

	            for (String file : files) {
	            	
		            System.out.println("file"+file);
		            
		            StringTokenizer st = new StringTokenizer(file);
	            	System.out.println("filename"+src);
	            	String fileName=st.nextToken();//Get File name
	        		String date = st.nextToken();//Get date
	        		//String time = st.nextToken();//Get time
	        		if(date.equals(2013-07-27)){
	        			System.out.println("filename"+file);
	        			 //construct the src and dest file structure
	 	               File srcFile = new File(src, file);
	 	               File destFile = new File(dest, file);
	 	               //recursive copy
	 	               copyFolder(srcFile,destFile);
	        		}
	              
	            }

	        }else{
	            //if file, then copy it
	            //Use bytes stream to support all file types
	            InputStream in = new FileInputStream(src);
	                OutputStream out = new FileOutputStream(dest); 

	                byte[] buffer = new byte[1024];

	                int length;
	                //copy the file content in bytes 
	                
	                while ((length = in.read(buffer)) > 0){
	                   out.write(buffer, 0, length);
	                }

	                in.close();
	                out.close();
	                System.out.println("File copied from " + src + " to " + dest);
	        }
	    }
	}

