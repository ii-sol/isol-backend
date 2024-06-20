package shinhan.server_child.domain.invest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class InvestTradeDetailResDTO {
    String CompanyName;
    short quantity;
    int evaluationAmount;
    double profit;
    int profitAnsLossAmount;
    double holdingRatio;
}
