package shinhan.server_child.domain.invest.dto;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockHistoryResponse {
    private String companyName;
    private Integer stockPrice;
    private Short quantity; // Use Short for tinyint
    private Short tradingCode; // Use Short for tinyint
    private Date createDate;
}