package shinhan.server_common.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shinhan.server_common.domain.mission.repository.MissionRepository;

@Component
public class MyJob implements Job {

    @Autowired
    private MissionRepository missionRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        missionRepository.updateMissionExpiration();
        System.out.println("Mission expiration updated!");

        System.out.println("MyJob executed!");
    }
}
