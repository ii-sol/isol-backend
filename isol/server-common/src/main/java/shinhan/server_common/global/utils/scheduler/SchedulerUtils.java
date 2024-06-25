package shinhan.server_common.global.utils.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SchedulerUtils {

    // 이체 시간 정하는 메소드 => 한달에 한번 오후 1시에 출금
    public String generateTransmitCronExpression(LocalDateTime executionTime) {
        int month = executionTime.getMonthValue();
        int day = executionTime.getDayOfMonth();
//        return String.format("0 0 13 %d %d *", day, month);
        return String.format("* * * %d %d *", day, month);
    }

    // 김현수껄로?
    public String retestGenerateCronExpression(LocalDateTime executionTime) {
        int month = executionTime.getMonthValue();
        int day = executionTime.getDayOfMonth();
        return String.format("*/2 * * %d %d *", day, month);
    }
}
