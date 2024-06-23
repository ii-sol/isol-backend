package shinhan.server_child.domain.invest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CorpCodeResponse {
    String companyName;
    boolean isMyStock;
    String ticker;
    boolean canTrading;
    String currentPrice;
    String changePrice;
    String changeSign;
    String changeRate;
}
