package shinhan.server_child.domain.mission.dto;

import java.util.List;

public class MissionFindRequest {

    private long childSn;
    private long parentsSn;
    private List<Integer> statuses;
}
