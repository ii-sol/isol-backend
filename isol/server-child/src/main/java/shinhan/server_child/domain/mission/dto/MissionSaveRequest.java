package shinhan.server_child.domain.mission.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MissionSaveRequest {

    @NotNull(message = "아이 회원을 선택해주세요.")
    @Min(value = 1000000000L, message = "고유번호 형식은 10자리 자연수입니다.")
    @Max(value = 9999999999L, message = "고유번호 형식은 10자리 자연수입니다.")
    private long childSn;
    @NotNull(message = "부모 회원을 선택해주세요.")
    @Min(value = 1000000000L, message = "고유번호 형식은 10자리 자연수입니다.")
    @Max(value = 9999999999L, message = "고유번호 형식은 10자리 자연수입니다.")
    private long parentsSn;
    @NotBlank(message = "미션 내용을 입력해주세요.")
    @Length(max = 20, message = "미션 내용은 최대 한글 기준 20자를 넘을 수 없습니다.")
    private String content;
    @NotNull(message = "미션 금액을 입력해주세요.")
    private int price;
    private Timestamp dueDate = new Timestamp(System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000));
}
