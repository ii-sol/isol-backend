package shinhan.server_child.domain.invest.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_child.domain.invest.entity.InvestProposalResponse;

public interface InvestProposalResponseRepository extends JpaRepository<InvestProposalResponse,Integer> {
    Optional<InvestProposalResponse> findByProposalId(Integer proposalId);
}
