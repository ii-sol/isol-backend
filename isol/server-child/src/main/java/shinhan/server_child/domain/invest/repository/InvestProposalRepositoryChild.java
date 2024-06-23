package shinhan.server_child.domain.invest.repository;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_common.domain.invest.entity.InvestProposal;

public interface InvestProposalRepositoryChild extends JpaRepository<InvestProposal,Integer> {
    List<InvestProposal> findByChildSnAndCreateDateBetween(Long childSn, Timestamp year,Timestamp month);
    List<InvestProposal> findByChildSnAndTradingCodeAndCreateDateBetween(Long childSn,short tradingCode,  Timestamp year,Timestamp month);
}
