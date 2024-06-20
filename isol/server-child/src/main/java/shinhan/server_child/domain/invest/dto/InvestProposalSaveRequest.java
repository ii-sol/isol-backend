package shinhan.server_child.domain.invest.dto;


import java.sql.Timestamp;
import lombok.Builder;
import lombok.Getter;
import shinhan.server_child.domain.invest.entity.InvestProposal;

@Builder
@Getter
public class InvestProposalSaveRequest {
    String ticker;
    String message;
    short quantity;
    short tradingCode;

    public InvestProposal toInvestProposal(Long childSn,Long parentSn)
    {
        return InvestProposal.builder()
            .childSn(childSn)
            .createDate(new Timestamp(System.currentTimeMillis()))
            .status((short) 1)
            .message(message)
            .parentSn(parentSn)
            .quantity(quantity)
            .ticker(ticker)
            .TradingCode(tradingCode)
            .build();
    }
}