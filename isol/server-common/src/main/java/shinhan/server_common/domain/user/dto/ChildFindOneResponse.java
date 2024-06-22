package shinhan.server_common.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shinhan.server_common.domain.user.entity.Child;

import java.sql.Date;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ChildFindOneResponse {

    private long serialNumber;
    private String phoneNum;
    private String name;
    private Date birthDate;
    private int profileId;
    private int score;
}
