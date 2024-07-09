package shinhan.server_parent.domain.invest.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_common.domain.invest.dto.InvestProposalHistoryResponse;
import shinhan.server_common.domain.invest.entity.InvestProposal;
import shinhan.server_common.domain.stock.repository.CorpCodeRepository;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;
import shinhan.server_common.domain.invest.dto.ResponseInvestProposal;
import shinhan.server_common.domain.invest.entity.InvestProposalResponseParent;
import shinhan.server_common.domain.invest.repository.InvestProposalRepositoryParent;
import shinhan.server_common.domain.invest.repository.InvestProposalResponseRepositoryParent;

@Service
@Transactional
public class InvestProposalServiceParent {

    InvestProposalResponseRepositoryParent investProposalResponseRepository;
    InvestProposalRepositoryParent acceptInvestProposalRepositoryParent;
    CorpCodeRepository corpCodeRepository;

    InvestProposalServiceParent(
        InvestProposalResponseRepositoryParent investProposalResponseRepository,
        InvestProposalRepositoryParent acceptInvestProposalRepositoryParent,
        CorpCodeRepository corpCodeRepository
    ) {
        this.acceptInvestProposalRepositoryParent = acceptInvestProposalRepositoryParent;
        this.investProposalResponseRepository = investProposalResponseRepository;
        this.corpCodeRepository = corpCodeRepository;
    }

    public List<InvestProposalHistoryResponse> getProposalInvestHistory(Long parentSn, Long childSn,
        int year, int month, short status) {
        LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusMonths(1).minusSeconds(1);
        Timestamp startTimeStamp = Timestamp.valueOf(startDateTime);
        Timestamp endTimeStamp = Timestamp.valueOf(endDateTime);
        List<InvestProposalHistoryResponse> investProposalHistoryResponseList = new ArrayList<>();
        List<InvestProposal> investProposalList;
        if (status == 0) {
            investProposalList = acceptInvestProposalRepositoryParent.findByParentSnAndChildSnAndCreateDateBetween(
                parentSn,
                childSn, startTimeStamp, endTimeStamp);
        } else {
            investProposalList = acceptInvestProposalRepositoryParent.findByParentSnAndChildSnAndStatusAndCreateDateBetween(
                parentSn, childSn, status, startTimeStamp, endTimeStamp);
        }
        for (InvestProposal data : investProposalList) {
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

    public InvestProposal getProposalInvestDetail(int proposalId, Long parentSn) {
        return acceptInvestProposalRepositoryParent.findByIdAndParentSn(
                proposalId, parentSn)
            .orElseThrow(() -> new CustomException(ErrorCode.FAILED_NOT_AUTHORITY_PROPOSAL));
    }

    public InvestProposal setInvestProposalServiceParent(Long psn, int proposalId,
        ResponseInvestProposal proposal) {
        Optional<InvestProposal> resultInvestProposal = acceptInvestProposalRepositoryParent.findById(
            proposalId);

        if (!Objects.equals(resultInvestProposal.orElseThrow(
            () -> new CustomException(ErrorCode.FOUND_NOT_INVEST_PROPOSAL)).getParentSn(), psn)) {
            throw new CustomException(ErrorCode.FAILED_NOT_AUTHORITY_PROPOSAL);
        }
        if (proposal.isAccept()) {
            resultInvestProposal.get().setStatus((short) 3);

        } else {
            resultInvestProposal.get().setStatus((short) 5);
            investProposalResponseRepository.save(InvestProposalResponseParent.builder()
                .message(proposal.getMessage())
                .proposalId(proposalId)
                .createDate(new Timestamp(System.currentTimeMillis()))
                .build());
        }
        return resultInvestProposal.get();
    }

    public List<InvestProposalHistoryResponse> getInvestProposalNoApproved(Long parentSn, Long childSn) {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(3);
        Timestamp startDate = Timestamp.valueOf(localDateTime);
        List<InvestProposalHistoryResponse> investProposalHistoryResponseList = new ArrayList<>();
        List<InvestProposal> investProposalList = acceptInvestProposalRepositoryParent.findByParentSnAndChildSnAndStatusAndCreateDateAfter(parentSn,childSn,
            (short) 1,startDate);
        for (InvestProposal data : investProposalList) {
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
}
