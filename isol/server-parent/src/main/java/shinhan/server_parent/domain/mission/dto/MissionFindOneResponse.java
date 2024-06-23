package shinhan.server_parent.domain.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class MissionFindOneResponse {

    private int id;
    private long childSn;
    private long parentsSn;
    private String content;
    private int price;
    private Timestamp createDate;
    private Timestamp dueDate;
    private Timestamp completeDate;
    private int status;
}
