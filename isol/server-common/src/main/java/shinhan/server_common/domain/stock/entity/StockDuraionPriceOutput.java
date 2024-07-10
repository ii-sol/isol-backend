package shinhan.server_common.domain.stock.entity;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockDuraionPriceOutput {
    
    private StockDurationDetail output1;
    private List<StockCartDate> output2;

}
