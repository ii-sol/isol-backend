package shinhan.server_common.domain.invest.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_common.domain.invest.entity.InvestProposal;

public interface InvestProposalRepositoryChild extends JpaRepository<InvestProposal,Integer> {
    List<InvestProposal> findByChildSnAndCreateDateBetween(Long childSn, Timestamp year,Timestamp month);
    List<InvestProposal> findByChildSnAndStatusAndCreateDateBetween(Long childSn,short status,  Timestamp year,Timestamp month);
    Optional<InvestProposal> findByIdAndChildSn(int proposal,Long childSn);
}
