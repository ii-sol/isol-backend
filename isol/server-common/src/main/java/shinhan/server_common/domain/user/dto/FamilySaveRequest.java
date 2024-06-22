package shinhan.server_common.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FamilySaveRequest {

    @Setter
    private long sn;
    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "전화번호 형식이 올바르지 않습니다.")
    private String phoneNum;
    @NotBlank(message = "부모님의 별명을 입력해주세요.")
    @Size(max = 15, message = "부모님의 별명은 최대 5글자(한글 기준)까지 입력 가능합니다.")
    private String parentsAlias;
}
