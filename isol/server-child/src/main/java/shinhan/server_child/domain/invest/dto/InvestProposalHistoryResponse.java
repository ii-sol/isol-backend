package shinhan.server_child.domain.invest.dto;

import java.sql.Date;
import lombok.Builder;
import lombok.Getter;

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
    String parentName;
    String parentAlias;
}
