package shinhan.server_parent.domain.allowance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_parent.domain.allowance.entity.MonthlyAllowance;


public interface MonthlyAllowanceRepository extends JpaRepository<MonthlyAllowance, Integer>, MonthlyAllowanceRepositoryCustom {

}
