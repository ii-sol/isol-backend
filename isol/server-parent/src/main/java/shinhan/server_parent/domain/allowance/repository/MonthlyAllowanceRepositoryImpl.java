package shinhan.server_parent.domain.allowance.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import shinhan.server_parent.domain.allowance.entity.MonthlyAllowance;

import java.util.List;

public class MonthlyAllowanceRepositoryImpl implements MonthlyAllowanceRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MonthlyAllowance> findByUserSerialNumberAndCreateDate(Long userSerialNumber, Integer year, Integer month, Long csn) {
        String jpql = "SELECT m FROM MonthlyAllowance m WHERE m.parents.serialNumber = :serialNumber AND m.child.serialNumber = :csn AND FUNCTION('MONTH', m.createDate) = :month AND FUNCTION('YEAR', m.createDate) = :year";

        return entityManager.createQuery(jpql, MonthlyAllowance.class)
                .setParameter("serialNumber", userSerialNumber)
                .setParameter("csn", csn)
                .setParameter("month", month)
                .setParameter("year", year)
                .getResultList();
    }

    @Override
    public List<MonthlyAllowance> findByParentsSerialNumberAndChildrenSerialNumberAndStatus(Long userSerialNumber, Long csn, Integer status) {
        String jpql = "SELECT m FROM MonthlyAllowance m WHERE m.parents.serialNumber = :serialNumber AND m.child.serialNumber = :csn AND m.status = :status";

        return entityManager.createQuery(jpql, MonthlyAllowance.class)
                .setParameter("serialNumber", userSerialNumber)
                .setParameter("csn", csn)
                .setParameter("status", status)
                .getResultList();
    }
}

