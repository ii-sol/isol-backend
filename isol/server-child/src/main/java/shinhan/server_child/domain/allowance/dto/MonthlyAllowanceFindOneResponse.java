package shinhan.server_child.domain.allowance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_child.domain.allowance.entity.MonthlyAllowance;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MonthlyAllowanceFindOneResponse {
    private int amount;
    private String name;
    private int period;
    private LocalDateTime createDate;
    private LocalDateTime dueDate;

    public static MonthlyAllowanceFindOneResponse of(MonthlyAllowance allowance, Integer period){
        return MonthlyAllowanceFindOneResponse.builder()
                .amount(allowance.getPrice())
                .name(allowance.getParents().getName())
                .period(period)
                .createDate(allowance.getCreateDate())
                .dueDate(allowance.getDueDate())
                .build();
    }

}
