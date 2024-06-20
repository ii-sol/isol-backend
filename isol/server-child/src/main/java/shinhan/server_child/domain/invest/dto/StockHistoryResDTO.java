package shinhan.server_child.domain.invest.dto;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class StockHistoryResDTO {
    private String companyName;
    private Integer stockPrice;
    private Short quantity; // Use Short for tinyint
    private Short tradingCode; // Use Short for tinyint
    private Date createDate;
}