package schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import pmr.dasboard.PmrDashboardOps;


public class SendDashboardJob implements Job {
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		new PmrDashboardOps().createDevDashboard();
		System.out.println("Running...");
	}
//	public void initDashboardCronJob(Scheduler scheduler) {
//    	try {
//        	JobDetail jobDetail = new JobDetail();
//    		jobDetail.setName("BCSG_Dashboard_SendEmail_CronJob");
//    		jobDetail.setGroup("mindtree-bcsg-JobGroup");
//    		jobDetail.setJobClass(SendDashboardJob.class);
//    		
//    		String dashboardCronJobExpression = Constant.DASHBOARD_CRON_JOB_EXPRESSION;
//    		CronTrigger trigger = new CronTrigger("DashboardDataSendCronJob", "group1", dashboardCronJobExpression);
//    		scheduler.scheduleJob(jobDetail, trigger);
//
//        } catch (Exception e) {
	//		e.printStackTrace();
//        }
//	}
}
