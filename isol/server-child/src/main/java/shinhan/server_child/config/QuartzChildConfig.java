package shinhan.server_child.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shinhan.server_child.job.ChildJob;

@Configuration
public class QuartzChildConfig {

    @Bean
    public JobDetail myJobDetail() {
        return JobBuilder.newJob(ChildJob.class)
            .withIdentity("childJob")
            .storeDurably()
            .build();
    }

    @Bean
    public Trigger myJobTrigger() {
        return TriggerBuilder.newTrigger()
            .forJob(myJobDetail())
            .withIdentity("childJobTrigger")
            .withSchedule(CronScheduleBuilder.cronSchedule("0/30 * * * * ?")) // 30초마다 실행
            .build();
    }
}
