package shinhan.server_common.domain.user.dto;

import lombok.*;

import java.sql.Date;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
//@Component
public class ParentsFindOneResponse {

    private long serialNumber;
    private String phoneNum;
    private String name;
    private Date birthDate;
    private int profileId;
}
