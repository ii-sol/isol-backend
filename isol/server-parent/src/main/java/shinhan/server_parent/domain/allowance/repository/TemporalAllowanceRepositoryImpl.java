package shinhan.server_parent.domain.allowance.repository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import shinhan.server_parent.domain.allowance.entity.TemporalAllowance;

import java.util.List;

public class TemporalAllowanceRepositoryImpl implements TemporalAllowanceRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    public List<TemporalAllowance> findByUserSerialNumberAndCreateDate(Long userSerialNumber, Integer year, Integer month, Long csn){
        String jpql = "SELECT m FROM TemporalAllowance m WHERE m.parentsSerialNumber = :serialNumber AND m.childSerialNumber = :csn AND FUNCTION('MONTH', m.createDate) = :month AND FUNCTION('YEAR', m.createDate) = :year";

        return entityManager.createQuery(jpql, TemporalAllowance.class)
                .setParameter("serialNumber", userSerialNumber)
                .setParameter("csn", csn)
                .setParameter("month", month)
                .setParameter("year", year)
                .getResultList();
    }

    @Override
    public List<TemporalAllowance> findByParentsSerialNumberAndChildrenSerialNumberAndStatus(Long userSerialNumber, Long csn, Integer status) {
        String jpql = "SELECT m FROM TemporalAllowance m WHERE m.parentsSerialNumber = :serialNumber AND m.childSerialNumber = :csn AND m.status = :status";

        return entityManager.createQuery(jpql, TemporalAllowance.class)
                .setParameter("serialNumber", userSerialNumber)
                .setParameter("csn", csn)
                .setParameter("status", status)
                .getResultList();
    }

}
