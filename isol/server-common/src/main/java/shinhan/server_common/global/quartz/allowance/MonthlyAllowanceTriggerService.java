package shinhan.server_common.global.quartz.allowance;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;

import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@Component
public class MonthlyAllowanceTriggerService {
    public Trigger build(JobKey jobKey, LocalDateTime createDate){

        //6월 13일 시작이면,6월 13일에 바로 이체 작업을 해주므로 7월 13일부터 시작
        Calendar startDate = Calendar.getInstance();
        startDate.set(createDate.getYear(), createDate.getMonthValue()+1, createDate.getDayOfMonth(), 13,0,0);

        return newTrigger()
                .forJob(jobKey)
                .withIdentity(new TriggerKey(jobKey.getName(), jobKey.getGroup()))
                .startAt(startDate.getTime())
                .withSchedule(calendarIntervalSchedule()
                        .withIntervalInMonths(1))
                .build();
    }

    //오류 발생으로 실패시 retry. retry 요청 들어오자마자 24시간 간격으로 3번 재시도
    public Trigger retryTrigger(){
        return newTrigger()
                .withSchedule(simpleSchedule()
                        .withIntervalInHours(24)
                        .withRepeatCount(3))
                .startNow()
                .withIdentity(new TriggerKey("retry"))
                .build();
    }
}