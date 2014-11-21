import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import schedule.SendDashboardJob;

public class Main {
	public static void main(String[] args) {

		try {
			// specify the job' s details..
			JobDetail job = JobBuilder.newJob(SendDashboardJob.class).withIdentity("BCSG_Dashboard_SendEmail_CronJob").build();

			// specify the running period of the job
			//Trigger trigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.simpleSchedule().build().setStartTime(arg0)repeatMinutelyForever()).build();//simpleSchedule().withIntervalInSeconds(5).withIntervalInHours(5)).build();
			Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.monthlyOnDayAndHourAndMinute(4, 15, 04)).build();

			// schedule the job
			SchedulerFactory schFactory = new StdSchedulerFactory();
			Scheduler sch = schFactory.getScheduler();
			sch.start();
			sch.scheduleJob(job, trigger);

		} catch (SchedulerException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
