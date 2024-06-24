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

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class DynamicScheduler implements SchedulingConfigurer {

    private final Map<String, CronTask> cronTasks = new ConcurrentHashMap<>();
    private final Map<String, ScheduledFuture<?>> scheduledFutures = new ConcurrentHashMap<>();
    private final Map<String, ExecutionInfo> executionInfoMap = new ConcurrentHashMap<>();

    private final TaskScheduler taskScheduler;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler);  // TaskScheduler 설정 추가
    }

    // 스케줄링 시작 메소드
    public void scheduleTask(String taskId, LocalDateTime executionTime, Integer period, Runnable task) {
        executionInfoMap.put(taskId, new ExecutionInfo(period, 0));
        String cronExpression = "*/5 * * * * *";

        CronTask cronTask = new CronTask(() -> {
            executionInfoMap.get(taskId).incrementCount();
            task.run();
            if (executionInfoMap.get(taskId).getLimit() <= executionInfoMap.get(taskId).getCount()) {
                stopScheduledTask(taskId);
            }
            // 숫자가 같아지면 종료 상태로 변경해줘야함 근데 이걸 어디서 하지?
        }, new CronTrigger(cronExpression));

        ScheduledFuture<?> future = taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        cronTasks.put(taskId, cronTask);
        scheduledFutures.put(taskId, future);
    }

    // 스케줄링 변경하기
    public void rescheduleTask(String taskId, LocalDateTime newExecutionTime, Integer period, Runnable task) {
        int executedCount = executionInfoMap.get(taskId).getCount();
        System.out.println(executedCount);

        stopScheduledTask(taskId);

        String newCronExpression = retestGenerateCronExpression(newExecutionTime);
        System.out.println(newCronExpression);
        executionInfoMap.put(taskId, new ExecutionInfo(period - executedCount, 0));

        CronTask cronTask = new CronTask(() -> {
            executionInfoMap.get(taskId).incrementCount();
            task.run();
            if (executionInfoMap.get(taskId).getLimit() <= executionInfoMap.get(taskId).getCount()) {
                stopScheduledTask(taskId);
            }
        }, new CronTrigger(newCronExpression));

        ScheduledFuture<?> future = taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        cronTasks.put(taskId, cronTask);
        scheduledFutures.put(taskId, future);
    }

    // 스케줄링 취소하기
    public void stopScheduledTask(String taskId) {
        ScheduledFuture<?> future = scheduledFutures.get(taskId);
        if (future != null) {
            future.cancel(true);
            scheduledFutures.remove(taskId);
            cronTasks.remove(taskId);
            executionInfoMap.remove(taskId);
            log.info("Task 취소 완료: {}", taskId);
        } else {
            log.info("현재 진행중인 task가 없음: {}", taskId);
        }
    }

    // 스케줄링 시간 정하는 메소드
    private String testGenerateCronExpression(LocalDateTime executionTime) {
        int month = executionTime.getMonthValue();
        int day = executionTime.getDayOfMonth();
        return String.format("0 */1 * %d %d *", day, month);
    }

    // 스케줄링 시간 정하는 메소드
    private String retestGenerateCronExpression(LocalDateTime executionTime) {
        int month = executionTime.getMonthValue();
        int day = executionTime.getDayOfMonth();
        return String.format("* * * %d %d *", day, month);
    }
}
