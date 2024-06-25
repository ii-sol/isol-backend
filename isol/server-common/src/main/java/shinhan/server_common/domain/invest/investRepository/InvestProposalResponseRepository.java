package shinhan.server_common.domain.invest.investRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_common.domain.invest.investEntity.InvestProposalResponse;

public interface InvestProposalResponseRepository extends JpaRepository<InvestProposalResponse,Integer> {
    Optional<InvestProposalResponse> findByProposalId(Integer proposalId);
}
