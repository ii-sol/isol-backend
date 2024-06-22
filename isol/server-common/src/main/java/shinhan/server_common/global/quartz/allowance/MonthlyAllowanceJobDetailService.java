package shinhan.server_common.global.quartz.allowance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.springframework.stereotype.Component;
import shinhan.server_common.global.quartz.allowance.dto.MonthlyAllowanceSaveOneRequest;

import java.time.LocalDateTime;
import java.time.Month;

import static org.quartz.JobBuilder.newJob;

@Slf4j
@RequiredArgsConstructor
@Component
public class MonthlyAllowanceJobDetailService {
    public JobDetail build(JobKey jobKey, Long parentsSerialNumber, LocalDateTime createDate, MonthlyAllowanceSaveOneRequest request){

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("parentsSerialNumber", parentsSerialNumber);
        jobDataMap.put("childSerialNumber", request.getChildSerialNumber());
        jobDataMap.put("amount", request.getAmount()); //request에서 빼올까 아님 allowance에서 빼올까
        jobDataMap.put("createDate", createDate);
        jobDataMap.put("dueDate", createDate.plusMonths(request.getPeriod()));
        jobDataMap.put("type", 3);

        return newJob(MonthlyAllowanceJob.class)
                .withIdentity(jobKey.getName(), jobKey.getGroup())
                .storeDurably(true)
                .usingJobData(jobDataMap)
                .build();
    }
}