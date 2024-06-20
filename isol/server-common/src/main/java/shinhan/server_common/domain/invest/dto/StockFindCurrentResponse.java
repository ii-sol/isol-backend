package shinhan.server_common.domain.invest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Data
public class StockFindCurrentResponse {
    public String ticker;
    private String companyName;
    public String changePrice;
    public String changeSign;
    public String changeRate;
    public String currentPrice;
}
