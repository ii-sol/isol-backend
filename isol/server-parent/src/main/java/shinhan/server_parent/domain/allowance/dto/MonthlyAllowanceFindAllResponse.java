package shinhan.server_parent.domain.allowance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_parent.domain.allowance.entity.MonthlyAllowance;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAllowanceFindAllResponse {
    private int id;
    private int amount;
    private int period;
    private LocalDateTime createDate;
    private LocalDateTime dueDate;

    public static MonthlyAllowanceFindAllResponse of(MonthlyAllowance allowance, Integer period){
        return MonthlyAllowanceFindAllResponse.builder()
                .id(allowance.getId())
                .amount(allowance.getPrice())
                .period(period)
                .createDate(allowance.getCreateDate())
                .dueDate(allowance.getDueDate())
                .build();
    }
}