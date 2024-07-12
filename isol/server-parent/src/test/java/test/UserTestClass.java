package test;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shinhan.ServerParentApplication;
import shinhan.server_common.domain.user.dto.ParentsFindOneResponse;
import shinhan.server_common.domain.user.entity.Parents;
import shinhan.server_common.domain.user.repository.ParentsRepository;
import shinhan.server_parent.domain.user.service.UserService;

import java.util.Optional;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ServerParentApplication.class)
public class UserTestClass {

    @Mock
    private ParentsRepository parentsRepository;

    @InjectMocks
    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {
        assertThat(userService).isNotNull();
    }

    @Test
    public void getParents_WhenUserExists_ShouldReturnParentsFindOneResponse() {
        long sn = 1000000000;
        Parents mockParents = new Parents();
        when(parentsRepository.findBySerialNum(sn)).thenReturn(Optional.of(mockParents));

        ParentsFindOneResponse result = userService.getParents(sn);
        assertThat(result).isEqualTo(mockParents.convertToUserFindOneResponse());
    }

    @Test
    public void getParents_WhenUserDoesNotExist_ShouldThrowNoSuchElementException() {
        long sn = 1000000000;
        when(parentsRepository.findBySerialNum(sn)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getParents(sn))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("사용자가 존재하지 않습니다.");
    }
}
