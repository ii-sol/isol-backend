package shinhan.server_common.domain.invest.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_common.domain.invest.entity.InvestProposalResponse;

public interface InvestProposalResponseRepository extends JpaRepository<InvestProposalResponse,Integer> {
    Optional<InvestProposalResponse> findByProposalId(Integer proposalId);
}
