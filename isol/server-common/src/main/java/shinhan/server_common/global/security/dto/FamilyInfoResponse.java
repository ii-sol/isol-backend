package shinhan.server_common.global.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FamilyInfoResponse{

    private long sn;
    private int profileId;
    private String name;
}
