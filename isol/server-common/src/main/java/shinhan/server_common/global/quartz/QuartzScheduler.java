package shinhan.server_common.global.quartz;

import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;
import shinhan.server_common.global.quartz.allowance.MonthlyAllowanceJobDetailService;
import shinhan.server_common.global.quartz.allowance.MonthlyAllowanceTriggerService;
import shinhan.server_common.global.quartz.allowance.dto.MonthlyAllowanceSaveOneRequest;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class QuartzScheduler {

    private final MonthlyAllowanceJobDetailService monthlyAllowanceJobDetailService;
    private final MonthlyAllowanceTriggerService monthlyAllowanceTriggerService;

    // 용돈 스케줄링
    public void scheduleAllowanceJob(Long loginUserSerialNumber, LocalDateTime createDate, MonthlyAllowanceSaveOneRequest request) {

        try {
            //스케줄러 생성
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            //식별할 수 있게 jobName만들어서 key에 넣기
            String jobName = loginUserSerialNumber.toString() + Long.toString(request.getChildSerialNumber());
            JobKey jobKey = JobKey.jobKey(jobName, "allowanceGroup");

            //allowance에 맞는 jobDetail, trigger 생성
            JobDetail jobDetail = monthlyAllowanceJobDetailService.build(jobKey, loginUserSerialNumber,createDate, request);
            Trigger trigger = monthlyAllowanceTriggerService.build(jobKey, createDate);

            //스케줄러에 등록후 시작
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();

        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    //대출 스케줄링

}
