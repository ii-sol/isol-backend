package shinhan.server_parent.domain.allowance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_parent.domain.allowance.entity.TemporalAllowance;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TemporalAllowanceFindAllResponse {
    private int id;
    private int amount;
    private String content;
    private LocalDateTime createDate;

    public static TemporalAllowanceFindAllResponse from(TemporalAllowance temporalAllowance){
        return TemporalAllowanceFindAllResponse.builder()
                .id(temporalAllowance.getId())
                .amount(temporalAllowance.getPrice())
                .content(temporalAllowance.getContent())
                .createDate(temporalAllowance.getCreateDate())
                .build();
    }
}