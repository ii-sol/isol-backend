package shinhan.server_common.global.quartz.allowance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MonthlyAllowanceSaveOneRequest {
    private long childSerialNumber;
    private int amount;
    private int period;
}
