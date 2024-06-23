package shinhan.server_common.global.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class UserInfoResponse {

    private long sn;
    private String name;
    private Integer profileId;
    private List<FamilyInfoResponse> familyInfo;
}
