package shinhan.server_child.domain.allowance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shinhan.server_child.domain.allowance.entity.TemporalAllowance;

import java.util.List;

public interface TemporalAllowanceRepository extends JpaRepository<TemporalAllowance, Integer>, TemporalAllowanceRepositoryCustom {

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
}