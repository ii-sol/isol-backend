package shinhan.server_child.domain.invest.dto;

import lombok.Getter;
import shinhan.server_child.domain.invest.entity.Portfolio;
import shinhan.server_child.domain.invest.entity.StockHistory;

@Getter
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
