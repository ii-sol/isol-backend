package shinhan.server_child.domain.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import shinhan.server_child.domain.mission.dto.MissionFindOneResponse;
import shinhan.server_child.domain.mission.entity.Mission;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Integer> {

    @Query("SELECT * " +
            "FROM Mission " +
            "WHERE 1 = 1" +
            "AND (status = :s1 OR status = :s2)" +
            "AND child_sn = :child_sn " +
            "AND parents_sn = :parents_sn " +
            "ORDER BY id")
    List<MissionFindOneResponse> findOngoingMissions(
            @Param("child_sn") long childSn,
            @Param("parents_sn") long parentsSn,
            @Param("s1") int s1,
            @Param("s2") int s2);
}
