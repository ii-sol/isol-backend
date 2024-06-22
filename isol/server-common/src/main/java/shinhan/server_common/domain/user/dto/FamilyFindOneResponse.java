package shinhan.server_common.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FamilyFindOneResponse {

    private int id;
    private long childSn;
    private long parentsSn;
    private String parentsAlias;
}
