package shinhan.server_parent.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shinhan.server_parent.domain.user.entity.Parents;

import java.sql.Date;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ParentsFindOneResponse {

    private long serialNumber;
    private String phoneNum;
    private String name;
    private Date birthDate;
    private int profileId;

    public static ParentsFindOneResponse from(Parents parents) {
        return ParentsFindOneResponse.builder()
                .serialNumber(parents.getSerialNum())
                .phoneNum(parents.getPhoneNum())
                .name(parents.getName())
                .birthDate(parents.getBirthDate())
                .profileId(parents.getProfileId())
                .build();
    }

}
