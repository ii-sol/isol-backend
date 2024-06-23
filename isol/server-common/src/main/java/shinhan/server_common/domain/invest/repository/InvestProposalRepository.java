package shinhan.server_common.domain.invest.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_common.domain.invest.entity.InvestProposal;

public interface InvestProposalRepository extends JpaRepository<InvestProposal,Long> {
    Optional<InvestProposal> findByIdAndChildSn(int proposal,Long childSn);
}
