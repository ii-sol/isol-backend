package shinhan.server_child.domain.invest.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shinhan.server_child.domain.invest.dto.InvestProposalHistoryResponse;
import shinhan.server_child.domain.invest.dto.InvestProposalSaveRequest;
import shinhan.server_child.domain.invest.entity.InvestProposal;
import shinhan.server_child.domain.invest.repository.InvestProposalRepository;

@Service
public class InvestProposalService {
    InvestProposalRepository investProposalRepository;

    @Autowired
    InvestProposalService(InvestProposalRepository investProposalRepository){
        this.investProposalRepository = investProposalRepository;
    }

    public void proposalInvest(Long childSn,Long parentSn, InvestProposalSaveRequest investProposalSaveRequest){
        //알림 서비스
        System.out.println(investProposalSaveRequest.getTicker());
        investProposalRepository.save(investProposalSaveRequest.toInvestProposal(childSn, parentSn));
    }

    public List<InvestProposalHistoryResponse> getProposalInvestHistory(Long userSn,int year,int month){
        LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusMonths(1).minusSeconds(1);
        Timestamp startTimeStamp = Timestamp.valueOf(startDateTime);
        Timestamp endTimeStamp = Timestamp.valueOf(endDateTime);
        List<InvestProposalHistoryResponse> investProposalHistoryResponseList = new ArrayList<>();
        List<InvestProposal> investProposalList = investProposalRepository.findByChildSnAndCreateDateBetween(userSn,startTimeStamp,endTimeStamp);
        for(InvestProposal data : investProposalList){
            System.out.println(data.getMessage());
            investProposalHistoryResponseList.add(
                InvestProposalHistoryResponse.builder()
                    .CreateDate(new Date(data.getCreateDate().getTime()))
                    .proposeId(data.getId())
                    .tradingCode(data.getTradingCode())
                    .status(data.getStatus())
                    .parentAlias("나중에 확인")
                    .parentName("auth 완성되면")
                    .companyName(data.getTicker())
                    .message(data.getMessage())
                    .quantity(data.getQuantity())
                    .build()
            );
        }
        return investProposalHistoryResponseList;
    }
}
