package shinhan.server_child.domain.invest.dto;

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
    Date CreateDate;
    @Setter
    String parentAlias;
}
