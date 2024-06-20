package shinhan.server_common.domain.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@RequiredArgsConstructor
@Getter
@Setter
public class ParentsUpdateRequest {

    private long serialNum;
    private final String phoneNum;
    private final String name;
    private final Date birthDate;
    private final int profileId;
}
