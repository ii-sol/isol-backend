package shinhan.server_child.domain.allowance.repository;

import shinhan.server_child.domain.allowance.entity.MonthlyAllowance;
import shinhan.server_common.domain.entity.TempUser;

import java.util.List;

public interface MonthlyAllowanceRepositoryCustom {
    public List<MonthlyAllowance> findByUserSerialNumberAndCreateDate(TempUser tempUser, Integer year, Integer month, Integer csn);
}
