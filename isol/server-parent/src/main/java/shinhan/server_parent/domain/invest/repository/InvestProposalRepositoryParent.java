package shinhan.server_parent.domain.invest.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_common.domain.invest.entity.InvestProposal;

public interface InvestProposalRepositoryParent extends JpaRepository<InvestProposal,Integer> {
    List<InvestProposal> findByParentSnAndChildSnAndCreateDateBetween(Long psn,Long csn, Timestamp startDate,Timestamp endDate);
    List<InvestProposal> findByParentSnAndChildSnAndTradingCodeAndCreateDateBetween(Long psn,Long csn,Short tradingCode,Timestamp startDate,Timestamp timestamp);
    List<InvestProposal> findByParentSnAndChildSnAndTradingCodeAndCreateDateAfter(Long psn,Long csn,Short tradingCode,Timestamp startDate);
    Optional<InvestProposal> findByIdAndParentSn(int proposalId,Long parentSn);
}
