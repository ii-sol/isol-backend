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
}
