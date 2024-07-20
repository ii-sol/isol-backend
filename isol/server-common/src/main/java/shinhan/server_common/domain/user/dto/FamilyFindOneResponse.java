package shinhan.server_common.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FamilyFindOneResponse {

    private int id;
    private long childSn;
    private long parentsSn;
    private String parentsAlias;
}
