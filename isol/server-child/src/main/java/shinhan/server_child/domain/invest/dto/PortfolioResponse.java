package shinhan.server_child.domain.invest.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PortfolioResponse {
    int totalEvaluationAmount;
    int totalPurchaseAmount;
    double totalProfit;
    List<InvestTradeDetailResponse> investTradeList;
}
