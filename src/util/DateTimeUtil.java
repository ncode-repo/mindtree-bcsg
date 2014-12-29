package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
	static ConfigManager cm= ConfigManager.getInstance();
	
	public static String getDashboard_MonthYear() {
		String str_month_year=null;
		String strDate = (cm.getProperty(Constant.TEST_DASHBOARD_START_DATE).equals("") ? DateTimeUtil.getPrevMonth_EndDate_yyyymmdd()
				: cm.getProperty(Constant.TEST_DASHBOARD_END_DATE));
		str_month_year=strDate.substring(0,strDate.lastIndexOf("-"));
		return str_month_year;
	}
	/**
	 * 
	 * @return Prev months min date
	 */
	public static String getDashboardMonth_StartDate_yyyymmdd() {
		Calendar c = Calendar.getInstance(); 
		c.add(Calendar.MONTH, -1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));  
		SimpleDateFormat sdf = new SimpleDateFormat(cm.getProperty(Constant.JIRA_DATE_FORMAT));  
		return sdf.format(c.getTime());
	}
	/**
	 * 
	 * @return Prev months max date
	 */
	public static String getPrevMonth_EndDate_yyyymmdd() {
		Calendar c = Calendar.getInstance();  
		c.add(Calendar.MONTH, -1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));  
		SimpleDateFormat sdf = new SimpleDateFormat(cm.getProperty(Constant.JIRA_DATE_FORMAT));  
		return sdf.format(c.getTime());
	}
	public static String getFormat_ddMMyyyy(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd"+cm.getProperty(Constant.DATE_DELIM)+"MM"+cm.getProperty(Constant.DATE_DELIM)+"yyyy");  
		return sdf.format(date);
	}
//	public String getStr_MonthStartDate_yyyymmdd(Date date) {
//		Calendar c = Calendar.getInstance();
//		c.setTime(date);
//		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));  
//
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  
//		return sdf.format(c.getTime());
//	}
//	public String getStr_MonthEndDate_yyyymmdd(Date date) {
//		Calendar c = Calendar.getInstance();
//		c.setTime(date);
//		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));  
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  
//		return sdf.format(c.getTime());
//	}
//	public static String previousMonthDateString(String dateString) {
//        // Create a date formatter using your format string
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        // Parse the given date string into a Date object.
//        // Note: This can throw a ParseException.
//        Date myDate=null;
//		try {
//			myDate = dateFormat.parse(dateString);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//        // Use the Calendar class to subtract one day
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(myDate);
//        calendar.add(Calendar.MONTH, -2);
//
//        // Use the date formatter to produce a formatted date string
//        Date previousDate = calendar.getTime();
//        String result = dateFormat.format(previousDate);
//
//        return result;
//    }
//	public static String dateBackByMonth(int noOfMonths) {
//        // Create a date formatter using your format string
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        // Parse the given date string into a Date object.
//        // Note: This can throw a ParseException.
//        Date myDate=new Date();
//        // Use the Calendar class to subtract one day
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(myDate);
//        calendar.add(Calendar.MONTH, -noOfMonths);
//
//        // Use the date formatter to produce a formatted date string
//        Date previousDate = calendar.getTime();
//        String result = dateFormat.format(previousDate);
//
//        return result;
//    }
	public static void main(String[] args) {
		String s=new DateTimeUtil().getDashboard_MonthYear();
		System.out.println(s); 
	}
}
