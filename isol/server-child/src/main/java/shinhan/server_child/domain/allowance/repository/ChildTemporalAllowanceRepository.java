package shinhan.server_child.domain.allowance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_child.domain.allowance.entity.ChildTemporalAllowance;

import java.util.List;

public interface ChildTemporalAllowanceRepository extends JpaRepository<ChildTemporalAllowance, Integer>, ChildTemporalAllowanceRepositoryCustom {

    List<ChildTemporalAllowance> findByChildSerialNumAndStatus(Long userSerialNumber, Integer status);
}