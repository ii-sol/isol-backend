package shinhan.server_child.domain.invest.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_common.domain.invest.repository.InvestProposalRepositoryChild;
import shinhan.server_common.domain.invest.dto.InvestProposalHistoryResponse;
import shinhan.server_common.domain.invest.dto.InvestProposalSaveRequest;
import shinhan.server_common.domain.invest.entity.InvestProposal;
import shinhan.server_common.domain.stock.repository.CorpCodeRepository;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;

@Service
@Transactional
public class InvestProposalServiceChild {
    InvestProposalRepositoryChild investProposalRepositoryChild;

    CorpCodeRepository corpCodeRepository;
    InvestProposalServiceChild(InvestProposalRepositoryChild investProposalRepositoryChild,
    CorpCodeRepository corpCodeRepository){
        this.investProposalRepositoryChild = investProposalRepositoryChild;
        this.corpCodeRepository = corpCodeRepository;
    }

    public List<InvestProposalHistoryResponse> getProposalInvestHistory(Long userSn,int year,int month,short status){
        LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusMonths(1).minusSeconds(1);
        Timestamp startTimeStamp = Timestamp.valueOf(startDateTime);
        Timestamp endTimeStamp = Timestamp.valueOf(endDateTime);
        List<InvestProposalHistoryResponse> investProposalHistoryResponseList = new ArrayList<>();
        List<InvestProposal> investProposalList;
        if(status == 0)
            investProposalList = investProposalRepositoryChild.findByChildSnAndCreateDateBetween(userSn,startTimeStamp,endTimeStamp);
        else
            investProposalList = investProposalRepositoryChild.findByChildSnAndStatusAndCreateDateBetween(userSn,status,startTimeStamp,endTimeStamp);
        for(InvestProposal data : investProposalList){
            investProposalHistoryResponseList.add(
                InvestProposalHistoryResponse.builder()
                    .CreateDate(new Date(data.getCreateDate().getTime()))
                    .proposeId(data.getId())
                    .tradingCode(data.getTradingCode())
                    .status(data.getStatus())
                    .ticker(data.getTicker())
                    .recieverName(String.valueOf(data.getParentSn()))
                    .companyName(corpCodeRepository.findByStockCode(
                        Integer.parseInt(data.getTicker())).get().getCorpName())
                    .message(data.getMessage())
                    .quantity(data.getQuantity())
                    .build()
            );
        }
        return investProposalHistoryResponseList;
    }

    public Long proposalInvest(Long childSn,Long parentSn, InvestProposalSaveRequest investProposalSaveRequest){
        //알림 서비스
        investProposalRepositoryChild.save(investProposalSaveRequest.toInvestProposal(childSn, parentSn));
        return childSn;
    }

    public InvestProposal getProposalInvestDetail(int proposalId,Long childSn){
        return investProposalRepositoryChild.findByIdAndChildSn(
            proposalId, childSn).orElseThrow(()->new CustomException(ErrorCode.FAILED_NOT_AUTHORITY_PROPOSAL));
    }
}
