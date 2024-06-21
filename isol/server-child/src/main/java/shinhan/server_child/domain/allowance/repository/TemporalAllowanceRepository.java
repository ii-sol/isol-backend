package shinhan.server_child.domain.allowance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_child.domain.allowance.entity.TemporalAllowance;

import java.util.List;

public interface TemporalAllowanceRepository extends JpaRepository<TemporalAllowance, Integer>, TemporalAllowanceRepositoryCustom {

    List<TemporalAllowance> findByChildSerialNumAndStatus(Long userSerialNumber, Integer status);
}