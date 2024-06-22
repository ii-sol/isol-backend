package shinhan.server_common.domain.user.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChildManageUpdateRequest {

    @NotNull(message = "아이를 선택해주세요.")
    @Min(value = 1000000000L, message = "고유번호 형식은 10자리 자연수입니다.")
    @Max(value = 9999999999L, message = "고유번호 형식은 10자리 자연수입니다.")
    private long childSn;
    @NotNull(message = "기준금리를 입력해주세요.")
    @DecimalMin(value = "0.0", message = "기준 금리는 0% 이상입니다.")
    @DecimalMax(value = "100.0", message = "기준 금리는 100% 이하입니다.")
    private Float baseRate;
    @NotNull(message = "투자 상한액을 입력해주세요.")
    @DecimalMin(value = "0", message = "투자 상한액은 0원 이상입니다.")
    @DecimalMax(value = "8300000", message = "투자 상한액은 8,300,000원 이하입니다.")
    private Integer investLimit;
    @NotNull(message = "대출 상한액을 입력해주세요.")
    @DecimalMin(value = "0", message = "대출 상한액은 0원 이상입니다.")
    @DecimalMax(value = "8300000", message = "대출 상한액은 8,300,000원 이하입니다.")
    private Integer loanLimit;
}
