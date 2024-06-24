package shinhan.server_parent.domain.mission.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MissionAnswerSaveRequest {

    @NotNull(message = "미션을 선택해주세요.")
    private int id;
    @NotNull(message = "아이 회원을 선택해주세요.")
    @Min(value = 1000000000L, message = "고유번호 형식은 10자리 자연수입니다.")
    @Max(value = 9999999999L, message = "고유번호 형식은 10자리 자연수입니다.")
    private long childSn;
    @NotNull(message = "부모 회원을 선택해주세요.")
    @Min(value = 1000000000L, message = "고유번호 형식은 10자리 자연수입니다.")
    @Max(value = 9999999999L, message = "고유번호 형식은 10자리 자연수입니다.")
    private long parentsSn;
    @NotNull(message = "미션 요청에 응답해주세요.")
    private boolean answer;
}
