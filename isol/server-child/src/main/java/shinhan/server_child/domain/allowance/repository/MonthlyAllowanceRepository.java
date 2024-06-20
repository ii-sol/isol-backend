package shinhan.server_child.domain.allowance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_child.domain.allowance.entity.MonthlyAllowance;

import java.util.List;

public interface MonthlyAllowanceRepository extends JpaRepository<MonthlyAllowance, Integer>, MonthlyAllowanceRepositoryCustom {


    List<MonthlyAllowance> findByChildSerialNumberAndStatus(Long serialNumber, Integer status);
}
