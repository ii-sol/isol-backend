package shinhan.server_common.domain.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChildManageSaveRequest {

    @NotBlank(message = "아이를 선택해주세요.")
    @Pattern(regexp = "\\d{10}", message = "고유번호 형식은 10자리 자연수입니다.")
    private long childSn;
    @Min(value = 0, message = "기준 금리는 0원 이상입니다.")
    private int baseRate = 0;
    @Min(value = 0, message = "투자 상한액은 0원 이상입니다.")
    @Max(value = 8300000, message = "투자 상한액은 8,300,000원 이하입니다.")
    private int investLimit = 8300000;
    @Min(value = 0, message = "대출 상한액은 0 이상이어야 합니다.")
    @Max(value = 8300000, message = "대출 상한액은 8,300,000원 이하입니다.")
    private int loanLimit = 8300000;
}
