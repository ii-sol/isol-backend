package shinhan.server_common.domain.allowance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shinhan.server_common.domain.allowance.entity.TemporalAllowance;

import java.util.List;

public interface TemporalAllowanceRepository extends JpaRepository<TemporalAllowance, Integer>{

    List<TemporalAllowance> findByChildSerialNumberAndStatus(Long userSerialNumber, Integer status);

    @Query("SELECT t FROM TemporalAllowance t " +
            "WHERE t.childSerialNumber = :serialNumber " +
            "AND t.status IN (4, 5, 6) " +
            "AND FUNCTION('MONTH', t.createDate) = :month " +
            "AND FUNCTION('YEAR', t.createDate) = :year")
    List<TemporalAllowance> findByChildSerialNumberAndCreateDateAndStatus(
            @Param("serialNumber") Long serialNumber,
            @Param("year") Integer year,
            @Param("month") Integer month);

    @Query("SELECT m FROM TemporalAllowance m " +
            "WHERE m.parentsSerialNumber = :serialNumber " +
            "AND m.childSerialNumber = :csn " +
            "AND FUNCTION('MONTH', m.createDate) = :month " +
            "AND FUNCTION('YEAR', m.createDate) = :year")
    List<TemporalAllowance> findByUserSerialNumberAndCreateDate(
            @Param("serialNumber") Long serialNumber,
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("csn") Long csn);

    @Query("SELECT m FROM TemporalAllowance m " +
            "WHERE m.parentsSerialNumber = :serialNumber " +
            "AND m.childSerialNumber = :csn " +
            "AND m.status = :status")
    List<TemporalAllowance> findByParentsSerialNumberAndChildrenSerialNumberAndStatus(
            @Param("serialNumber") Long serialNumber,
            @Param("csn") Long csn,
            @Param("status") Integer status);
}
