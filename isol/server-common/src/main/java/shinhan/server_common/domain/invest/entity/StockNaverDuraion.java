package shinhan.server_common.domain.invest.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StockNaverDuraion {
    String localDate;
    double closePrice;
    double openPrice;
    double highPrice;
    double lowPrice;

    public StockNaverDuraion adjustPrice(){
        this.closePrice = closePrice*100;
        this.openPrice = openPrice*100;
        this.highPrice = highPrice*100;
        this.lowPrice = lowPrice*100;
        return this;
    }

    public StockNaverDuraion(double closePrice, double openPrice, double highPrice,
        double lowPrice) {
        this.closePrice = closePrice*100;
        this.openPrice = openPrice*100;
        this.highPrice = highPrice*100;
        this.lowPrice = lowPrice*100;
    }

    public StockNaverDuraion(String localDate, double closePrice, double openPrice,
        double highPrice,
        double lowPrice) {
        this.localDate = localDate;
        this.closePrice = closePrice*100;
        this.openPrice = openPrice*100;
        this.highPrice = highPrice*100;
        this.lowPrice = lowPrice*100;
    }
}
