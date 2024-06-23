package shinhan.server_child.domain.mission.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_child.domain.mission.dto.MissionFindOneResponse;
import shinhan.server_child.domain.mission.entity.Mission;
import shinhan.server_child.domain.mission.repository.MissionRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

    public List<MissionFindOneResponse> getOngoingMissions(long childSn, long parentsSn, int s1, int s2) {
        List<Mission> missions = missionRepository.findOngoingMissions(childSn, parentsSn, s1, s2);
        return missions.stream()
                .map(Mission::convertToMissionFindOneResponse)
                .collect(Collectors.toList());
    }
}
