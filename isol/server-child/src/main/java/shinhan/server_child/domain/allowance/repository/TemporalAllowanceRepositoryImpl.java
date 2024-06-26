package shinhan.server_child.domain.allowance.repository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import shinhan.server_child.domain.allowance.entity.TemporalAllowance;

import java.util.List;

public class TemporalAllowanceRepositoryImpl implements TemporalAllowanceRepositoryCustom {
    @Autowired
    EntityManager entityManager;

//    public List<ChildTemporalAllowance> findByUserSerialNumberAndCreateDate(Long userSerialNumber, Integer year, Integer month, Integer csn){
//        String jpql = "SELECT m FROM ChildTemporalAllowance t WHERE t.parents.serialNumber = :serialNumber AND t.child.serialNumber = :csn AND FUNCTION('MONTH', t.createDate) = :month AND FUNCTION('YEAR', t.createDate) = :year";
//
//        return entityManager.createQuery(jpql, ChildTemporalAllowance.class)
//                .setParameter("serialNumber", userSerialNumber)
//                .setParameter("csn", csn)
//                .setParameter("month", month)
//                .setParameter("year", year)
//                .getResultList();
//    }

//    public List<TemporalAllowance> findByChildSerialNumberAndCreateDateAndStatus(Long userSerialNumber, Integer year, Integer month){
//        String jpql = "select t from TemporalAllowance t where t.childSerialNumber = :serialNumber and t.status IN (4, 5, 6) and FUNCTION('MONTH', t.createDate) = :month AND FUNCTION('YEAR', t.createDate) = :year";
//
//        return entityManager.createQuery(jpql, TemporalAllowance.class)
//                .setParameter("serialNumber", userSerialNumber)
//                .setParameter("month", month)
//                .setParameter("year", year)
//                .getResultList();
//    }
}
