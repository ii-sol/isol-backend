package shinhan.server_child.domain.mission.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import shinhan.server_common.domain.mission.dto.MissionAnswerSaveRequest;
import shinhan.server_common.domain.mission.dto.MissionFindOneResponse;
import shinhan.server_common.domain.mission.dto.MissionSaveRequest;
import shinhan.server_common.domain.mission.entity.Mission;
import shinhan.server_common.domain.mission.repository.MissionRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class MissionService {

    private final MissionRepository missionRepository;

    public MissionFindOneResponse getMission(int id) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("미션이 존재하지 않습니다."));

        return mission.convertToMissionFindOneResponse();
    }

    public List<MissionFindOneResponse> getMissions(long childSn, int s1, int s2) {
        List<Mission> missions = missionRepository.findChildMissions(childSn, s1, s2);
        return missions.stream()
                .map(Mission::convertToMissionFindOneResponse)
                .collect(Collectors.toList());
    }

    public List<MissionFindOneResponse> getMissions(long childSn, int s) {
        List<Mission> missions = missionRepository.findChildMissions(childSn, s);
        return missions.stream()
                .map(Mission::convertToMissionFindOneResponse)
                .collect(Collectors.toList());
    }

    public List<MissionFindOneResponse> getMissionsHistory(long childSn, int year, int month, Integer status) {
        List<Mission> missions = null;

        if (status == null) {
            missions = missionRepository.findChildMissionsHistory(childSn, year, month);
        } else {
            missions = missionRepository.findChildMissionsHistory(childSn, year, month, status);
        }

        return missions.stream()
                .map(Mission::convertToMissionFindOneResponse)
                .collect(Collectors.toList());
    }

    public MissionFindOneResponse createMission(MissionSaveRequest missionSaveRequest) {

        Mission mission = Mission.builder()
                .childSn(missionSaveRequest.getChildSn())
                .parentsSn(missionSaveRequest.getParentsSn())
                .content(missionSaveRequest.getContent())
                .price(missionSaveRequest.getPrice())
                .createDate(Timestamp.valueOf(LocalDateTime.now()))
                .dueDate(missionSaveRequest.getDueDate())
                .status(1)
                .build();

        return missionRepository.save(mission).convertToMissionFindOneResponse();
    }

    public MissionFindOneResponse updateMission(MissionAnswerSaveRequest missionAnswerSaveRequest) throws BadRequestException {
        Mission mission = missionRepository.findById(missionAnswerSaveRequest.getId())
                .orElseThrow(() -> new NoSuchElementException("미션이 존재하지 않습니다."));

        if (missionAnswerSaveRequest.isAnswer()) {
            if (mission.getStatus() == 2) {
                mission.setStatus(3);
                return missionRepository.save(mission).convertToMissionFindOneResponse();
            } else if (mission.getStatus() == 3) {
                mission.setStatus(6);
                return missionRepository.save(mission).convertToMissionFindOneResponse();
            }
        } else if (mission.getStatus() == 2 || mission.getStatus() == 3) {
            mission.setStatus(5);
            return missionRepository.save(mission).convertToMissionFindOneResponse();
        }

        throw new BadRequestException("잘못된 사용자 요청입니다.");
    }
}
