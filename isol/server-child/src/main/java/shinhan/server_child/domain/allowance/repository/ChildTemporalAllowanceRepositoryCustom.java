package shinhan.server_child.domain.allowance.repository;

import shinhan.server_child.domain.allowance.entity.ChildTemporalAllowance;

import java.util.List;

public interface ChildTemporalAllowanceRepositoryCustom {
//    List<ChildTemporalAllowance> findByUserSerialNumberAndCreateDate(Long userSerialNumber, Integer year, Integer month, Integer csn);

    List<ChildTemporalAllowance> findByChildSerialNumberAndCreateDateAndStatus(Long userSerialNumber, Integer year, Integer month);

}
