package shinhan.server_parent.domain.invest.repository;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_parent.domain.invest.entity.InvestProposal;

public interface InvestProposalRepository extends JpaRepository<InvestProposal,Integer> {
    List<InvestProposal> findByChildSn(Long childSn);
    List<InvestProposal> findByChildSnAndCreateDateBetween(Long childSn, Timestamp year,Timestamp month);
}
