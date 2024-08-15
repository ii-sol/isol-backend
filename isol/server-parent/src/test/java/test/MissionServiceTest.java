package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shinhan.server_common.domain.mission.dto.MissionFindOneResponse;
import shinhan.server_common.domain.mission.entity.Mission;
import shinhan.server_common.domain.mission.repository.MissionRepository;
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
}