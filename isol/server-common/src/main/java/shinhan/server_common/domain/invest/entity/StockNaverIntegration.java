package shinhan.server_common.domain.invest.entity;

import java.util.List;
import lombok.Data;

@Data
public class StockNaverIntegration {
    String itemCode;
    String stockName;
    List<StockNaverIntegrationDetail> totalInfos;
}
