package pmr.dasboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jira.JiraData;
import ole.DashboardExcel;
import pmr.beans.DevDataBean;
import util.ConfigManager;
import util.Constant;
import util.DateTimeUtil;

import com.atlassian.jira.rest.client.JiraRestClient;
import comm.Mailing;

public class PmrDashboardOps {
	static ConfigManager cm = ConfigManager.getInstance();

	/*
	 * 1 Connect to JIRA 2 create dev dashboard 3 create xls 4 send mail
	 */
	public PmrDashboardOps() {
		// new JiraData().connectToJIRA();
	}

	public void createDevDashboard() {
		// 1 connect to jira
		JiraData jd = new JiraData();
		System.out.println("Connecting to JIRA...");
		JiraRestClient jc = jd.connectToJIRA();
		try {
			List<String> prjList = Arrays.asList(cm.getProperty(Constant.PROJECT_NAMES).split(","));

			for (String prj : prjList) {
				// List<DevDataBean> lstDevDataBean = new
				// ArrayList<DevDataBean>();
				// 2 retrive data from jira
				// // for cr's
				System.out.println("Retriving data from JIRA for..." + prj);
				// DevDataBean devDataBeanForCR = jd.getReleaseWiseData(jc, prj,
				// Constant.RELEASE_VERSION);
				jd.setJc(jc);
				jd.setProjectName(prj);
				List<List<String>> lstData = new ArrayList<List<String>>();
				lstData.add(jd.getReleaseWiseData(jd.getFixVersion(prj)));
				lstData.add(jd.getMonthWiseData());
				// // for bugs
				// System.out.println("...for..." + Constant.TICKET_TYPE_BUG);
				// DevDataBean devDataBeanForBug = jd.getReleaseWiseData(jc,
				// prj, Constant.TICKET_TYPE_BUG);

//				String fileName = createFileName(prj);
//				// 3 create xls and fill data in it
//				System.out.println("Writing dashboard excel...");
//				// lstDevDataBean.add(devDataBeanForCR);
//				// lstDevDataBean.add(devDataBeanForBug);
//				// new ExportToXls().writeDataInExcel(fileName, lstDevDataBean);
//				new DashboardExcel().writeDataInExcel(fileName, lstData);
//				// 4 send mail
//				System.out.println("Sending mail...");
//				new Mailing().sendMail(fileName, prj);
			}
		} catch (Exception e) {

		} finally {
			jd = null;
			jc = null;
		}
	}

	public String createFileName(String prj) {
		String fileName = cm.getProperty(Constant.CLIENT_NAME) + cm.getProperty(Constant.FILENAME_DELIM) + prj
				+ cm.getProperty(Constant.FILENAME_DELIM) + cm.getProperty(Constant.DEV_DASHBOARD) + cm.getProperty(Constant.FILENAME_DELIM);
		fileName = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? fileName + DateTimeUtil.getDashboard_MonthYear() : fileName
				+ cm.getProperty(Constant.TEST_DASHBOARD_START_DATE) + cm.getProperty(Constant.FILENAME_DELIM) + "_"
				+ cm.getProperty(Constant.TEST_DASHBOARD_END_DATE));
		return fileName + cm.getProperty(Constant.FILE_EXTENTION);
	}

	public static void main(String[] args) {
		PmrDashboardOps pmrDashboardOps = new PmrDashboardOps();
		// System.out.println(pmrDashboardOps.createFileName("MBW"));
		pmrDashboardOps.createDevDashboard();
	}
}
