package shinhan.server_child.domain.invest.dto;

import lombok.Builder;
import lombok.Getter;
import shinhan.server_child.domain.invest.entity.InvestProposal;
import shinhan.server_child.domain.invest.entity.InvestProposalResponse;
import shinhan.server_common.domain.invest.dto.StockFindDetailResponse;

@Getter
@Builder
public class InvestProposalDetailResponse {
    StockFindDetailResponse companyInfo;
    InvestProposal requestProposal;
    InvestProposalResponse responseProposal;
}
