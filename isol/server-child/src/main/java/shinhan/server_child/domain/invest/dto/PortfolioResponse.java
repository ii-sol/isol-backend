package shinhan.server_child.domain.invest.dto;

import java.util.List;
import lombok.Builder;

@Builder
public class PortfolioResponse {
    int totalEvaluationAmount;
    int totalPurchaseAmount;
    double totalProfit;
    List<InvestTradeDetailResponse> investTradeList;

}
