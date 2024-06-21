package shinhan.server_child.domain.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_child.domain.mission.entity.Mission;

public interface MissionRepository extends JpaRepository<Mission, Integer> {
}
