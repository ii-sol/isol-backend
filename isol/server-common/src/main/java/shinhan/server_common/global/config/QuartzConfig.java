package shinhan.server_common.global.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import shinhan.server_common.job.MyJob;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail myJobDetail() {
        return JobBuilder.newJob(MyJob.class)
            .withIdentity("myJob")
            .storeDurably()
            .build();
    }

    @Bean
    public Trigger myJobTrigger() {
        return TriggerBuilder.newTrigger()
            .forJob(myJobDetail())
            .withIdentity("myJobTrigger")
            .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?")) // 10초마다 실행
            .build();
    }

    @Bean
    public Scheduler scheduler() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        // JobFactory 설정
        scheduler.setJobFactory(springBeanJobFactory());

        // Job과 Trigger를 스케줄러에 등록
        scheduler.scheduleJob(myJobDetail(), myJobTrigger());

        // 스케줄러 시작
        scheduler.start();

        return scheduler;
    }


    @Bean
    public                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         SpringBeanJobFactory springBeanJobFactory() {
        return new SpringBeanJobFactory();
    }
}