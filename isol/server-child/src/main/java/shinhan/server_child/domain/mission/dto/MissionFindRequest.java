package shinhan.server_child.domain.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MissionFindRequest {

    private long childSn;
    private long parentsSn;
    private List<Integer> statuses;
}
