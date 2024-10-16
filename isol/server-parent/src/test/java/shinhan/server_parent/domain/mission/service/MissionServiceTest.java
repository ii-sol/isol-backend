package shinhan.server_parent.domain.mission.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shinhan.server_common.domain.mission.dto.MissionAnswerSaveRequest;
import shinhan.server_common.domain.mission.dto.MissionFindOneResponse;
import shinhan.server_common.domain.mission.dto.MissionSaveRequest;
import shinhan.server_common.domain.mission.entity.Mission;
import shinhan.server_common.domain.mission.repository.MissionRepository;
import shinhan.server_common.domain.user.entity.Child;
import shinhan.server_common.global.utils.user.UserUtils;
import shinhan.server_parent.domain.mission.service.MissionService;

@ExtendWith(MockitoExtension.class)
class MissionServiceTest {

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private MissionService missionService;

    private long parentsSn, childSn;
    private Mission mission1, mission2, mission3, mission4, mission5, mission6;

    @BeforeEach
    void setUp() {
        parentsSn = 1000000000L;
        childSn = 1000000001L;

        mission1 = Mission.builder().id(1).childSn(childSn).parentsSn(parentsSn).content("status 1")
            .price(1000).status(1).build();
        mission2 = Mission.builder().id(2).childSn(childSn).parentsSn(parentsSn).content("status 2")
            .price(1000).status(2).build();
        mission3 = Mission.builder().id(3).childSn(childSn).parentsSn(parentsSn).content("status 3")
            .price(1000).status(3).build();
        mission4 = Mission.builder().id(4).childSn(childSn).parentsSn(parentsSn).content("status 4")
            .price(1000).status(4).build();
        mission5 = Mission.builder().id(5).childSn(childSn).parentsSn(parentsSn).content("status 5")
            .price(1000).status(5).build();
        mission6 = Mission.builder().id(6).childSn(childSn).parentsSn(parentsSn).content("status 6")
            .price(1000).status(6).build();

        lenient().when(missionRepository.findById(1)).thenReturn(Optional.of(mission1));
        lenient().when(missionRepository.findById(2)).thenReturn(Optional.of(mission2));
        lenient().when(missionRepository.findById(3)).thenReturn(Optional.of(mission3));
        lenient().when(missionRepository.findById(4)).thenReturn(Optional.of(mission4));
        lenient().when(missionRepository.findById(5)).thenReturn(Optional.of(mission5));
        lenient().when(missionRepository.findById(6)).thenReturn(Optional.of(mission6));
    }

    @Test
    public void contextLoads() {
        assertThat(missionService).isNotNull();
    }

    @Test
    void getMission_WhenMissionExists_ShouldReturnMissionFindOneResponse() {
        MissionFindOneResponse result = missionService.getMission(1);

        assertThat(result.getId()).isEqualTo(mission1.getId());
    }

    @Test
    void getMission_WhenMissionDoesNotExists_ShouldThrowNoSuchElementException() {
        when(missionRepository.findById(100)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> missionService.getMission(100))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("미션이 존재하지 않습니다.");
    }

    @Test
    void first_getMissions_WhenMissionsExists_ShouldReturnList() {
        when(missionRepository.findParentsMissions(childSn, parentsSn, 1, 2)).thenReturn(
            List.of(mission1, mission2));

        List<MissionFindOneResponse> result = missionService.getMissions(childSn, parentsSn, 1, 2);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(mission1.getId());
        assertThat(result.get(1).getId()).isEqualTo(mission2.getId());
    }

    @Test
    void first_getMissions_WhenMissionsDoseNotExists_ShouldReturnList() {
        when(missionRepository.findParentsMissions(childSn, parentsSn, 1, 2)).thenReturn(
            Collections.emptyList());

        List<MissionFindOneResponse> result = missionService.getMissions(childSn, parentsSn, 1, 2);

        assertThat(result).hasSize(0);
    }

    @Test
    void second_getMissions_WhenMissionsExists_ShouldReturnList() {
        when(missionRepository.findParentsMissions(childSn, parentsSn, 1)).thenReturn(
            List.of(mission1));

        List<MissionFindOneResponse> result = missionService.getMissions(childSn, parentsSn, 1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(mission1.getId());
    }

    @Test
    void second_getMissions_WhenMissionsDoseNotExists_ShouldReturnList() {
        when(missionRepository.findParentsMissions(childSn, parentsSn, 1)).thenReturn(
            Collections.emptyList());

        List<MissionFindOneResponse> result = missionService.getMissions(childSn, parentsSn, 1);

        assertThat(result).hasSize(0);
    }

    @Test
    void getMissionsHistory_WhenStatusIsNull_ShouldReturnList() {
        when(missionRepository.findParentsMissionsHistory(childSn, parentsSn, 2024, 8)).thenReturn(
            List.of(mission4, mission5));

        List<MissionFindOneResponse> result = missionService.getMissionsHistory(childSn, parentsSn,
            2024, 8, null);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(mission4.getId());
        assertThat(result.get(1).getId()).isEqualTo(mission5.getId());
    }

    @Test
    void getMissionsHistory_WhenStatusExists_ShouldReturnList() {
        when(missionRepository.findParentsMissionsHistory(childSn, parentsSn, 2024, 8,
            4)).thenReturn(
            List.of(mission4));

        List<MissionFindOneResponse> result = missionService.getMissionsHistory(childSn, parentsSn,
            2024, 8, 4);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(mission4.getId());
    }

    @Test
    void getMissionsHistory_WhenHistoryDoesNotExists_ShouldReturnList() {
        when(missionRepository.findParentsMissionsHistory(childSn, parentsSn, 2024, 8)).thenReturn(
            Collections.emptyList());

        List<MissionFindOneResponse> result = missionService.getMissionsHistory(childSn, parentsSn,
            2024, 8, null);

        assertThat(result).hasSize(0);
    }

    @Test
    void createMission_ShouldCreateAndReturnMissionFindOneResponse() {
        MissionSaveRequest missionSaveRequest = MissionSaveRequest.builder()
            .childSn(childSn)
            .parentsSn(parentsSn)
            .content("status 2")
            .price(1000)
            .build();

        when(missionRepository.save(any(Mission.class))).thenReturn(mission2);

        MissionFindOneResponse result = missionService.createMission(missionSaveRequest);

        assertThat(result.getId()).isEqualTo(mission2.getId());
        assertThat(result.getStatus()).isEqualTo(mission2.getStatus());
    }

    @Test
    void updateMission_WhenAnswerIsTrueAndStatusIs1_ShouldReturnStatusIs3()
        throws BadRequestException {
        MissionAnswerSaveRequest missionAnswerSaveRequest = MissionAnswerSaveRequest.builder()
            .id(1)
            .childSn(childSn)
            .parentsSn(parentsSn)
            .answer(true)
            .build();

        when(missionRepository.save(mission1)).thenReturn(mission1);

        MissionFindOneResponse result = missionService.updateMission(missionAnswerSaveRequest);

        assertThat(result.getStatus()).isEqualTo(3);
    }

    @Test
    void updateMission_WhenAnswerIsTrueAndStatusIs6_ShouldReturnStatusIs4()
        throws BadRequestException {
        MissionAnswerSaveRequest missionAnswerSaveRequest = MissionAnswerSaveRequest.builder()
            .id(6)
            .childSn(childSn)
            .parentsSn(parentsSn)
            .answer(true)
            .build();

        when(missionRepository.save(mission6)).thenReturn(mission6);

        MissionFindOneResponse result = missionService.updateMission(missionAnswerSaveRequest);

        assertThat(result.getStatus()).isEqualTo(4);
    }

    @Test
    void updateMission_WhenAnswerIsFalseAndStatusIs1_ShouldReturnStatusIs5()
        throws BadRequestException {
        MissionAnswerSaveRequest missionAnswerSaveRequest = MissionAnswerSaveRequest.builder()
            .id(1)
            .childSn(childSn)
            .parentsSn(parentsSn)
            .answer(false)
            .build();

        when(missionRepository.save(mission1)).thenReturn(mission1);

        MissionFindOneResponse result = missionService.updateMission(missionAnswerSaveRequest);

        assertThat(result.getStatus()).isEqualTo(5);
    }

    @Test
    void updateMission_WhenAnswerIsFalseAndStatusIs6_ShouldReturnStatusIs3()
        throws BadRequestException {
        MissionAnswerSaveRequest missionAnswerSaveRequest = MissionAnswerSaveRequest.builder()
            .id(6)
            .childSn(childSn)
            .parentsSn(parentsSn)
            .answer(false)
            .build();

        when(missionRepository.save(mission6)).thenReturn(mission6);

        MissionFindOneResponse result = missionService.updateMission(missionAnswerSaveRequest);

        assertThat(result.getStatus()).isEqualTo(3);
    }

    @Test
    void updateMission_WhenMissionMissionDoesNotExists_ShouldThrowNoSuchElementException() {
        MissionAnswerSaveRequest missionAnswerSaveRequest = MissionAnswerSaveRequest.builder()
            .id(100)
            .childSn(childSn)
            .parentsSn(parentsSn)
            .answer(true)
            .build();

        assertThatThrownBy(() -> missionService.updateMission(missionAnswerSaveRequest))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("미션이 존재하지 않습니다.");
    }

    @Test
    void updateMission_WhenMissionAnswerSaveRequestIsInvalid_ShouldThrowBadRequestException() {
        MissionAnswerSaveRequest missionAnswerSaveRequest = MissionAnswerSaveRequest.builder()
            .id(2)
            .childSn(childSn)
            .parentsSn(parentsSn)
            .answer(true)
            .build();

        assertThatThrownBy(() -> missionService.updateMission(missionAnswerSaveRequest))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("잘못된 사용자 요청입니다.");
    }
}