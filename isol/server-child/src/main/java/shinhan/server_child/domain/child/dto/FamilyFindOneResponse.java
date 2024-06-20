package shinhan.server_child.domain.child.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FamilyFindOneResponse {

    private int id;
    @JsonProperty(value = "child_sn")
    private long childSn;
    @JsonProperty(value = "parents_sn")
    private long parentsSn;
    @JsonProperty(value = "parents_alias")
    private String parentsAlias;
}
