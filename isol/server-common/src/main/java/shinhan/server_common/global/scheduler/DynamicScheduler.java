package shinhan.server_common.global.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class DynamicScheduler implements SchedulingConfigurer {

    private final Map<String, CronTask> cronTasks = new ConcurrentHashMap<>();
    private final Map<String, ScheduledTaskRegistrar> taskRegistrars = new HashMap<>();
    private Map<String, ExecutionInfo> executionInfoMap = new ConcurrentHashMap<>();

    private final TaskScheduler taskScheduler;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler);  // TaskScheduler 설정 추가
        this.taskRegistrars.put("default", taskRegistrar);
    }

    //스케줄링 시작 메소드
    public void scheduleTask(String taskId, LocalDateTime executionTime, Integer period, Runnable task) {
//        String cronExpression = testGenerateCronExpression(executionTime);
        executionInfoMap.put(taskId, new ExecutionInfo(period, 0));
        System.out.println(executionInfoMap.get(taskId).getLimit());
        String cronExpression = "*/5 * * * * *";
        CronTask cronTask = new CronTask(() -> {
            System.out.println("cronTask");
            // 실행횟수 증가
            executionInfoMap.get(taskId).incrementCount();

            //스케줄링 작업 시작
            task.run();

            //만약 실행횟수가 period보다 커지면 전체 스케줄링 멈추고 삭제해줌
            if(executionInfoMap.get(taskId).getLimit() <= executionInfoMap.get(taskId).getCount()){
                stopScheduledTask(taskId);
            }
        }, new CronTrigger(cronExpression));

        this.cronTasks.put(taskId, cronTask);
        // Dynamically add a new task
        taskRegistrars.get("default").addCronTask(cronTask);
         //실제 스케줄링을 시작하도록 강제 호출
        taskRegistrars.get("default").afterPropertiesSet();
    }

    //스케줄링 시간 정하는거 test하기 위한 것 => 5분마다 실행
    private String testGenerateCronExpression(LocalDateTime executionTime) {
        System.out.println("cronExpression들어옴");
        int month = executionTime.getMonthValue();
        int day = executionTime.getDayOfMonth();
        System.out.println(day);
        //        return String.format("0 0 13 %d * ?", month);
        return String.format("0 */1 * %d %d *", day, month);
    }

    //스케줄링 시간 정하는거 test하기 위한 것 => 5분마다 실행
    private String retestGenerateCronExpression(LocalDateTime executionTime) {
        System.out.println("cronExpression들어옴");
        int month = executionTime.getMonthValue();
        int day = executionTime.getDayOfMonth();
        System.out.println(day);
        //        return String.format("0 0 13 %d * ?", month);
        return String.format("0 */2 * %d %d *", day, month);
    }


    //스케줄링 변경하기
    public void rescheduleTask(String taskId, LocalDateTime newExecutionTime, Integer period,  Runnable task){

        //실행된 횟수 숫자 가져옴
        int executedCount = executionInfoMap.get(taskId).getCount();
        //기존의 스케줄 취소
        stopScheduledTask(taskId);

        // 새로운 Cron 표현식 생성
        String newCronExpression = retestGenerateCronExpression(newExecutionTime);
        executionInfoMap.put(taskId, new ExecutionInfo(period-executedCount, 0));

        CronTask cronTask = new CronTask(() -> {
            System.out.println("rescheduling-cronTask");
            // 실행횟수 증가
            executionInfoMap.get(taskId).incrementCount();

            //스케줄링 작업 시작
            task.run();

            //만약 실행횟수가 period보다 커지면 전체 스케줄링 멈추고 삭제해줌
            if(executionInfoMap.get(taskId).getLimit() <= executionInfoMap.get(taskId).getCount()){
                stopScheduledTask(taskId);
            }
        }, new CronTrigger(newCronExpression));

        this.cronTasks.put(taskId, cronTask);
        // Dynamically add a new task
        taskRegistrars.get("default").addCronTask(cronTask);
        //실제 스케줄링을 시작하도록 강제 호출
        taskRegistrars.get("default").afterPropertiesSet();
    }


    //스케줄링 취소하기
    public void stopScheduledTask(String taskId){
        CronTask existingCronTask = cronTasks.get(taskId);
        if (existingCronTask != null) {
            // 기존의 CronTask를 취소
            System.out.println(taskRegistrars.get("default").getCronTaskList());
            taskRegistrars.get("default").getCronTaskList().remove(taskId);
            cronTasks.remove(taskId);
            executionInfoMap.remove(taskId);
            log.info("task 취소 완료");
        } else {
            log.info("현재 진행중인 task가 없음");
        }
    }

}


//    private String generateCronExpression(LocalDateTime executionTime, int periodInSeconds) {
//        int second = executionTime.getSecond();
//        int minute = executionTime.getMinute();
//        int hour = executionTime.getHour();
//        int dayOfMonth = executionTime.getDayOfMonth();
//        int month = executionTime.getMonthValue();
//
//        // Cron expression format: second minute hour dayOfMonth month *
//        return String.format("0 0 13 %d * *", dayOfMonth);
//    }

//public void scheduleTransmit(Long parentsSerialNumber, Long childSerialNumber, Integer amount, LocalDateTime startDate, Integer period) {
//    for (int i = 0; i < period; i++) {
//        LocalDateTime scheduledDate = startDate.plusMonths(i);
//        taskScheduler.schedule(
//                () -> {
//                    Account parentsAccount = accountUtils.getAccountByUserSerialNumberAndStatus(parentsSerialNumber, 3);
//                    Account childAccount = accountUtils.getAccountByUserSerialNumberAndStatus(childSerialNumber, 1);
//                    accountUtils.transferMoneyByAccount(parentsAccount, childAccount, amount, 4);
//                },
//                Date.from(scheduledDate.atZone(ZoneId.systemDefault()).toInstant())
//        );
//    }
//}
//
//public void cancelScheduledTask() {
//    if (future != null) {
//        future.cancel(true);
//    }
//}

