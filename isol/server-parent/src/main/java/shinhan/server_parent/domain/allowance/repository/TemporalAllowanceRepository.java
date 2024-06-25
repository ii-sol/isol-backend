package shinhan.server_parent.domain.allowance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shinhan.server_parent.domain.allowance.entity.TemporalAllowance;

import java.util.List;

public interface TemporalAllowanceRepository extends JpaRepository<TemporalAllowance, Integer>, TemporalAllowanceRepositoryCustom {
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