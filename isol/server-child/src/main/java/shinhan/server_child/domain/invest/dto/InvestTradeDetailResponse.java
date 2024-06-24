package shinhan.server_child.domain.invest.dto;

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
    String ticker;
    int profitAnsLossAmount;
    @Setter
    double holdingRatio;

}
