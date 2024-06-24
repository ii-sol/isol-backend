package shinhan.server_common.domain.invest.dto;

import lombok.Builder;
import lombok.Getter;
import shinhan.server_common.domain.invest.investEntity.InvestProposal;
import shinhan.server_common.domain.invest.investEntity.InvestProposalResponse;

@Getter
@Builder
public class InvestProposalDetailResponse {
    StockFindDetailResponse companyInfo;
    InvestProposal requestProposal;
    InvestProposalResponse responseProposal;
}
