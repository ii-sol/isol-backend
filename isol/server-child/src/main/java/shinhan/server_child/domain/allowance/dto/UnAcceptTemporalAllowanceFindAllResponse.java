package shinhan.server_child.domain.allowance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_child.domain.allowance.entity.TemporalAllowance;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnAcceptTemporalAllowanceFindAllResponse {
    private int id;
    private int amount;
    private String name;
    private LocalDate createDate;

    public static UnAcceptTemporalAllowanceFindAllResponse of(TemporalAllowance temporalAllowance, String name){
        return UnAcceptTemporalAllowanceFindAllResponse.builder()
                .id(temporalAllowance.getId())
                .amount(temporalAllowance.getPrice())
                .name(name)
                .build();
    }
}