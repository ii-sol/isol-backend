package shinhan.server_common.domain.invest.dto;

import java.sql.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class InvestProposalHistoryResponse {
    Short status;
    int proposeId;
    String companyName;
    short quantity;
    short tradingCode;
    String message;
    String ticker;
    Date CreateDate;
    @Setter
    String recieverName;
}
