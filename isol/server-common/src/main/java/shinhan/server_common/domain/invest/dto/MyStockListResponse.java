package shinhan.server_common.domain.invest.dto;


import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_common.domain.invest.dto.StockFindCurrentResponse;

@NoArgsConstructor
@Getter
@Builder
public class MyStockListResponse {
    List<StockFindCurrentResponse> tradableStockList;

    public MyStockListResponse(List<StockFindCurrentResponse> tradableStockList) {
        this.tradableStockList = tradableStockList;
    }
}
