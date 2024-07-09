package shinhan.server_common.domain.allowance.dto.child;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_common.domain.allowance.entity.MonthlyAllowance;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MonthlyAllowanceFindOneResponse {
    private int id;
    private int amount;
    private String name;
    private int period;
    private LocalDateTime createDate;
    private LocalDateTime dueDate;

    public static MonthlyAllowanceFindOneResponse of(MonthlyAllowance allowance, Integer period, String parentsName){
        return MonthlyAllowanceFindOneResponse.builder()
                .id(allowance.getId())
                .amount(allowance.getPrice())
                .name(parentsName)
                .period(period)
                .createDate(allowance.getCreateDate())
                .dueDate(allowance.getDueDate())
                .build();
    }

}