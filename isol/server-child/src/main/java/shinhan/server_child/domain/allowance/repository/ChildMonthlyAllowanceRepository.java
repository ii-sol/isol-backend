package shinhan.server_child.domain.allowance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_child.domain.allowance.entity.ChildMonthlyAllowance;

import java.util.List;

public interface ChildMonthlyAllowanceRepository extends JpaRepository<ChildMonthlyAllowance, Integer>, ChildMonthlyAllowanceRepositoryCustom {


    List<ChildMonthlyAllowance> findByChildSerialNumAndStatus(Long serialNumber, Integer status);
}
