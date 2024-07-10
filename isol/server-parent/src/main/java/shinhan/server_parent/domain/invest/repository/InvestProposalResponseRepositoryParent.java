package shinhan.server_parent.domain.invest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_common.domain.invest.investEntity.InvestProposalResponseParent;

public interface InvestProposalResponseRepositoryParent extends JpaRepository<InvestProposalResponseParent,Integer> {
}
