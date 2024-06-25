package shinhan.server_common.domain.invest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_common.domain.invest.investEntity.Portfolio;
import shinhan.server_common.domain.invest.investEntity.StockHistory;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvestStockRequest {
    short trading;
    String ticker;
    short quantity;

    public StockHistory toEntityHistory(String account,int stockPrice){
        return new StockHistory(account,ticker,stockPrice,quantity,trading);
    }
    public Portfolio toEntityPortfolio(String accountNum,int stockPrice){
        return new Portfolio(ticker,quantity,stockPrice,accountNum);
    }
}
