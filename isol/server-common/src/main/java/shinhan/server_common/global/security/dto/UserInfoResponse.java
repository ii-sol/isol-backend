package shinhan.server_common.global.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserInfoResponse {

    private long sn;
    private int profileId;
    private List<FamilyInfoResponse> familyInfo;
}
