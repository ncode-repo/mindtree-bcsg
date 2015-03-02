package util;

public class Constant {
	public static String CLIENT_NAME = "CLIENT_NAME";
	public static String FILENAME_DELIM = "FILENAME_DELIM";
	public static String FILE_PATH = "FILE_PATH";
	public static String DEV_DASHBOARD = "DEV_DASHBOARD";
	public static String TEST_DASHBOARD_NAME = "TEST_DASHBOARD_NAME";
	
//	//JIRA
	public static String TICKET_TYPE_CR = "TICKET_TYPE_CR";
	public static String TICKET_TYPE_BUG = "TICKET_TYPE_BUG";
//	public static String PROJECT_NAMES = "PROJECT_NAMES";
	public static String PROJECT_NAMES =  "PROJECT_NAMES" ;
	public static String RELEASE_VERSIONS = "RELEASE_VERSIONS";
//	public static String ESD_RELEASE_VERSION = "ESD_RELEASE_VERSION";
//	public static String SG_RELEASE_VERSION = "SG_RELEASE_VERSION";
	
	public static String STATUS_REOPENED= "STATUS_REOPENED";
	public static String STATUS_DEV_OPEN= "STATUS_DEV_OPEN";
	public static String STATUS_CLOSED= "STATUS_CLOSED";
	public static String STATUS_BUILD_UPDATED= "STATUS_BUILD_UPDATED";
	public static String STATUS_INVALID= "STATUS_INVALID";
	
	//Effort unit is hrs, and getting data in mins. 60: to get time in hrs and 8: to get it in staff days
	public static String EFFORTS_UNIT="EFFORTS_UNIT";
	public static String BH_QA_JIRA_IDS="BH_QA_JIRA_IDS";
	public static String ESD_QA_JIRA_IDS="ESD_QA_JIRA_IDS";
	
	public static String BH_DEV_JIRA_IDS="BH_DEV_JIRA_IDS";
	public static String ESD_DEV_JIRA_IDS="ESD_DEV_JIRA_IDS";
	
	// Excel file ralated
	public static String FILE_EXTENTION = "FILE_EXTENTION";
	public static String HEADER_RELEASE ="HEADER_RELEASE";
	public static String HEADER_MONTH ="HEADER_MONTH";
	public static String HEADER_RELEASE_DATA = "HEADER_RELEASE_DATA";
	public static String HEADER_MONTHLY_DATA ="HEADER_MONTHLY_DATA";
	public static String HEADER_PRIORITY ="HEADER_PRIORITY";
	public static String HEADER_PRIORITY_DATA ="HEADER_PRIORITY_DATA";

	// This is for testing only,
	// default val="", for testing provide date in format- yyyy-mm-dd
//	public static String TEST_DASHBOARD_START_DATE = "2014-08-01";
//	public static String TEST_DASHBOARD_END_DATE = "2014-08-30";
	public static String TEST_DASHBOARD_START_DATE = "TEST_DASHBOARD_START_DATE";
	public static String TEST_DASHBOARD_END_DATE = "TEST_DASHBOARD_END_DATE";
//
//	// JIRA related constants
	public static String JIRA_DATE_FORMAT = "JIRA_DATE_FORMAT";
	public static String DATE_DELIM = "DATE_DELIM";
//
//	// Mail details
	public static String SMTP_HOST= "SMTP_HOST";
	public static String SMTP_PORT = "SMTP_PORT";
	public static String MAIL_SUBJECT = "MAIL_SUBJECT";
	public static String MAIL_TEXT = "MAIL_TEXT";
	public static String MAIL_FROM="MAIL_FROM";
	//public static String[] MAIL_TO_LIST={"Mayuresh_Tendulkar@mindtree.com","Rohan_Darekar@mindtree.com"};
	public static String MAIL_TO_LIST="MAIL_TO_LIST";
	//public static String MAIL_TO_LIST="Deepti_Kaushik@mindtree.com";
	//public static String[] MAIL_CC_LIST={"Pramod_Chavan@mindtree.com","Vinamra_Dhoot@mindtree.com","nilesh_shinde@mindtree.com","Prashant_Mishra2@mindtree.com"};
	public static String MAIL_CC_LIST="MAIL_CC_LIST";
	public static String ESD_LEAD_MAILID="ESD_LEAD_MAILID";
	public static String BH_LEAD_MAILID="BH_LEAD_MAILID";
	public static String PM_MAILID="PM_MAILID";
	//Scheduler Realted
	public static String DASHBOARD_CRON_JOB_EXPRESSION="DASHBOARD_CRON_JOB_EXPRESSION";
	//priority
	public final static String URI="URI";
	public final static String USER_NAME="USER_NAME";
	public final static String PASSWORD="PASSWORD";
	
}
