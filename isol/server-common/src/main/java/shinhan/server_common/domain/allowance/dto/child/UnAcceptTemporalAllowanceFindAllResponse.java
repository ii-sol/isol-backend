package shinhan.server_common.domain.allowance.dto.child;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_common.domain.allowance.entity.TemporalAllowance;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnAcceptTemporalAllowanceFindAllResponse {
    private int id;
    private int amount;
    private String name;
    private LocalDateTime createDate;

    public static UnAcceptTemporalAllowanceFindAllResponse of(TemporalAllowance temporalAllowance, String name){
        return UnAcceptTemporalAllowanceFindAllResponse.builder()
                .id(temporalAllowance.getId())
                .amount(temporalAllowance.getPrice())
                .name(name)
                .createDate(temporalAllowance.getCreateDate())
                .build();
    }
}