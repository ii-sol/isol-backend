package shinhan.server_child.domain.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shinhan.server_child.domain.mission.entity.Mission;

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
}
