package shinhan.server_child.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import shinhan.server_child.domain.user.entity.Parents;

import java.sql.Date;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ParentsFindOneResponse {

    private int id;
    @JsonProperty(value = "serial_number")
    private long serialNumber;
    @JsonProperty(value = "phone_num")
    private String phoneNum;
    private String name;
    @JsonProperty(value = "birth_date")
    private Date birthDate;
    @JsonProperty(value = "profile_id")
    private int profileId;

    public static ParentsFindOneResponse from(Parents parents) {
        return ParentsFindOneResponse.builder()
                .id(parents.getId())
                .serialNumber(parents.getSerialNum())
                .phoneNum(parents.getPhoneNum())
                .name(parents.getName())
                .birthDate(parents.getBirthDate())
                .profileId(parents.getProfileId())
                .build();
    }

}
