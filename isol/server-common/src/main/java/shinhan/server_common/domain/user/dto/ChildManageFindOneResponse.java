package shinhan.server_common.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ChildManageFindOneResponse {

    private float baseRate;
    private int investLimit;
    private int loanLimit;
}
