package shinhan.server_child.domain.allowance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import shinhan.server_child.domain.allowance.entity.TemporalAllowance;
import shinhan.server_common.domain.entity.TempUser;

import java.util.List;

public interface TemporalAllowanceRepository extends JpaRepository<TemporalAllowance, Integer>,TemporalAllowanceRepositoryCustom {

    List<TemporalAllowance> findByChildSerialNumberAndStatus(Long userSerialNumber, Integer status);
}