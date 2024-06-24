package shinhan.server_common.domain.invest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_common.domain.invest.investEntity.InvestProposalResponse;
import shinhan.server_common.domain.invest.repository.InvestProposalResponseRepository;
import shinhan.server_common.domain.invest.repository.CorpCodeRepository;

@Service
@Transactional
public class InvestProposalService {

    CorpCodeRepository corpCodeRepository;
    InvestProposalResponseRepository investProposalResponseRepository;
    @Autowired
    InvestProposalService(CorpCodeRepository corpCodeRepository
    ,InvestProposalResponseRepository investProposalResponseRepository){
        this.corpCodeRepository = corpCodeRepository;
        this.investProposalResponseRepository = investProposalResponseRepository;
    }

    public InvestProposalResponse getInvestProposalResponse(Integer proposalId){
        return investProposalResponseRepository.findByProposalId(proposalId).get();
    }
}
