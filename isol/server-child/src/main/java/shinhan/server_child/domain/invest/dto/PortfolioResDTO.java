package shinhan.server_child.domain.invest.dto;

import java.util.List;
import lombok.Builder;
import shinhan.server_child.domain.invest.dto.InvestTradeDetailResDTO;

@Builder
public class PortfolioResDTO {
    int totalEvaluationAmount;
    int totalPurchaseAmount;
    double totalProfit;
    List<InvestTradeDetailResDTO> investTradeList;

}
