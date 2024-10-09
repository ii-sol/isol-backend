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
    public JobDetail parentsJobDetail() {
        return JobBuilder.newJob(ParentsJob.class)
            .withIdentity("parentsJob")
            .storeDurably()
            .build();
    }

    @Bean
    public Trigger parentsJobTrigger() {
        return TriggerBuilder.newTrigger()
            .forJob(parentsJobDetail())
            .withIdentity("parentsJobTrigger")
            .withSchedule(CronScheduleBuilder.cronSchedule("0/30 * * * * ?")) // 30초마다 실행
            .build();
    }
}
