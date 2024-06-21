package shinhan.server_common.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ChildManageFIndOneResponse {

    private int id;
    private long childSn;
    private float baseRate;
    private int investLimit;
    private int loanLimit;
}
