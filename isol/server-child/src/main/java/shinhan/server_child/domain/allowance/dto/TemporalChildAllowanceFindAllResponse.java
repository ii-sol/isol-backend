package shinhan.server_child.domain.allowance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_child.domain.allowance.entity.TemporalAllowance;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TemporalChildAllowanceFindAllResponse {

    private int amount;
    private String name;
    private int status;
    private LocalDateTime createDate;

    public static TemporalChildAllowanceFindAllResponse of(TemporalAllowance temporalAllowance, String name){
        return TemporalChildAllowanceFindAllResponse.builder()
                .amount(temporalAllowance.getPrice())
                .name(name)
                .status(temporalAllowance.getStatus())
                .createDate(temporalAllowance.getCreateDate())
                .build();
    }
}