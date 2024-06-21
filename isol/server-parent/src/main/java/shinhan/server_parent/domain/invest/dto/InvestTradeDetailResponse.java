package shinhan.server_parent.domain.invest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class InvestTradeDetailResponse {
    String CompanyName;
    short quantity;
    int evaluationAmount;
    double profit;
    int profitAnsLossAmount;
    @Setter
    double holdingRatio;

}
