package shinhan.server_common.domain.stock.entity;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@JsonNaming
public class StockFianceResponseOutput {
    public List<StockFianceDetail> output;

    public StockFianceResponseOutput(List<StockFianceDetail> output) {
        this.output = output;
    }

    public StockFianceResponseOutput() {
        this.output = new ArrayList<>();
    }
}
