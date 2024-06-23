package shinhan.server_common.domain.invest.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_common.domain.invest.dto.InvestProposalHistoryResponse;
import shinhan.server_common.domain.invest.dto.InvestProposalSaveRequest;
import shinhan.server_common.domain.invest.entity.InvestProposal;
import shinhan.server_common.domain.invest.entity.InvestProposalResponse;
import shinhan.server_common.domain.invest.repository.InvestProposalRepository;
import shinhan.server_common.domain.invest.repository.InvestProposalResponseRepository;
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
        return investProposalRepository.findByIdAndChildSn(
            proposalId, childSn).orElseThrow(()->new CustomException(ErrorCode.FAILED_NOT_AUTHORITY_PROPOSAL));
    }
}
