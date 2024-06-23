package shinhan.server_child.domain.mission.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_child.domain.mission.dto.MissionFindOneResponse;
import shinhan.server_child.domain.mission.entity.Mission;
import shinhan.server_child.domain.mission.repository.MissionRepository;

import java.util.NoSuchElementException;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(transactionManager = "missionTransactionManager")
public class MissionService {

    private final MissionRepository missionRepository;

    public MissionFindOneResponse getMission(int id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("미션이 존재하지 않습니다."));

        return mission.convertToMissionFindOneResponse();
    }

}
