package shinhan.server_parent.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class ParentsJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 작업 내용
        System.out.println("Job executed!");
    }
}
