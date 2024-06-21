package shinhan.server_parent.domain.allowance.repository;

import shinhan.server_parent.domain.allowance.entity.MonthlyAllowance;

import java.util.List;

public interface MonthlyAllowanceRepositoryCustom {
    List<MonthlyAllowance> findByUserSerialNumberAndCreateDate(Long userSerialNumber, Integer year, Integer month, Long csn);

    List<MonthlyAllowance> findByParentsSerialNumberAndChildrenSerialNumberAndStatus(Long userSerialNumber, Long csn, Integer status);
}