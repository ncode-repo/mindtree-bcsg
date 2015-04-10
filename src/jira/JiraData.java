package jira;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import util.ConfigManager;
import util.Constant;
import util.DateTimeUtil;
import util.Log;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.domain.TimeTracking;
import com.atlassian.jira.rest.client.domain.Worklog;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

public class JiraData {
	
	static ConfigManager cm= ConfigManager.getInstance();
	static DateTimeUtil dateTimeUtil = new DateTimeUtil();
	JiraRestClient jc = null;
	String projectName=null;
	int count = 0;
	
	public void setJc(JiraRestClient jc) {
		this.jc = jc;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	// TODO:
	// 1. use uname pwd from prop file
	// 2. handle exception properly
	
	/**
	 * This method will return JiraRestClient (used to connect with jira)
	 * 
	 * @return JiraRestClient
	 */
	public JiraRestClient connectToJIRA() {
		try {
			// passing uri, username and pwd to get jira client
			jc = new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(new URI(cm.getProperty(Constant.URI)), cm.getProperty(Constant.USER_NAME), cm.getProperty(Constant.PASSWORD));
			/*jc = new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(new URI("https://bcsgsvn.atlassian.net/"), "nilesh",
					"njiras");*/
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.info("Exception in: connectToJIRA()");
		}
		return jc;
	}
	
	/**
	 * This method will return total Reopen tickets count
	 * @param all_reopen_ids
	 * @return reopen_count
	 */
	private int getReopenTicketsCount(StringBuilder all_reopen_ids) {
		int reopen_count = 0;
		String jsql_reopenTickets = "project = " + projectName + " AND status WAS " + cm.getProperty(Constant.STATUS_REOPENED) + " AND issueKey IN " + all_reopen_ids;
		Log.info("jsql_reopenTickets= "+jsql_reopenTickets);
		try {
			Promise<SearchResult> r = jc.getSearchClient().searchJql(jsql_reopenTickets);
			reopen_count = r.claim().getTotal();
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Exception in: getReopenTicketsCount()" + e.getMessage());
		}
		return reopen_count;
	}
	
	/**
	 * This method will return jql for release version
	 * @param releaseVersion
	 * @return jql
	 */
	private String getReleaseWiseJQL(String releaseVersion) {
		String assignee_condi =getJIRA_MT_list();
		String jsql_releaseWise = "project = " + projectName + " AND assignee was in (" + assignee_condi + ") AND fixVersion IN(" + releaseVersion
				+ ")";
		Log.info("jsql_releaseWise= "+jsql_releaseWise);
		return jsql_releaseWise;
	}
	
	/**
	 * This method will return month wise jql
	 * @param boolean (false for cr, true for bug )
	 * @return jql
	 */
	private String getMonthWiseJQL(boolean isForBug) {
		String strStartDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getDashboardMonth_StartDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_START_DATE));
		String strEndDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getPrevMonth_EndDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_END_DATE));
		String date_condi= " AND updated>="+ strStartDate + " AND updated <= " + strEndDate;
		String type_condi = (isForBug ? " AND issueType=Bug " : " AND issueType!=Bug ");
		//String timeSpent_condi = 
		String jsql_monthWise = "project = " + projectName + type_condi +date_condi;
		Log.info("jsql_monthWise: " + jsql_monthWise);
		return jsql_monthWise;
	}
	
	/**
	 * This method will return release version of project
	 * @param projectName
	 * @return version
	 */
	public List<String> getFixVersion(String projectName){
		List<String> lstVersion= new ArrayList<String>();//Arrays.toString(cm.getProperty(Constant.RELEASE_VERSIONS).contains(projectName);
		String str=null;
		for(int i=0;i<cm.getProperty(Constant.RELEASE_VERSIONS).split(",").length;i++){
			str= cm.getProperty(Constant.RELEASE_VERSIONS).split(",")[i];
			if(str.contains(projectName)){
				lstVersion.add(str.substring(str.indexOf("_")+1));
				//break;
			}
		}
//		if(version.contains(",")){
//			version=version.substring(0, version.length()-1);
//		}
		return lstVersion;
	}
	
	/**
	 * This method calls getReleaseWiseData method to get data of all the releases.
	 * @param releaseVersion - List of all the releases.
	 * @return list - List containing data for all the releases.
	 */
	
	public List<List<String>> getReleaseWiseDataForAllReleases (List<String> lstReleaseVersions){
		List<List<String>> lstReleaseData = new ArrayList<List<String>>();

		if(null!=lstReleaseVersions){
			Iterator<String> itr =  lstReleaseVersions.iterator();
			while(itr.hasNext()){
				lstReleaseData.add(getReleaseWiseData(itr.next()));
			}
		}
		return lstReleaseData;	
	}
	
	/**
	 * This method will return list of release wise data
	 * @param releaseVersion - Name of release for which data needs to be extracted
	 * @return list - List of data for a individual release.
	 */
	public List<String> getReleaseWiseData(String releaseVersion) {
		
		Log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ getReleaseWiseData Function Start~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ");
		List<String> devDataList= new ArrayList<String>();
		
		StringBuilder chksFor_Cr_reopened = new StringBuilder("(");
		StringBuilder chksFor_Bug_reopened = new StringBuilder("(");
		int totalTicketCount_CR = 0;
		Double totalDevEstimatedEfforts_CR = 0.0;
		Double totalDevActualEfforts_CR = 0.0;
		Double totalQaEstimatedEfforts_CR = 0.0;
		Double totalQaActualEfforts_CR = 0.0;
		int totalTicketCount_Bug = 0;
		Double totalDevEstimatedEfforts_Bug = 0.0;
		Double totalDevActualEfforts_Bug = 0.0;
		Double totalQaEstimatedEfforts_Bug = 0.0;
		Double totalQaActualEfforts_Bug = 0.0;
		boolean incrTtlTktCntIndicator = false;
		//get jql for release wise
		String jsql_releaseSpec = getReleaseWiseJQL(releaseVersion);

		try {
			Promise<SearchResult> r = jc.getSearchClient().searchJql(jsql_releaseSpec);
			Iterator<BasicIssue> it = r.claim().getIssues().iterator();
			TimeTracking tt = null;
			while (it.hasNext()) {
				System.out.print(".");
				Issue issue = jc.getIssueClient().getIssue(((BasicIssue) it.next()).getKey()).claim();
				// Efforts
				Iterator<Worklog> itrWL = issue.getWorklogs().iterator();
				Worklog wl = null;
				String author = null;
				String issueType = issue.getIssueType().getName();
				// checking for CR 
				if (cm.getProperty(Constant.TICKET_TYPE_CR).contains(issueType)) {
					 incrTtlTktCntIndicator = false;
					// For Reopen count
					chksFor_Cr_reopened = chksFor_Cr_reopened.append(issue.getKey() + ",");
					while (itrWL.hasNext()) {
						System.out.print(".");
						wl = itrWL.next();
						author = wl.getUpdateAuthor().getName();
						// Efforts calc
						tt = issue.getTimeTracking();
						if (getJIRA_DEV_list().contains(author)) {
//							totalDevEstimatedEfforts_CR = totalDevEstimatedEfforts_CR
//									+ (tt.getOriginalEstimateMinutes() != null ? tt.getOriginalEstimateMinutes() : 0)
//									+ (tt.getRemainingEstimateMinutes() != null ? tt.getRemainingEstimateMinutes() : 0);
//							totalDevActualEfforts_CR = totalDevActualEfforts_CR + tt.getTimeSpentMinutes();
							if(wl.getMinutesSpent() >0 ){
								incrTtlTktCntIndicator =true;
								totalDevEstimatedEfforts_CR = totalDevEstimatedEfforts_CR + wl.getMinutesSpent();
								totalDevActualEfforts_CR = totalDevActualEfforts_CR + wl.getMinutesSpent();
							}
						} else if (getJIRA_QA_list().contains(author)) {
//							totalQaEstimatedEfforts_CR = totalQaEstimatedEfforts_CR
//									+ (tt.getOriginalEstimateMinutes() != null ? tt.getOriginalEstimateMinutes() : 0)
//									+ (tt.getRemainingEstimateMinutes() != null ? tt.getRemainingEstimateMinutes() : 0);
//							totalQaActualEfforts_CR = totalQaActualEfforts_CR + (tt.getTimeSpentMinutes() != null ? tt.getTimeSpentMinutes() : 0);
							if(wl.getMinutesSpent() >0 ){
								incrTtlTktCntIndicator =true;
								totalQaEstimatedEfforts_CR = totalQaEstimatedEfforts_CR +  wl.getMinutesSpent();
								totalQaActualEfforts_CR = totalQaActualEfforts_CR +  wl.getMinutesSpent();
							}
						}// //
					}
						if(incrTtlTktCntIndicator){
							// CR count
							totalTicketCount_CR++;
						}
				} else if (cm.getProperty(Constant.TICKET_TYPE_BUG).contains(issueType)) { //checking for Bugs
					incrTtlTktCntIndicator = false;
					// For Reopen count
					chksFor_Bug_reopened = chksFor_Bug_reopened.append(issue.getKey() + ",");
					while (itrWL.hasNext()) {
						System.out.print(".");
						wl = itrWL.next();
						author = wl.getUpdateAuthor().getName();
						// Efforts calc
						tt = issue.getTimeTracking();
						//checking for particular Dev and calculating efforts
						if (getJIRA_DEV_list().contains(author)) {
//							totalDevEstimatedEfforts_Bug = totalDevEstimatedEfforts_Bug
//									+ (tt.getOriginalEstimateMinutes() != null ? tt.getOriginalEstimateMinutes() : 0)
//									+ (tt.getRemainingEstimateMinutes() != null ? tt.getRemainingEstimateMinutes() : 0);
//							totalDevActualEfforts_Bug = totalDevActualEfforts_Bug + tt.getTimeSpentMinutes();
							if(wl.getMinutesSpent() >0){
								incrTtlTktCntIndicator =true;
								totalDevEstimatedEfforts_Bug = totalDevEstimatedEfforts_Bug +  wl.getMinutesSpent();
								totalDevActualEfforts_Bug = totalDevActualEfforts_Bug +  wl.getMinutesSpent();
							}
						} else if (getJIRA_QA_list().contains(author)) { //checking for particular QA and calculating efforts
//							totalQaEstimatedEfforts_Bug = totalQaEstimatedEfforts_Bug
//									+ (tt.getOriginalEstimateMinutes() != null ? tt.getOriginalEstimateMinutes() : 0)
//									+ (tt.getRemainingEstimateMinutes() != null ? tt.getRemainingEstimateMinutes() : 0);
//							totalQaActualEfforts_Bug = totalQaActualEfforts_Bug + (tt.getTimeSpentMinutes() != null ? tt.getTimeSpentMinutes() : 0);
							if(wl.getMinutesSpent() >0){
								incrTtlTktCntIndicator =true;
								totalQaEstimatedEfforts_Bug = totalQaEstimatedEfforts_Bug +  wl.getMinutesSpent();
								totalQaActualEfforts_Bug = totalQaActualEfforts_Bug +  wl.getMinutesSpent();
							}
						}// //
					}
					if(incrTtlTktCntIndicator){
						// BUG count
						totalTicketCount_Bug++;
					}
				}
			}
			chksFor_Cr_reopened.deleteCharAt(chksFor_Cr_reopened.length() - 1);
			chksFor_Cr_reopened.append(")");
			chksFor_Bug_reopened.deleteCharAt(chksFor_Bug_reopened.length() - 1);
			chksFor_Bug_reopened.append(")");
			
			//Filling release data list
			devDataList.add(releaseVersion);
			devDataList.add(String.valueOf(totalTicketCount_CR));
			devDataList.add(String.valueOf((chksFor_Cr_reopened.length()>1?getReopenTicketsCount(chksFor_Cr_reopened):0)));
			devDataList.add(String.valueOf(totalQaEstimatedEfforts_CR / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT))));
			devDataList.add(String.valueOf(totalQaActualEfforts_CR / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT))));
			devDataList.add(String.valueOf(totalDevEstimatedEfforts_CR / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT))));
			devDataList.add(String.valueOf(totalDevActualEfforts_CR / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT))));
			devDataList.add(String.valueOf(totalTicketCount_Bug));
			devDataList.add(String.valueOf((chksFor_Bug_reopened.length()>1?getReopenTicketsCount(chksFor_Bug_reopened):0)));
			devDataList.add(String.valueOf(totalQaEstimatedEfforts_Bug / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT))));
			devDataList.add(String.valueOf(totalQaActualEfforts_Bug / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT))));
			devDataList.add(String.valueOf(totalDevEstimatedEfforts_Bug / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT))));
			devDataList.add(String.valueOf(totalDevActualEfforts_Bug / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT))));
			
			
			Log.info("==============================RESULT=================================");
			Log.info("Project name= " + projectName);
			Log.info("Release name= " + releaseVersion);
			Log.info("================================CR===================================");
			Log.info("No. of CR's= " + totalTicketCount_CR);
			Log.info("No. of reopen CR's= " + (chksFor_Bug_reopened.length()>1?getReopenTicketsCount(chksFor_Bug_reopened):0));
			Log.info("Total estimated DEV efforts for CR's (SD)= " + totalDevEstimatedEfforts_CR / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			Log.info("Total actual DEV efforts for CR's (SD)= " + totalDevActualEfforts_CR / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			Log.info("Total estimated QA efforts for CR's (SD)= " + totalQaEstimatedEfforts_CR / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			Log.info("Total actual QA efforts for CR's (SD)= " + totalQaActualEfforts_CR / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			Log.info("=====================================================================");
			Log.info("===============================Bugs==================================");
			Log.info("No. of Bug's= " + totalTicketCount_Bug);
			Log.info("No. of reopen Bug's= " + (chksFor_Bug_reopened.length()>1?getReopenTicketsCount(chksFor_Bug_reopened):0));
			Log.info("Total estimated DEV efforts for Bug's (SD)= " + totalDevEstimatedEfforts_Bug / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			Log.info("Total actual DEV efforts for Bug's (SD)= " + totalDevActualEfforts_Bug / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			Log.info("Total estimated QA efforts for Bug's (SD)= " + totalQaEstimatedEfforts_Bug / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			Log.info("Total actual QA efforts for Bug's (SD)= " + totalQaActualEfforts_Bug / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			Log.info("=====================================================================");
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Exception in : getReleaseWiseData() " + e.getMessage());
		}
		Log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ getReleaseWiseData Function END~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ");	
		return devDataList;
	}

	/**
	 * return no. of open tickets till this month plus no. of tickets raised in
	 * this month
	 * @param boolean (true for Bug)
	 * @return int
	 */
	private int getOpenTicketsCountForMonthWise(boolean isForBug) {

		int open_count = 0;
		String strStartDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getDashboardMonth_StartDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_START_DATE));
		String strEndDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getPrevMonth_EndDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_END_DATE));
		String type_condi = (isForBug ? " AND issueType=Bug " : " AND issueType!=Bug ");
		String status_condi = " AND status IN ("+cm.getProperty(Constant.STATUS_DEV_OPEN)+")";
		String assignee_wasin_condi = " (assignee was in (" + getJIRA_MT_list() + ")"+" )";
		String assignee_in_condi= " assignee in ("+ getJIRA_MT_list() + ")";
		// jql for open tickets in last month
		String jsql_openTickets_inlast_month = "project = " + projectName + " AND created < " + strStartDate + status_condi + " AND "+assignee_wasin_condi
				+ type_condi;
		// jql for open tickets in current month
		String jsql_openTickets_cur_month = "project = " + projectName + " AND created >= " + strStartDate + " AND created <= " + strEndDate
				+ type_condi + " AND (" + assignee_in_condi +" OR "+ assignee_wasin_condi +")";
		Promise<SearchResult> r =null;
		try {
			r= jc.getSearchClient().searchJql(jsql_openTickets_inlast_month);
			open_count = r.claim().getTotal();
			r = jc.getSearchClient().searchJql(jsql_openTickets_cur_month);
			open_count = open_count + r.claim().getTotal();
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Exception in getOpenTicketsCountForMonthWise(): " + e.getMessage());
		}
		return open_count;
	}

	/**
	 * This method will return closed tickets till this month
	 * @param boolean (true for Bug)
	 * @return int
	 */
	private int getCloseTicketsCountForMonthWise(boolean isForBug) {

		int close_count = 0;
		Boolean isvalid = false;
		String strStartDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getDashboardMonth_StartDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_START_DATE));
		String strEndDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getPrevMonth_EndDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_END_DATE));
		String updated_condi=" AND updated >= " + strStartDate + " AND updated <= " + strEndDate;
		
		String type_condi = (isForBug ? " AND issueType=Bug " : " AND issueType!=Bug ");
		//String status_condi = " AND status WAS IN ("+cm.getProperty(Constant.STATUS_BUILD_UPDATED)+") AND status!="+cm.getProperty(Constant.STATUS_REOPENED);
		String status_condi = " AND status WAS IN ("+cm.getProperty(Constant.STATUS_CLOSED)+")";
		String assignee_wasin_condi = " AND assignee was in ("+ getJIRA_MT_list() +") AND timespent > 0";

		//jql for current month close tickets
		String jsql_closeTickets_cur_month = "project = " + projectName + updated_condi
				+ type_condi + assignee_wasin_condi + status_condi;
		Log.info("jql_closeTickets_cur_month: "+jsql_closeTickets_cur_month);
		Promise<SearchResult> r =null;
		try {
			r = jc.getSearchClient().searchJql(jsql_closeTickets_cur_month);
			Iterable<BasicIssue> issues =  r.claim().getIssues();
			Iterator issueItr = issues.iterator();
			
			while(issueItr.hasNext()){		
				Issue issue = jc.getIssueClient().getIssue(((BasicIssue) issueItr.next()).getKey()).claim();
				Iterator<Worklog> workLogItr = issue.getWorklogs().iterator();
				isvalid = false;
				while(workLogItr.hasNext()){
					isvalid = isCurrentMonthTimeLog(workLogItr.next());
					if(isvalid){
						close_count++;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Exception in getCloseTicketsCountForMonthWise(): " + e.getMessage());
		}
		return close_count;
	}

	/**
	 * This method will return invalid tickets till this month
	 * @return int
	 */
	private int getInvalidTicketsCountForMonthWise() {

		int invalid_count = 0;
		String strStartDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getDashboardMonth_StartDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_START_DATE));
		String strEndDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getPrevMonth_EndDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_END_DATE));
		String status_condi = " AND status="+cm.getProperty(Constant.STATUS_INVALID);
		//jql for current month invalid tickets 
		String jsql_InvalidTickets_cur_month = "project = " + projectName + " AND updated >= " + strStartDate + " AND updated <= " + strEndDate + status_condi;
		
		Promise<SearchResult> r =null;
		try {
			r = jc.getSearchClient().searchJql(jsql_InvalidTickets_cur_month);
			invalid_count = r.claim().getTotal();
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Exception in getInvalidTicketsCountForMonthWise(): " + e.getMessage());
		}
		return invalid_count;
	}
	
	/**
	 * This method will return JIRA QA list
	 * @return String
	 */
	public String getJIRA_QA_list(){
		if(projectName.equalsIgnoreCase("ESD")){
			return cm.getProperty(Constant.ESD_QA_JIRA_IDS).replaceAll("[\\[\\]]", "");
		}else if(projectName.equalsIgnoreCase("SG")|| projectName.equalsIgnoreCase("WESTPAC")|| projectName.equalsIgnoreCase("SBH")){
			return cm.getProperty(Constant.BH_QA_JIRA_IDS).replaceAll("[\\[\\]]", "");
		}
		return "";
	}
	
	/**
	 * This method will return JIRA DEV list
	 * @return String
	 */
	public String getJIRA_DEV_list(){
		if(projectName.equalsIgnoreCase("ESD")){
			return cm.getProperty(Constant.ESD_DEV_JIRA_IDS).replaceAll("[\\[\\]]", "");
		}else if(projectName.equalsIgnoreCase("SG") || projectName.equalsIgnoreCase("WESTPAC") || projectName.equalsIgnoreCase("SBH")){
			return cm.getProperty(Constant.BH_DEV_JIRA_IDS).replaceAll("[\\[\\]]", "");
		}
		return "";
	}
	
	/**
	 * This method will return JIRA MT(DEV+QA) list
	 * @return String
	 */
	public String getJIRA_MT_list(){
		return getJIRA_DEV_list()+", " +getJIRA_QA_list();
	}
	
	/**
	 * Function to check if time is logged in current month.
	 * @param wl
	 * @return
	 */
	public boolean isCurrentMonthTimeLog(Worklog wl){
		boolean isvalidTimeLog= false;
		String strStartDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getDashboardMonth_StartDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_START_DATE));
		String strEndDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getPrevMonth_EndDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_END_DATE));
		
		
		Date strtDt = new java.util.Date();
		strtDt.setYear(Integer.parseInt(strStartDate.substring(0, 4))-1900);
		strtDt.setMonth(Integer.parseInt(strStartDate.substring(5,7))-1);
		strtDt.setDate(Integer.parseInt(strStartDate.substring(8)));
		
		Date endDt = new java.util.Date();
		endDt.setYear(Integer.parseInt(strEndDate.substring(0, 4))-1900);
		endDt.setMonth(Integer.parseInt(strEndDate.substring(5,7))-1);
		endDt.setDate(Integer.parseInt(strEndDate.substring(8)));
		
		Date wlDate = new java.util.Date();
		wlDate.setYear(wl.getStartDate().getYear()-1900);
		wlDate.setMonth(wl.getStartDate().getMonthOfYear()-1);
		wlDate.setDate(wl.getStartDate().getDayOfMonth());

		//Log.info("isCurrentMonthTimeLog : wlDate :" + wlDate );
		
		if(strtDt.before(wlDate)&& endDt.after(wlDate)){
			isvalidTimeLog =true;
		}
			return isvalidTimeLog;
	}
	/**
	 * This method will return map of Release wise total efforts(DEV+QA)
	 * @param jsql_releaseSpec
	 * @return Map
	 */
	private Map<String, Double> getTicketTotal_Count_Efforts(String jsql_releaseSpec) {
		Map<String, Double> mapEfforts = new HashMap<String, Double>();
		int totalTicketCount = 0;
		boolean incrTtlTktCntIndicator = false;
		Double totalDevEstimatedEfforts = 0.0;
		Double totalDevActualEfforts = 0.0;
		Double totalQaEstimatedEfforts = 0.0;
		Double totalQaActualEfforts = 0.0;
		try { 
			Promise<SearchResult> r = jc.getSearchClient().searchJql(jsql_releaseSpec);
			Iterator<BasicIssue> it = r.claim().getIssues().iterator();
			TimeTracking tt = null;
			while (it.hasNext()) {
				Issue issue = jc.getIssueClient().getIssue(((BasicIssue) it.next()).getKey()).claim();
				incrTtlTktCntIndicator = false;
				// Efforts
				Iterator<Worklog> itrWL = issue.getWorklogs().iterator();
				Worklog wl = null;
				String author = null;
				String issueType = issue.getIssueType().getName();
				Date timeLogDate;
				while (itrWL.hasNext()) {
					wl = itrWL.next();
					boolean	isvalid = isCurrentMonthTimeLog(wl);
					if(isvalid){
						author = wl.getUpdateAuthor().getName();
						Log.info("getTicketTotal_Count_Efforts : wl.getStartDate() " +wl.getStartDate());
						// Efforts calc
						tt = issue.getTimeTracking();
						Log.info("Author-"+author+"::issue id-"+issue.getKey()+"::Time spent(mins)-"+wl.getMinutesSpent());
						if (getJIRA_DEV_list().contains(author)) {
							//						totalDevEstimatedEfforts = totalDevEstimatedEfforts
							//								+ (tt.getOriginalEstimateMinutes() != null ? tt.getOriginalEstimateMinutes() : 0)
							//								+ (tt.getRemainingEstimateMinutes() != null ? tt.getRemainingEstimateMinutes() : 0);
							//						totalDevActualEfforts = totalDevActualEfforts + tt.getTimeSpentMinutes();
							if(wl.getMinutesSpent() >0){
								incrTtlTktCntIndicator =true;
								totalDevEstimatedEfforts = totalDevEstimatedEfforts + wl.getMinutesSpent();
								totalDevActualEfforts = totalDevActualEfforts + wl.getMinutesSpent();
							}
						} else if (getJIRA_QA_list().contains(author)) {
							//						totalQaEstimatedEfforts = totalQaEstimatedEfforts
							//								+ (tt.getOriginalEstimateMinutes() != null ? tt.getOriginalEstimateMinutes() : 0)
							//								+ (tt.getRemainingEstimateMinutes() != null ? tt.getRemainingEstimateMinutes() : 0);
							//						totalQaActualEfforts = totalQaActualEfforts + (tt.getTimeSpentMinutes() != null ? tt.getTimeSpentMinutes() : 0);
							if(wl.getMinutesSpent() >0){
								incrTtlTktCntIndicator =true;
								totalQaEstimatedEfforts = totalQaEstimatedEfforts + wl.getMinutesSpent();
								totalQaActualEfforts = totalQaActualEfforts + wl.getMinutesSpent();
							}
						}// //
					}
				}
				if(incrTtlTktCntIndicator){
					// count
					totalTicketCount++;
				}
			}
			Log.info(jsql_releaseSpec);
			Log.info("TOTAL_DEV_ESTIMATED_EFFORTS: "+totalDevEstimatedEfforts);
			Log.info("TOTAL_QA_ACTUAL_EFFORTS: "+totalQaActualEfforts);
			mapEfforts.put("TOTAL_TICKET_COUNT",Double.valueOf(totalTicketCount));
			mapEfforts.put("TOTAL_DEV_ESTIMATED_EFFORTS", totalDevEstimatedEfforts);
			mapEfforts.put("TOTAL_DEV_ACTUAL_EFFORTS", totalDevActualEfforts);
			mapEfforts.put("TOTAL_QA_ESTIMATED_EFFORTS", totalQaEstimatedEfforts);
			mapEfforts.put("TOTAL_QA_ACTUAL_EFFORTS", totalQaActualEfforts);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Exception in getTicketTotal_Count_Efforts(): " + e.getMessage());
		}
		return mapEfforts;
	}
	
	/**
	 * This method will return list of map data for Month wise efforts
	 * @return List<Map> lstEffortsData
	 */
	private List<Map> getTicketTotal_Count_Efforts_CR_Bug(){
		List<Map> lstEffortsData=new ArrayList<Map>();
		String jsql_releaseSpec =null;
		//create jql for cr
		jsql_releaseSpec = getMonthWiseJQL(false);
		lstEffortsData.add(getTicketTotal_Count_Efforts(jsql_releaseSpec));
		//create jql for bugs
		jsql_releaseSpec = getMonthWiseJQL( true);
		lstEffortsData.add(getTicketTotal_Count_Efforts(jsql_releaseSpec));
		
		return lstEffortsData;
	}
	
	/**
	 * This method will return Priority wise count
	 * @param priority
	 * @param status
	 * @return int
	 */
	private int getPriorityWiseCount(String priority,String status) {
		int priority_count=0;
		Boolean isvalid = false;
		String strStartDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getDashboardMonth_StartDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_START_DATE));
		String strEndDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getPrevMonth_EndDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_END_DATE));
		
		String createdDate_condi_param=null;
		if(status==null){
			createdDate_condi_param="created";
		}else{
			createdDate_condi_param="updated";
		}
		String issueTypeCondition = " AND issueType=Bug ";
		String date_condi= " AND "+createdDate_condi_param+">="+ strStartDate + " AND "+createdDate_condi_param+"<= " + strEndDate;
		String priority_condi=" AND priority="+priority;
		String status_condi = (status!=null?" AND status was in("+status+")":"");
		//String assignee_condi =" AND assignee was in (" + getJIRA_MT_list() + ")";
		String assignee_wasin_condi = " AND assignee was in ("+ getJIRA_MT_list() +")";
		//jql to check priority
		String jsql_FindPriority = "project = " + projectName +date_condi+priority_condi+status_condi+assignee_wasin_condi + issueTypeCondition;
		Log.info("jsql_FindPriority: " + jsql_FindPriority);
		
		Promise<SearchResult> r =null;
		try {
			r = jc.getSearchClient().searchJql(jsql_FindPriority);
			Iterable<BasicIssue> issues =  r.claim().getIssues();
			//Condition to count only those tickets on which we have spent time in current month.
			if(createdDate_condi_param.equalsIgnoreCase("updated")){
				Iterator issueItr = issues.iterator();
				while(issueItr.hasNext()){
					
					Issue issue = jc.getIssueClient().getIssue(((BasicIssue) issueItr.next()).getKey()).claim();
					Iterator<Worklog> workLogItr = issue.getWorklogs().iterator();
					isvalid = false;
					while(workLogItr.hasNext()){
						isvalid = isCurrentMonthTimeLog(workLogItr.next());
						if(isvalid){
							priority_count++;
							break;
						}
					}
				}
			}else{
				priority_count = r.claim().getTotal();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Exception in getPriorityWiseCount(): " + e.getMessage());
		}
		return priority_count;
	}
	
	/**
	 * This method will return Map of Month wise Raised tickets 
	 * @return Map
	 */
	private Map<String, Integer> getPrioritydataForRaised(){
		Map<String, Integer> map=new HashMap<String,Integer >();
		int count=0;
		count=getPriorityWiseCount("Urgent",null);
		Log.info("P1-Urgent: "+count);
		map.put("p1",count);
		count=getPriorityWiseCount("High",null);
		Log.info("P2-High: "+count);
		map.put("p2",count);
		count=getPriorityWiseCount("Medium",null);
		Log.info("P3-Medium: "+count);
		map.put("p3",count);
		count=getPriorityWiseCount("Low",null);
		Log.info("Other-Low: "+count);
		map.put("other",count);
		return map;
	}
	
	/**
	 * This method will return Map of Month wise closed tickets 
	 * @return Map
	 */
	private Map<String, Integer> getPrioritydataForRaisedForClosed(){
		Map<String,Integer> map=new HashMap<String,Integer>();
		int count=0;
		count=getPriorityWiseCount("Urgent",cm.getProperty(Constant.STATUS_CLOSED));
		Log.info("P1-Urgent: "+count);
		map.put("p1",count);
		count=getPriorityWiseCount("High",cm.getProperty(Constant.STATUS_CLOSED));
		Log.info("P2-High: "+count);
		map.put("p2",count);
		count=getPriorityWiseCount("Medium",cm.getProperty(Constant.STATUS_CLOSED));
		Log.info("P3-Medium: "+count);
		map.put("p3",count);
		count=getPriorityWiseCount("Low",cm.getProperty(Constant.STATUS_CLOSED));
		Log.info("Other-Low: "+count);
		map.put("other",count);
		return map;
	}
	
	/**
	 * This method will return Map of Month wise Reopened tickets 
	 * @return Map
	 */
	private Map<String, Integer> getPrioritydataForRaisedForReOpened(){
		Map<String, Integer> map=new HashMap<String, Integer>();
		int count=0;
		count=getPriorityWiseCount("Urgent","Reopened");
		Log.info("P1-Urgent: "+count);
		map.put("p1",count);
		count=getPriorityWiseCount("High","Reopened");
		Log.info("P2-High: "+count);
		map.put("p2",count);
		count=getPriorityWiseCount("Medium","Reopened");
		Log.info("P3-Medium: "+count);
		map.put("p3",count);
		count=getPriorityWiseCount("Low","Reopened");
		Log.info("Other-Low: "+count);
		map.put("other",count);
		return map;
	}
	
	/**
	 * This method will return list of Map data for priority wise Month data 
	 * @return List<Map> lstPriorityData
	 */
	public List<Map<String, Integer>> getPriorityWiseData(){
		
		List<Map<String, Integer>> lstPriorityData=new ArrayList<Map<String, Integer>>();
		lstPriorityData.add(getPrioritydataForRaised());
		lstPriorityData.add(getPrioritydataForRaisedForClosed());
		lstPriorityData.add(getPrioritydataForRaisedForReOpened());
		
		Log.info("total tickets :: "+ lstPriorityData.get(0));
		Log.info("closed tickets :: "+ lstPriorityData.get(1));
		Log.info("reopened tickets :: "+ lstPriorityData.get(2));
		return lstPriorityData;
	}
	
	/**
	 * This method will return list of Month wise data 
	 * @return List list
	 */
	public List<String> getMonthWiseData() {
		
		List<String> devDataList= new ArrayList<String>();
		try {
			List<Map> lstDevData=null;
			Map<String, Double> mapEfforts_CR = new HashMap<String, Double>();
			Map<String, Double> mapEfforts_BUG = new HashMap<String, Double>();
			lstDevData=getTicketTotal_Count_Efforts_CR_Bug();
			mapEfforts_CR=lstDevData.get(0);
			mapEfforts_BUG=lstDevData.get(1);
	
			//Filing Month wise data
			devDataList.add(String.valueOf(DateTimeUtil.getDashboard_MonthYear()));
			devDataList.add(String.valueOf(getOpenTicketsCountForMonthWise( false)));
			devDataList.add(String.valueOf(getCloseTicketsCountForMonthWise( false))); 
			devDataList.add(String.valueOf(mapEfforts_CR.get("TOTAL_QA_ACTUAL_EFFORTS")/ Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT))));
			devDataList.add(String.valueOf(mapEfforts_CR.get("TOTAL_DEV_ACTUAL_EFFORTS") / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT))));
			devDataList.add(String.valueOf(getOpenTicketsCountForMonthWise(true)));
			devDataList.add(String.valueOf(getCloseTicketsCountForMonthWise( true)));
			devDataList.add(String.valueOf(mapEfforts_BUG.get("TOTAL_QA_ACTUAL_EFFORTS")/ Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT))));
			devDataList.add(String.valueOf(mapEfforts_BUG.get("TOTAL_DEV_ACTUAL_EFFORTS")/ Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT))));
			devDataList.add(String.valueOf(getInvalidTicketsCountForMonthWise()));
			
			Log.info("==============================RESULT=================================");
			Log.info("Project name= " + projectName);
			Log.info("Month= " + DateTimeUtil.getDashboard_MonthYear());
			Log.info("================================CR===================================");
			Log.info("Open enhancements (Prev open + this month raised)= " + getOpenTicketsCountForMonthWise( false));
			Log.info("Closed enhancements (Build updated)= " + getCloseTicketsCountForMonthWise( false));
			Log.info("Total actual DEV efforts for CR's (SD) in current month= " + mapEfforts_CR.get("TOTAL_DEV_ACTUAL_EFFORTS")/ Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			Log.info("Total actual QA efforts for CR's (SD) in current month= " + mapEfforts_CR.get("TOTAL_QA_ACTUAL_EFFORTS")/ Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			Log.info("=====================================================================");
			Log.info("===============================Bugs==================================");
			Log.info("Open Bugs (Prev open + this month raised)= " + getOpenTicketsCountForMonthWise(true));
			Log.info("Closed Bugs (Build updated)= " + getCloseTicketsCountForMonthWise( true));
			Log.info("Total actual DEV efforts for Bug's (SD) in current month= " + mapEfforts_BUG.get("TOTAL_DEV_ACTUAL_EFFORTS")/ Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			Log.info("Total actual QA efforts for Bug's (SD) in current month= " + mapEfforts_BUG.get("TOTAL_QA_ACTUAL_EFFORTS")/ Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			
			Log.info("Invalid count = " + getInvalidTicketsCountForMonthWise());
			Log.info("=====================================================================");
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Exception in getMonthWiseData(): " + e.getMessage());
		}
		return devDataList;
	}

	public static void main(String[] args) throws Exception {
		new JiraData().getFixVersion("SG");
		/*URI baseUri = UriBuilder.fromUri(new URI("https://bcsgsvn.atlassian.net/")).path("/rest/api/latest").build(new Object[0]);
		Log.info(baseUri);*/
	}
}
