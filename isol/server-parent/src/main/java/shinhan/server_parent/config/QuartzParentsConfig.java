package shinhan.server_parent.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shinhan.server_parent.job.ParentsJob;

@Configuration
public class QuartzParentsConfig {

    @Bean
    public JobDetail myJobDetail() {
        return JobBuilder.newJob(ParentsJob.class)
            .withIdentity("myJob")
            .storeDurably()
            .build();
    }

    @Bean
    public Trigger myJobTrigger() {
        return TriggerBuilder.newTrigger()
            .forJob(myJobDetail())
            .withIdentity("myJobTrigger")
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0/5 * * * ?")) // 매 5분마다 실행
            .build();
    }
}
