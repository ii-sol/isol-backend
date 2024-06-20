package shinhan.server_child.domain.invest.dto;


import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shinhan.server_common.domain.invest.dto.StockFindCurrentResponseDTO;

@NoArgsConstructor
@Getter
@Builder
public class MyStockListResDTO {
    List<StockFindCurrentResponseDTO> tradableStockList;

    public MyStockListResDTO(List<StockFindCurrentResponseDTO> tradableStockList) {
        this.tradableStockList = tradableStockList;
    }
}
