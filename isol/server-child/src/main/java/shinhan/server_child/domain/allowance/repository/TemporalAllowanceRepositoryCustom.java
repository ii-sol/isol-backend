package shinhan.server_child.domain.allowance.repository;

import shinhan.server_child.domain.allowance.entity.TemporalAllowance;
import shinhan.server_common.domain.entity.TempUser;

import java.util.List;

public interface TemporalAllowanceRepositoryCustom {
    List<TemporalAllowance> findByUserSerialNumberAndCreateDate(Long userSerialNumber, Integer year, Integer month, Integer csn);

    List<TemporalAllowance> findByChildSerialNumberAndCreateDateAndStatus(Long userSerialNumber, Integer year, Integer month);

}
