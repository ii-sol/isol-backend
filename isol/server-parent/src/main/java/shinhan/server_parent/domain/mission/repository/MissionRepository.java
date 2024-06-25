package shinhan.server_parent.domain.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import shinhan.server_common.domain.mission.entity.Mission;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Integer> {

    @Query("SELECT m " +
            "FROM Mission m " +
            "WHERE 1 = 1 " +
            "AND m.childSn = :childSn " +
            "AND m.parentsSn = :parentsSn " +
            "AND (m.status = :s1 OR m.status = :s2) " +
            "ORDER BY m.id")
    List<Mission> findMissions(@Param("childSn") long childSn, @Param("parentsSn") long parentsSn, @Param("s1") int s1, @Param("s2") int s2);

    @Query("SELECT m " +
            "FROM Mission m " +
            "WHERE 1 = 1 " +
            "AND m.childSn = :childSn " +
            "AND m.parentsSn = :parentsSn " +
            "AND m.status = :s " +
            "ORDER BY m.id")
    List<Mission> findMissions(@Param("childSn") long childSn, @Param("parentsSn") long parentsSn, @Param("s") int s);

    @Query("SELECT m " +
            "FROM Mission m " +
            "WHERE 1 = 1 " +
            "AND m.childSn = :childSn " +
            "AND m.parentsSn = :parentsSn " +
            "AND m.status IN (4, 5) " +
            "AND FUNCTION('YEAR', m.completeDate) = :year " +
            "AND FUNCTION('MONTH', m.completeDate) = :month " +
            "ORDER BY m.completeDate DESC")
    List<Mission> findMissionsHistory(@Param("childSn") long childSn, @Param("parentsSn") long parentsSn, @Param("year") int year, @Param("month") int month);

    @Query("SELECT m " +
            "FROM Mission m " +
            "WHERE 1 = 1 " +
            "AND m.childSn = :childSn " +
            "AND m.parentsSn = :parentsSn " +
            "AND m.status = :status " +
            "AND FUNCTION('YEAR', m.completeDate) = :year " +
            "AND FUNCTION('MONTH', m.completeDate) = :month " +
            "ORDER BY m.completeDate DESC")
    List<Mission> findMissionsHistory(@Param("childSn") long childSn, @Param("parentsSn") long parentsSn, @Param("year") int year, @Param("month") int month, @Param("status") int status);

    @Procedure(name = "update_mission_expiration")
    void updateMissionExpiration();
}
