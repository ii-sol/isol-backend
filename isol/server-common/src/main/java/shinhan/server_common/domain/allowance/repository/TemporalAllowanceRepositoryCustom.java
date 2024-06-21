package shinhan.server_common.domain.allowance.repository;


import shinhan.server_parent.domain.allowance.entity.TemporalAllowance;

import java.util.Arrays;
import java.util.List;

public interface TemporalAllowanceRepositoryCustom {
    List<TemporalAllowance> findByUserSerialNumberAndCreateDate(Long userSerialNumber, Integer year, Integer month, Long csn);

    List<TemporalAllowance> findByParentsSerialNumberAndChildrenSerialNumberAndStatus(Long userSerialNumber, Long csn, Integer status);
}
