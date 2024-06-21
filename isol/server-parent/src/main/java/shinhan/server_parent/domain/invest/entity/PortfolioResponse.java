package shinhan.server_parent.domain.invest.entity;

import java.util.List;
import lombok.Builder;
import shinhan.server_parent.domain.invest.dto.InvestTradeDetailResponse;

@Builder
public class PortfolioResponse {
    int totalEvaluationAmount;
    int totalPurchaseAmount;
    double totalProfit;
    List<InvestTradeDetailResponse> investTradeList;

}
