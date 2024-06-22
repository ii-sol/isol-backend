package shinhan.server_child.domain.invest.repository;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_child.domain.invest.entity.InvestProposal;

public interface InvestProposalRepository extends JpaRepository<InvestProposal,Long> {
    List<InvestProposal> findByChildSn(Long childSn);
    List<InvestProposal> findByChildSnAndCreateDateBetween(Long childSn, Timestamp year,Timestamp month);
    List<InvestProposal> findByChildSnAndTradingCodeAndCreateDateBetween(Long childSn,short tradingCode,  Timestamp year,Timestamp month);
}
