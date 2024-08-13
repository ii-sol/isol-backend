package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shinhan.server_child.domain.mission.service.MissionService;
import shinhan.server_common.domain.mission.entity.Mission;
import shinhan.server_common.domain.mission.repository.MissionRepository;

@ExtendWith(MockitoExtension.class)
class MissionServiceTest {

    @Mock
    private MissionRepository missionRepository;

    @InjectMocks
    private MissionService missionService;

    private long parentsSn, childSn;
    private Mission mission1, mission2, mission3, mission4, mission5, mission6;

    @BeforeEach
    void setUp() {
        parentsSn = 1000000000L;
        childSn = 1000000001L;

        mission1 = Mission.builder().id(1).childSn(childSn).parentsSn(parentsSn).content("status 1")
            .status(1).build();
        mission2 = Mission.builder().id(2).childSn(childSn).parentsSn(parentsSn).content("status 2")
            .status(2).build();
        mission3 = Mission.builder().id(3).childSn(childSn).parentsSn(parentsSn).content("status 3")
            .status(3).build();
        mission4 = Mission.builder().id(4).childSn(childSn).parentsSn(parentsSn).content("status 4")
            .status(4).build();
        mission5 = Mission.builder().id(5).childSn(childSn).parentsSn(parentsSn).content("status 5")
            .status(5).build();
        mission6 = Mission.builder().id(6).childSn(childSn).parentsSn(parentsSn).content("status 6")
            .status(6).build();

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
}