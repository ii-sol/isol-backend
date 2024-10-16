package shinhan.server_common.domain.invest.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import shinhan.server_common.domain.stock.entity.StockNaverDuraion;

@Getter
@Builder
@AllArgsConstructor
@Data
public class StockFindDetailResponse {
    public String companyName;
    public int currentPrice;
    public String marketCapitalization;
    public String dividendYield;
    public String PBR;
    public String PER;
    public String ROE;
    public String profitGrowth;
    public String changePrice;
    public String changeSign;
    public String changeRate;
    public String ticker;
    public List<StockNaverDuraion> charts;
}
