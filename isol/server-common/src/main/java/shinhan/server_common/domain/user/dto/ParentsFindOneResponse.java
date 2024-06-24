package shinhan.server_common.domain.user.dto;

import lombok.*;

import java.sql.Date;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ParentsFindOneResponse {

    private long sn;
    private String phoneNum;
    private String name;
    private Date birthDate;
    private int profileId;
}
