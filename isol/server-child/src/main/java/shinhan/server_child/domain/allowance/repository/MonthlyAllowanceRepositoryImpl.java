package shinhan.server_child.domain.allowance.repository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import shinhan.server_child.domain.allowance.entity.MonthlyAllowance;
import shinhan.server_common.domain.entity.TempUser;

import java.util.List;

public class MonthlyAllowanceRepositoryImpl implements MonthlyAllowanceRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    public List<MonthlyAllowance> findByUserSerialNumberAndCreateDate(TempUser tempUser, Integer year, Integer month, Integer csn){
        String jpql = "SELECT m FROM MonthlyAllowance m WHERE m.parents.serialNumber = :serialNumber AND m.child.serialNumber = :csn AND FUNCTION('MONTH', m.createDate) = :month AND FUNCTION('YEAR', m.createDate) = :year";

        return entityManager.createQuery(jpql, MonthlyAllowance.class)
                .setParameter("serialNumber", tempUser.getSerialNumber())
                .setParameter("month", month)
                .setParameter("year", year)
                .getResultList();
    }

}
