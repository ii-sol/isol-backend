package shinhan.server_common.domain.allowance.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TemporalAllowanceAcceptOneRequest {
    private boolean accept;
    private String message;
}
