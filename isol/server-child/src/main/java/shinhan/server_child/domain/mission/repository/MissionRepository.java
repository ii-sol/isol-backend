package shinhan.server_child.domain.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shinhan.server_child.domain.mission.dto.MissionFindOneResponse;
import shinhan.server_child.domain.mission.entity.Mission;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Integer> {

    @Query("SELECT * " +
            "FROM Mission " +
            "WHERE status = :s1 " +
            "OR status = s2 " +
            "ORDER BY id")
    List<MissionFindOneResponse> findOngoingMissions(@Param("s1") int s1, @Param("s2") int s2);
}
