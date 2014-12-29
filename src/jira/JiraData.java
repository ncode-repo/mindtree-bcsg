package jira;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import util.ConfigManager;
import util.Constant;
import util.DateTimeUtil;

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
	 * @param uri
	 * @param userName
	 * @param password
	 * @return JiraRestClient
	 */
	public JiraRestClient connectToJIRA() {
		try {
			jc = new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(new URI("https://bcsgsvn.atlassian.net/"), "nilesh",
					"njiras");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Exception in: connectToJIRA()");
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
		String jsql_releaseSpec = "project = " + projectName + " AND status WAS " + cm.getProperty(Constant.STATUS_REOPENED) + " AND issueKey IN " + all_reopen_ids;
		try {
			Promise<SearchResult> r = jc.getSearchClient().searchJql(jsql_releaseSpec);
			reopen_count = r.claim().getTotal();
		} catch (Exception e) {
			System.out.println("Exception in: getReopenTicketsCount()" + e.getMessage());
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
		String jsql_releaseSpec = "project = " + projectName + " AND assignee was in (" + assignee_condi + ") AND fixVersion IN(" + releaseVersion
				+ ")";
		return jsql_releaseSpec;
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
		
		String jsql_releaseSpec = "project = " + projectName + type_condi +date_condi;
		System.out.println("jSql: " + jsql_releaseSpec);
		return jsql_releaseSpec;
	}
	
	/**
	 * This method will return release version of project
	 * @param projectName
	 * @return version
	 */
	public String getFixVersion(String projectName){
		String version= null;//Arrays.toString(cm.getProperty(Constant.RELEASE_VERSIONS).contains(projectName);
		
		for(int i=0;cm.getProperty(Constant.RELEASE_VERSIONS).split(",").length>0;i++){
			version= cm.getProperty(Constant.RELEASE_VERSIONS).split(",")[i];
			if(version.contains(projectName)){
				version=version.substring(version.indexOf("_")+1);
				break;
			}
		}
		return version;
	}
	
	/**
	 * This method will return list of release wise data
	 * @param releaseVersion
	 * @return list
	 */
	public List<String> getReleaseWiseData(String releaseVersion) {
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
					// CR count
					totalTicketCount_CR++;
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
							totalDevEstimatedEfforts_CR = totalDevEstimatedEfforts_CR + wl.getMinutesSpent();
							totalDevActualEfforts_CR = totalDevActualEfforts_CR + wl.getMinutesSpent();
						} else if (getJIRA_QA_list().contains(author)) {
//							totalQaEstimatedEfforts_CR = totalQaEstimatedEfforts_CR
//									+ (tt.getOriginalEstimateMinutes() != null ? tt.getOriginalEstimateMinutes() : 0)
//									+ (tt.getRemainingEstimateMinutes() != null ? tt.getRemainingEstimateMinutes() : 0);
//							totalQaActualEfforts_CR = totalQaActualEfforts_CR + (tt.getTimeSpentMinutes() != null ? tt.getTimeSpentMinutes() : 0);
							totalQaEstimatedEfforts_CR = totalQaEstimatedEfforts_CR +  wl.getMinutesSpent();
							totalQaActualEfforts_CR = totalQaActualEfforts_CR +  wl.getMinutesSpent();
						}// //
					}
				} else if (cm.getProperty(Constant.TICKET_TYPE_BUG).contains(issueType)) { //checking for Bugs
					// BUG count
					totalTicketCount_Bug++;
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
							totalDevEstimatedEfforts_Bug = totalDevEstimatedEfforts_Bug +  wl.getMinutesSpent();
							totalDevActualEfforts_Bug = totalDevActualEfforts_Bug +  wl.getMinutesSpent();
						} else if (getJIRA_QA_list().contains(author)) { //checking for particular QA and calculating efforts
//							totalQaEstimatedEfforts_Bug = totalQaEstimatedEfforts_Bug
//									+ (tt.getOriginalEstimateMinutes() != null ? tt.getOriginalEstimateMinutes() : 0)
//									+ (tt.getRemainingEstimateMinutes() != null ? tt.getRemainingEstimateMinutes() : 0);
//							totalQaActualEfforts_Bug = totalQaActualEfforts_Bug + (tt.getTimeSpentMinutes() != null ? tt.getTimeSpentMinutes() : 0);
							totalQaEstimatedEfforts_Bug = totalQaEstimatedEfforts_Bug +  wl.getMinutesSpent();
							totalQaActualEfforts_Bug = totalQaActualEfforts_Bug +  wl.getMinutesSpent();
						}// //
					}
				}
			}
			chksFor_Cr_reopened.deleteCharAt(chksFor_Cr_reopened.length() - 1);
			chksFor_Cr_reopened.append(")");
			chksFor_Bug_reopened.deleteCharAt(chksFor_Bug_reopened.length() - 1);
			chksFor_Bug_reopened.append(")");
			System.out.println();
			//Filling release data list
			devDataList.add(getFixVersion(projectName));
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
			
			
			System.out.println("==============================RESULT=================================");
			System.out.println("Project name= " + projectName);
			System.out.println("Release name= " + getFixVersion(projectName));
			System.out.println("================================CR===================================");
			System.out.println("No. of CR's= " + totalTicketCount_CR);
			System.out.println("No. of reopen CR's= " + (chksFor_Bug_reopened.length()>1?getReopenTicketsCount(chksFor_Bug_reopened):0));
			System.out.println("Total estimated DEV efforts for CR's (SD)= " + totalDevEstimatedEfforts_CR / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			System.out.println("Total actual DEV efforts for CR's (SD)= " + totalDevActualEfforts_CR / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			System.out.println("Total estimated QA efforts for CR's (SD)= " + totalQaEstimatedEfforts_CR / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			System.out.println("Total actual QA efforts for CR's (SD)= " + totalQaActualEfforts_CR / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			System.out.println("=====================================================================");
			System.out.println("===============================Bugs==================================");
			System.out.println("No. of Bug's= " + totalTicketCount_Bug);
			System.out.println("No. of reopen Bug's= " + (chksFor_Bug_reopened.length()>1?getReopenTicketsCount(chksFor_Bug_reopened):0));
			System.out.println("Total estimated DEV efforts for Bug's (SD)= " + totalDevEstimatedEfforts_Bug / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			System.out.println("Total actual DEV efforts for Bug's (SD)= " + totalDevActualEfforts_Bug / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			System.out.println("Total estimated QA efforts for Bug's (SD)= " + totalQaEstimatedEfforts_Bug / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			System.out.println("Total actual QA efforts for Bug's (SD)= " + totalQaActualEfforts_Bug / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			System.out.println("=====================================================================");
		} catch (Exception e) {
			System.out.println("Exception in : getReleaseWiseData() " + e.getMessage());
		}
		return devDataList;
	}

	/**
	 * return no. of open tickets till this month plus no. of tickets raised in
	 * this month
	 * @return
	 */
	private int getOpenTicketsCountForMonthWise(boolean isForBug) {

		int open_count = 0;
		String strStartDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getDashboardMonth_StartDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_START_DATE));
		String strEndDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getPrevMonth_EndDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_END_DATE));
		String type_condi = (isForBug ? " AND issueType=Bug " : " AND issueType!=Bug ");
		String status_condi = " AND status IN ("+cm.getProperty(Constant.STATUS_DEV_OPEN)+")";
		//String assignee_wasin_condi = " (assignee was in (" + (Arrays.toString(cm.getProperty(Constant.DEV_JIRA_IDS) + "," + Arrays.toString(cm.getProperty(Constant.QA_JIRA_IDS)).replaceAll("[\\[\\]]", "") + ")"+" AND timespent > 0)";
		String assignee_wasin_condi = " (assignee was in (" + getJIRA_MT_list() + ")"+" AND timespent > 0)";
		String assignee_in_condi= " assignee in ("+ getJIRA_MT_list() + ")";

		String jsql_openTickets_inlast_month = "project = " + projectName + " AND created < " + strStartDate + status_condi + " AND "+assignee_wasin_condi
				+ type_condi;
//		System.out.println("jSql:  " + jsql_openTickets_inlast_month);
		String jsql_openTickets_cur_month = "project = " + projectName + " AND created >= " + strStartDate + " AND created <= " + strEndDate
				+ type_condi + " AND (" + assignee_in_condi +" OR "+ assignee_wasin_condi +")";
//		System.out.println("jSql: " + jsql_openTickets_cur_month);
		Promise<SearchResult> r =null;
		try {
			r= jc.getSearchClient().searchJql(jsql_openTickets_inlast_month);
			open_count = r.claim().getTotal();
			r = jc.getSearchClient().searchJql(jsql_openTickets_cur_month);
			open_count = open_count + r.claim().getTotal();
		} catch (Exception e) {
			System.out.println("Exception in getOpenTicketsCountForMonthWise(): " + e.getMessage());
		}
		return open_count;
	}

	private int getCloseTicketsCountForMonthWise(boolean isForBug) {

		int close_count = 0;
		String strStartDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getDashboardMonth_StartDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_START_DATE));
		String strEndDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getPrevMonth_EndDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_END_DATE));
		String type_condi = (isForBug ? " AND issueType=Bug " : " AND issueType!=Bug ");
		String status_condi = " AND status WAS IN ("+cm.getProperty(Constant.STATUS_BUILD_UPDATED)+") AND status!="+cm.getProperty(Constant.STATUS_REOPENED);
		String assignee_wasin_condi = " AND assignee was in ("+ getJIRA_DEV_list() +") AND timespent > 0";
//		String assignee_wasin_condi = " AND assignee was in ("+ getJIRA_DEV_list() +")";

		String jsql_openTickets_cur_month = "project = " + projectName + " AND updated >= " + strStartDate + " AND updated <= " + strEndDate
				+ type_condi + assignee_wasin_condi + status_condi;
//		System.out.println("jSql: " + jsql_openTickets_cur_month);
		
		Promise<SearchResult> r =null;
		try {
			r = jc.getSearchClient().searchJql(jsql_openTickets_cur_month);
			close_count = r.claim().getTotal();
		} catch (Exception e) {
			System.out.println("Exception in getCloseTicketsCountForMonthWise(): " + e.getMessage());
		}
		return close_count;
	}

	private int getInvalidTicketsCountForMonthWise() {

		int invalid_count = 0;
		String strStartDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getDashboardMonth_StartDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_START_DATE));
		String strEndDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getPrevMonth_EndDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_END_DATE));
		String status_condi = " AND status="+cm.getProperty(Constant.STATUS_INVALID);
		//String assignee_wasin_condi = " AND assignee was in ("+ Arrays.toString(cm.getProperty(Constant.DEV_JIRA_IDS).replaceAll("[\\[\\]]", "") +") AND timespent > 0";

		String jsql_InvalidTickets_cur_month = "project = " + projectName + " AND updated >= " + strStartDate + " AND updated <= " + strEndDate + status_condi;
//		System.out.println("jSql: " + jsql_InvalidTickets_cur_month);
		
		Promise<SearchResult> r =null;
		try {
			r = jc.getSearchClient().searchJql(jsql_InvalidTickets_cur_month);
			invalid_count = r.claim().getTotal();
		} catch (Exception e) {
			System.out.println("Exception in getInvalidTicketsCountForMonthWise(): " + e.getMessage());
		}
		return invalid_count;
	}
	
	private String getJIRA_QA_list(){
		if(projectName.equalsIgnoreCase("ESD")){
			return cm.getProperty(Constant.ESD_QA_JIRA_IDS).replaceAll("[\\[\\]]", "");
		}else if(projectName.equalsIgnoreCase("SG")){
			return cm.getProperty(Constant.BH_QA_JIRA_IDS).replaceAll("[\\[\\]]", "");
		}
		return "";
	}
	private String getJIRA_DEV_list(){
		if(projectName.equalsIgnoreCase("ESD")){
			return cm.getProperty(Constant.ESD_DEV_JIRA_IDS).replaceAll("[\\[\\]]", "");
		}else if(projectName.equalsIgnoreCase("SG")){
			return cm.getProperty(Constant.BH_DEV_JIRA_IDS).replaceAll("[\\[\\]]", "");
		}
		return "";
	}
	private String getJIRA_MT_list(){
		return getJIRA_DEV_list()+", " +getJIRA_QA_list();
	}
	private Map<String, Double> getTicketTotal_Count_Efforts(String jsql_releaseSpec) {
		Map<String, Double> mapEfforts = new HashMap<String, Double>();
		int totalTicketCount = 0;
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

				// Efforts
				Iterator<Worklog> itrWL = issue.getWorklogs().iterator();
				Worklog wl = null;
				String author = null;
				String issueType = issue.getIssueType().getName();
//				System.out.println("issueType:" + issueType);
				// count
				totalTicketCount++;
				while (itrWL.hasNext()) {
//					System.out.print(".");
					wl = itrWL.next();
					author = wl.getUpdateAuthor().getName();
//					System.out.println("UpdateAuthor:" + author);
					// Efforts calc
					tt = issue.getTimeTracking();
					System.out.println("Author-"+author+"::issue id-"+issue.getKey()+"::Time spent(mins)-"+wl.getMinutesSpent());
					if (getJIRA_DEV_list().contains(author)) {
//						totalDevEstimatedEfforts = totalDevEstimatedEfforts
//								+ (tt.getOriginalEstimateMinutes() != null ? tt.getOriginalEstimateMinutes() : 0)
//								+ (tt.getRemainingEstimateMinutes() != null ? tt.getRemainingEstimateMinutes() : 0);
//						totalDevActualEfforts = totalDevActualEfforts + tt.getTimeSpentMinutes();
						totalDevEstimatedEfforts = totalDevEstimatedEfforts + wl.getMinutesSpent();
						totalDevActualEfforts = totalDevActualEfforts + wl.getMinutesSpent();
					} else if (getJIRA_QA_list().contains(author)) {
//						totalQaEstimatedEfforts = totalQaEstimatedEfforts
//								+ (tt.getOriginalEstimateMinutes() != null ? tt.getOriginalEstimateMinutes() : 0)
//								+ (tt.getRemainingEstimateMinutes() != null ? tt.getRemainingEstimateMinutes() : 0);
//						totalQaActualEfforts = totalQaActualEfforts + (tt.getTimeSpentMinutes() != null ? tt.getTimeSpentMinutes() : 0);
						totalQaEstimatedEfforts = totalQaEstimatedEfforts + wl.getMinutesSpent();
						totalQaActualEfforts = totalQaActualEfforts + wl.getMinutesSpent();
					}// //
				}
			}
			System.out.println(jsql_releaseSpec);
			System.out.println("TOTAL_DEV_ESTIMATED_EFFORTS: "+totalDevEstimatedEfforts);
			System.out.println("TOTAL_QA_ACTUAL_EFFORTS: "+totalQaActualEfforts);
			mapEfforts.put("TOTAL_TICKET_COUNT",Double.valueOf(totalTicketCount));
			mapEfforts.put("TOTAL_DEV_ESTIMATED_EFFORTS", totalDevEstimatedEfforts);
			mapEfforts.put("TOTAL_DEV_ACTUAL_EFFORTS", totalDevActualEfforts);
			mapEfforts.put("TOTAL_QA_ESTIMATED_EFFORTS", totalQaEstimatedEfforts);
			mapEfforts.put("TOTAL_QA_ACTUAL_EFFORTS", totalQaActualEfforts);
		} catch (Exception e) {
			System.out.println("Exception in getTicketTotal_Count_Efforts(): " + e.getMessage());
		}
		return mapEfforts;
	}
	
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
	
	private int getPriorityWiseCount(String priority,String status) {
		int priority_count=0;
		String strStartDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getDashboardMonth_StartDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_START_DATE));
		String strEndDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getPrevMonth_EndDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_END_DATE));
		
		String createdDate_condi= " AND created>="+ strStartDate + " AND created <= " + strEndDate;
		String priority_condi=" AND priority="+priority;
		String status_condi = (status!=null?" AND status in("+status+")":"");
		String assignee_condi =" AND assignee was in (" + getJIRA_MT_list() + ")";
		
		String jsql_FindPriority = "project = " + projectName +createdDate_condi+priority_condi+status_condi+assignee_condi;
		System.out.println("jSql: " + jsql_FindPriority);
		
		Promise<SearchResult> r =null;
		try {
			r = jc.getSearchClient().searchJql(jsql_FindPriority);
			priority_count = r.claim().getTotal();
		} catch (Exception e) {
			System.out.println("Exception in getPriorityWiseCount(): " + e.getMessage());
		}
		return priority_count;
	}
	private Map<String, Integer> getPrioritydataForRaised(){
		Map<String, Integer> map=new HashMap<String,Integer >();
		int count=0;
		count=getPriorityWiseCount("Urgent",null);
		System.out.println("P1-Urgent: "+count);
		map.put("p1",count);
		count=getPriorityWiseCount("High",null);
		System.out.println("P2-High: "+count);
		map.put("p2",count);
		count=getPriorityWiseCount("Medium",null);
		System.out.println("P3-Medium: "+count);
		map.put("p3",count);
		count=getPriorityWiseCount("Low",null);
		System.out.println("Other-Low: "+count);
		map.put("other",count);
		return map;
	}
	
	private Map<String, Integer> getPrioritydataForRaisedForClosed(){
		Map<String,Integer> map=new HashMap<String,Integer>();
		int count=0;
		count=getPriorityWiseCount("Urgent",cm.getProperty(Constant.STATUS_CLOSED));
		System.out.println("P1-Urgent: "+count);
		map.put("p1",count);
		count=getPriorityWiseCount("High",cm.getProperty(Constant.STATUS_CLOSED));
		System.out.println("P2-High: "+count);
		map.put("p2",count);
		count=getPriorityWiseCount("Medium",cm.getProperty(Constant.STATUS_CLOSED));
		System.out.println("P3-Medium: "+count);
		map.put("p3",count);
		count=getPriorityWiseCount("Low",cm.getProperty(Constant.STATUS_CLOSED));
		System.out.println("Other-Low: "+count);
		map.put("other",count);
		return map;
	}
	
	private Map<String, Integer> getPrioritydataForRaisedForReOpened(){
		Map<String, Integer> map=new HashMap<String, Integer>();
		int count=0;
		count=getPriorityWiseCount("Urgent","Reopened");
		System.out.println("P1-Urgent: "+count);
		map.put("p1",count);
		count=getPriorityWiseCount("High","Reopened");
		System.out.println("P2-High: "+count);
		map.put("p2",count);
		count=getPriorityWiseCount("Medium","Reopened");
		System.out.println("P3-Medium: "+count);
		map.put("p3",count);
		count=getPriorityWiseCount("Low","Reopened");
		System.out.println("Other-Low: "+count);
		map.put("other",count);
		return map;
	}
	
	public List<Map<String, Integer>> getPriorityWiseData(){
		
		List<Map<String, Integer>> lstPriorityData=new ArrayList<Map<String, Integer>>();
		lstPriorityData.add(getPrioritydataForRaised());
		lstPriorityData.add(getPrioritydataForRaisedForClosed());
		lstPriorityData.add(getPrioritydataForRaisedForReOpened());
		
		System.out.println("total tickets :: "+ lstPriorityData.get(0));
		System.out.println("closed tickets :: "+ lstPriorityData.get(1));
		System.out.println("reopened tickets :: "+ lstPriorityData.get(2));
		return lstPriorityData;
	}
	public List<String> getMonthWiseData() {
		
		List<String> devDataList= new ArrayList<String>();
		try {
			List<Map> lstDevData=null;
			Map<String, Double> mapEfforts_CR = new HashMap<String, Double>();
			Map<String, Double> mapEfforts_BUG = new HashMap<String, Double>();
			lstDevData=getTicketTotal_Count_Efforts_CR_Bug();
			mapEfforts_CR=lstDevData.get(0);
			mapEfforts_BUG=lstDevData.get(1);

			System.out.println();
			//Filing Month wise data
			devDataList.add(String.valueOf(DateTimeUtil.getDashboard_MonthYear()));
			devDataList.add(String.valueOf(getOpenTicketsCountForMonthWise( false)));
			devDataList.add(String.valueOf(getCloseTicketsCountForMonthWise( true))); 
			devDataList.add(String.valueOf(mapEfforts_CR.get("TOTAL_QA_ACTUAL_EFFORTS")/ Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT))));
			devDataList.add(String.valueOf(mapEfforts_CR.get("TOTAL_DEV_ACTUAL_EFFORTS") / Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT))));
			devDataList.add(String.valueOf(getOpenTicketsCountForMonthWise(true)));
			devDataList.add(String.valueOf(getCloseTicketsCountForMonthWise( true)));
			devDataList.add(String.valueOf(mapEfforts_BUG.get("TOTAL_QA_ACTUAL_EFFORTS")/ Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT))));
			devDataList.add(String.valueOf(mapEfforts_BUG.get("TOTAL_DEV_ACTUAL_EFFORTS")/ Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT))));
			devDataList.add(String.valueOf(getInvalidTicketsCountForMonthWise()));
			
			System.out.println("==============================RESULT=================================");
			System.out.println("Project name= " + projectName);
			System.out.println("Month= " + DateTimeUtil.getDashboard_MonthYear());
			System.out.println("================================CR===================================");
			System.out.println("Open enhancements (Prev open + this month raised)= " + getOpenTicketsCountForMonthWise( false));
			System.out.println("Closed enhancements (Build updated)= " + getCloseTicketsCountForMonthWise( true));
			System.out.println("Total actual DEV efforts for CR's (SD) in current month= " + mapEfforts_CR.get("TOTAL_DEV_ACTUAL_EFFORTS")/ Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			System.out.println("Total actual QA efforts for CR's (SD) in current month= " + mapEfforts_CR.get("TOTAL_QA_ACTUAL_EFFORTS")/ Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			System.out.println("=====================================================================");
			System.out.println("===============================Bugs==================================");
			System.out.println("Open Bugs (Prev open + this month raised)= " + getOpenTicketsCountForMonthWise(true));
			System.out.println("Closed Bugs (Build updated)= " + getCloseTicketsCountForMonthWise( true));
			System.out.println("Total actual DEV efforts for Bug's (SD) in current month= " + mapEfforts_BUG.get("TOTAL_DEV_ACTUAL_EFFORTS")/ Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			System.out.println("Total actual QA efforts for Bug's (SD) in current month= " + mapEfforts_BUG.get("TOTAL_QA_ACTUAL_EFFORTS")/ Integer.parseInt(cm.getProperty(Constant.EFFORTS_UNIT)));
			
			System.out.println("Invalid count = " + getInvalidTicketsCountForMonthWise());
			System.out.println("=====================================================================");
		} catch (Exception e) {
			System.out.println("Exception in getMonthWiseData(): " + e.getMessage());
		}
		return devDataList;
	}

	public static void main(String[] args) throws Exception {
		URI baseUri = UriBuilder.fromUri(new URI("https://bcsgsvn.atlassian.net/")).path("/rest/api/latest").build(new Object[0]);
		System.out.println(baseUri);
	}
}
