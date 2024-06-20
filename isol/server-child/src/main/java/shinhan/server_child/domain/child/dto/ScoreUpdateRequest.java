package shinhan.server_child.domain.child.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ScoreUpdateRequest {

    private long sn;
    private int change;
}
