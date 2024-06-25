package shinhan.server_common.global.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MonthlyAllowanceScheduleSaveOneRequest {
    private long childSerialNumber;
    private int amount;
    private int period;
}
