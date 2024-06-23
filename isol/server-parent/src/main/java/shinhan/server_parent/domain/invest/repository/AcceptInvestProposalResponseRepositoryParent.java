package shinhan.server_parent.domain.invest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_parent.domain.invest.entity.InvestProposalResponseParent;

public interface AcceptInvestProposalResponseRepositoryParent extends JpaRepository<InvestProposalResponseParent,Integer> {
}
