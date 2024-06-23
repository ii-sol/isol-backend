package shinhan.server_child.domain.mission.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_child.domain.mission.dto.MissionAnswerSaveRequest;
import shinhan.server_child.domain.mission.dto.MissionFindOneResponse;
import shinhan.server_child.domain.mission.dto.MissionSaveRequest;
import shinhan.server_child.domain.mission.entity.Mission;
import shinhan.server_child.domain.mission.repository.MissionRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    public List<MissionFindOneResponse> getMissions(long childSn, long parentsSn, int s1, int s2) {
        List<Mission> missions = missionRepository.findMissions(childSn, parentsSn, s1, s2);
        return missions.stream()
                .map(Mission::convertToMissionFindOneResponse)
                .collect(Collectors.toList());
    }

    public List<MissionFindOneResponse> getMissions(long childSn, long parentsSn, int s) {
        List<Mission> missions = missionRepository.findMissions(childSn, parentsSn, s);
        return missions.stream()
                .map(Mission::convertToMissionFindOneResponse)
                .collect(Collectors.toList());
    }

    public List<MissionFindOneResponse> getMissionsHistory(long childSn, long parentsSn, int year, int month, Integer status) {
        List<Mission> missions = null;

        if (status == null) {
            missions = missionRepository.findMissionsHistory(childSn, parentsSn, year, month);
        } else {
            missions = missionRepository.findMissionsHistory(childSn, parentsSn, year, month, status);
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

        Mission createdMission = missionRepository.save(mission);

        Mission savedMission = missionRepository.findById(createdMission.getId())
                .orElseThrow(() -> new RuntimeException("미션이 생성되지 않았습니다."));

        return savedMission.convertToMissionFindOneResponse();
    }

    public MissionFindOneResponse updateMission(MissionAnswerSaveRequest missionAnswerSaveRequest) {
        Mission mission = missionRepository.findById(missionAnswerSaveRequest.getId())
                .orElseThrow(() -> new NoSuchElementException("미션이 존재하지 않습니다."));

        if (missionAnswerSaveRequest.isAnswer()) {
            if (mission.getStatus() == 3) {
                mission.setStatus(6);
                missionRepository.save(mission).convertToMissionFindOneResponse();
            }
        } else if (mission.getStatus() == 2 || mission.getStatus() == 3) {
            mission.setStatus(5);
            return missionRepository.save(mission).convertToMissionFindOneResponse();
        }

        throw new RuntimeException("미션 응답에 대해 반영되지 않았습니다.");
    }
}
