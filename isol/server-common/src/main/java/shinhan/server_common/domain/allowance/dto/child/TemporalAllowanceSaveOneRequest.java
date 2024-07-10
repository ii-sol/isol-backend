package shinhan.server_common.domain.allowance.dto.child;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TemporalAllowanceSaveOneRequest {
    private int amount;
    private String content;
}