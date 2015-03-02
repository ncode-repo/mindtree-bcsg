package pmr.dasboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import jira.JiraData;
import ole.DashboardExcel;
import util.ConfigManager;
import util.Constant;
import util.DateTimeUtil;
import util.Log;

import com.atlassian.jira.rest.client.JiraRestClient;
import comm.Mailing;

public class PmrDashboardOps {
	static ConfigManager cm = ConfigManager.getInstance();
	final static Logger logger = Logger.getLogger(PmrDashboardOps.class.getName());
	/*
	 * 1 Connect to JIRA 2 create dev dashboard 3 create xls 4 send mail
	 */
	public PmrDashboardOps() {
		// new JiraData().connectToJIRA();
	}
	
	/**
	 * This method is responsible to get jira client for data, prepare excel sheet and to send mail 
	 * 
	 */
	public void createDevDashboard() {
		// 1 connect to jira
		JiraData jd = new JiraData();
		
		Log.info("Connecting to JIRA...");
		JiraRestClient jc = jd.connectToJIRA();
		try {
			List<String> prjList = Arrays.asList(cm.getProperty(Constant.PROJECT_NAMES).split(","));

			for (String prj : prjList) {
				// 2 retrive data from jira
				Log.info("Retriving data from JIRA for..." + prj);
				jd.setJc(jc);
				jd.setProjectName(prj);
				List<List<?>> lstData = new ArrayList<List<?>>();
				lstData.add(jd.getReleaseWiseDataForAllReleases(jd.getFixVersion(prj))); 
				lstData.add(jd.getMonthWiseData());
				lstData.add(jd.getPriorityWiseData());
				
				// 3 create xls and fill data in it
				Log.info("Writing dashboard excel...");
				String fileName = createFileName(prj);
				Log.info("File name =>"+fileName);
				if(lstData.size()>0){
					new DashboardExcel().writeDataInExcel(fileName, lstData);
				}

				// 4 send mail
				Log.info("Sending mail...");
				new Mailing().sendMail(fileName, prj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jd = null;
			jc = null;
		}
	}

	/**
	 * This method will return excel file name
	 * @param prject name
	 * @return file name
	 * 
	 */
	public String createFileName(String prj) {
		//FILE_PATH
		String fileName = cm.getProperty(Constant.FILE_PATH) + cm.getProperty(Constant.CLIENT_NAME) + cm.getProperty(Constant.FILENAME_DELIM) + prj
				+ cm.getProperty(Constant.FILENAME_DELIM) + cm.getProperty(Constant.DEV_DASHBOARD) + cm.getProperty(Constant.FILENAME_DELIM);
		fileName = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? fileName + DateTimeUtil.getDashboard_MonthYear() : fileName
				+ cm.getProperty(Constant.TEST_DASHBOARD_START_DATE) + cm.getProperty(Constant.FILENAME_DELIM) + "_"
				+ cm.getProperty(Constant.TEST_DASHBOARD_END_DATE));
		return fileName + cm.getProperty(Constant.FILE_EXTENTION);
	}

	public static void main(String[] args) {
		
		Log.info("Test Logging******************************************");
		
		PmrDashboardOps pmrDashboardOps = new PmrDashboardOps();
		pmrDashboardOps.createDevDashboard();
	}
}
