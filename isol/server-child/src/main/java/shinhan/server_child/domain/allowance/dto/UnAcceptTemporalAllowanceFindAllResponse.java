package shinhan.server_child.domain.allowance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_child.domain.allowance.entity.ChildTemporalAllowance;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnAcceptTemporalAllowanceFindAllResponse {
    private int amount;
    private String name;
    private LocalDate createDate;

    public static UnAcceptTemporalAllowanceFindAllResponse of(ChildTemporalAllowance childTemporalAllowance, String name){
        return UnAcceptTemporalAllowanceFindAllResponse.builder()
                .amount(childTemporalAllowance.getPrice())
                .name(name)
                .build();
    }
}