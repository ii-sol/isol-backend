package shinhan.server_common.global.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MonthlyAllowanceScheduleChangeOneRequest {
    private long childSerialNumber;
    private int amount;
    private int period;
    private int idBeforeChange; //원래 존재하던 정기용돈 id
    private LocalDateTime dateBeforeChange; // 원래 존재하던 시작날짜
}
