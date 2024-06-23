package shinhan.server_child.domain.invest.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_child.domain.invest.dto.InvestProposalHistoryResponse;
import shinhan.server_child.domain.invest.dto.InvestProposalSaveRequest;
import shinhan.server_child.domain.invest.entity.InvestProposal;
import shinhan.server_child.domain.invest.entity.InvestProposalResponse;
import shinhan.server_child.domain.invest.repository.InvestProposalRepository;
import shinhan.server_child.domain.invest.repository.InvestProposalResponseRepository;
import shinhan.server_common.domain.invest.repository.CorpCodeRepository;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;

@Service
@Transactional
public class InvestProposalService {
    InvestProposalRepository investProposalRepository;
    CorpCodeRepository corpCodeRepository;
    InvestProposalResponseRepository investProposalResponseRepository;
    @Autowired
    InvestProposalService(InvestProposalRepository investProposalRepository,CorpCodeRepository corpCodeRepository
    ,InvestProposalResponseRepository investProposalResponseRepository){
        this.investProposalRepository = investProposalRepository;
        this.corpCodeRepository = corpCodeRepository;
        this.investProposalResponseRepository = investProposalResponseRepository;
    }

    public InvestProposalResponse getInvestProposalResponse(Integer proposalId){
        return investProposalResponseRepository.findByProposalId(proposalId).get();
    }

    public Long proposalInvest(Long childSn,Long parentSn, InvestProposalSaveRequest investProposalSaveRequest){
        //알림 서비스
        investProposalRepository.save(investProposalSaveRequest.toInvestProposal(childSn, parentSn));
        return childSn;
    }

    public InvestProposal getProposalInvestDetail(int proposalId,Long childSn){
        System.out.println(proposalId);
        System.out.println(childSn);
        return investProposalRepository.findByIdAndChildSn(
            proposalId, childSn).orElseThrow(()->new CustomException(ErrorCode.FAILED_NOT_AUTHORITY_PROPOSAL));
    }

    public List<InvestProposalHistoryResponse> getProposalInvestHistory(Long userSn,int year,int month,short status){
        LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusMonths(1).minusSeconds(1);
        Timestamp startTimeStamp = Timestamp.valueOf(startDateTime);
        Timestamp endTimeStamp = Timestamp.valueOf(endDateTime);
        List<InvestProposalHistoryResponse> investProposalHistoryResponseList = new ArrayList<>();
        List<InvestProposal> investProposalList;
        if(status == 0)
            investProposalList = investProposalRepository.findByChildSnAndCreateDateBetween(userSn,startTimeStamp,endTimeStamp);
        else
            investProposalList = investProposalRepository.findByChildSnAndTradingCodeAndCreateDateBetween(userSn,status,startTimeStamp,endTimeStamp);

        for(InvestProposal data : investProposalList){
            investProposalHistoryResponseList.add(
                InvestProposalHistoryResponse.builder()
                    .CreateDate(new Date(data.getCreateDate().getTime()))
                    .proposeId(data.getId())
                    .tradingCode(data.getTradingCode())
                    .status(data.getStatus())
                    .ticker(data.getTicker())
                    .parentAlias(String.valueOf(data.getParentSn()))
                    .companyName(corpCodeRepository.findByStockCode(
                        Integer.parseInt(data.getTicker())).get().getCorpName())
                    .message(data.getMessage())
                    .quantity(data.getQuantity())
                    .build()
            );
        }
        return investProposalHistoryResponseList;
    }
}
