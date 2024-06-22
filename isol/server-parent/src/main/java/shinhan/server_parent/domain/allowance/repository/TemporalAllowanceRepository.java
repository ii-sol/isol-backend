package shinhan.server_parent.domain.allowance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_parent.domain.allowance.entity.TemporalAllowance;

import java.util.List;

public interface TemporalAllowanceRepository extends JpaRepository<TemporalAllowance, Integer>, TemporalAllowanceRepositoryCustom {

}