package shinhan.server_parent.domain.allowance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_parent.domain.allowance.entity.MonthlyAllowance;
import shinhan.server_parent.domain.allowance.entity.TemporalAllowance;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TotalAllowanceFindAllResponse {

    private int id;
    private int amount;
    private String content;
    private int status;
    private LocalDateTime createDate;
    private String type;

    public static TotalAllowanceFindAllResponse from(MonthlyAllowance monthlyAllowance) {
        return TotalAllowanceFindAllResponse.builder()
                .id(monthlyAllowance.getId())
                .amount(monthlyAllowance.getPrice())
                .content(null)
                .status(monthlyAllowance.getStatus())
                .createDate(monthlyAllowance.getCreateDate())
                .type("정기용돈")
                .build();
    }

    public static TotalAllowanceFindAllResponse from(TemporalAllowance temporalAllowance) {
        return TotalAllowanceFindAllResponse.builder()
                .id(temporalAllowance.getId())
                .amount(temporalAllowance.getPrice())
                .content(temporalAllowance.getContent())
                .status(temporalAllowance.getStatus())
                .createDate(temporalAllowance.getCreateDate())
                .type("일시용돈")
                .build();
    }
}