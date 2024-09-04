package shinhan.server_child.domain.user.service;

import java.sql.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import shinhan.server_child.domain.user.service.UserService;
import shinhan.server_common.domain.user.dto.*;
import shinhan.server_common.domain.user.entity.Child;
import shinhan.server_common.domain.user.entity.ChildManage;
import shinhan.server_common.domain.user.entity.Family;
import shinhan.server_common.domain.user.entity.Parents;
import shinhan.server_common.domain.user.repository.*;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.dto.FamilyInfoResponse;

import java.util.Optional;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ParentsRepository parentsRepository;

    @Mock
    private ChildRepository childRepository;

    @Mock
    private FamilyRepository familyRepository;

    @Mock
    private ChildManageRepository childManageRepository;

    @InjectMocks
    private UserService userService;

    private long parentsSn, childSn;
    private Parents mockParents;
    private Child mockChild;

    @BeforeEach
    void setUp() {
        parentsSn = 1000000000L;
        mockParents = Parents.builder()
            .serialNum(parentsSn)
            .name("부모")
            .phoneNum("010-0000-0000")
            .birthDate(Date.valueOf("1990-01-01"))
            .accountInfo("$2a$10$D9yY91eRRO.GH9j2Ll1UPe/AXgYQYqv9UNuBlgNYL5.5dGJ19j06e")
            .profileId(1)
            .build();
        lenient().when(parentsRepository.findBySerialNum(parentsSn))
            .thenReturn(Optional.of(mockParents));

        childSn = 1000000001L;
        mockChild = Child.builder()
            .serialNum(childSn)
            .name("아이")
            .phoneNum("010-0000-0000")
            .birthDate(Date.valueOf("2010-01-01"))
            .profileId(1)
            .accountInfo("$2a$10$D9yY91eRRO.GH9j2Ll1UPe/AXgYQYqv9UNuBlgNYL5.5dGJ19j06e")
            .score(50)
            .build();
        lenient().when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.of(mockChild));
    }

    @Test
    public void contextLoads() {
        assertThat(userService).isNotNull();
    }

    @Test
    public void getChild_WhenUserExists_ShouldReturnChildFindOneResponse() {
        ChildFindOneResponse result = userService.getChild(childSn);

        assertThat(result.getSn()).isEqualTo(mockChild.getSerialNum());
        assertThat(result.getName()).isEqualTo(mockChild.getName());
        assertThat(result.getPhoneNum()).isEqualTo(mockChild.getPhoneNum());
        assertThat(result.getBirthDate()).isEqualTo(mockChild.getBirthDate());
        assertThat(result.getProfileId()).isEqualTo(mockChild.getProfileId());
    }

    @Test
    public void getChild_WhenUserDoesNotExist_ShouldThrowNoSuchElementException() {
        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getChild(childSn))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("사용자가 존재하지 않습니다.");
    }

    @Test
    public void getParents_WhenUserExists_ShouldReturnParentsFindOneResponse() {
        ParentsFindOneResponse result = userService.getParents(parentsSn);

        assertThat(result.getSn()).isEqualTo(mockParents.getSerialNum());
        assertThat(result.getName()).isEqualTo(mockParents.getName());
        assertThat(result.getPhoneNum()).isEqualTo(mockParents.getPhoneNum());
        assertThat(result.getBirthDate()).isEqualTo(mockParents.getBirthDate());
        assertThat(result.getProfileId()).isEqualTo(mockParents.getProfileId());
    }

    @Test
    public void getParents_WhenUserDoesNotExist_ShouldThrowNoSuchElementException() {
        when(parentsRepository.findBySerialNum(parentsSn)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getParents(parentsSn))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("부모 사용자가 존재하지 않습니다.");
    }

    @Test
    public void updateUser_WhenUserExists_ShouldUpdateAndReturnParentsFindOneResponse() {
        UserUpdateRequest updateRequest = new UserUpdateRequest(
            "010-1234-5678",
            "홍길동",
            Date.valueOf("2010-01-01"),
            2
        );
        updateRequest.setSerialNum(childSn);

        Child updatedChild = Child.builder()
            .serialNum(childSn)
            .name("홍길동")
            .phoneNum("010-1234-5678")
            .birthDate(Date.valueOf("2010-01-01"))
            .profileId(2)
            .build();
        when(childRepository.findByPhoneNum("010-1234-5678")).thenReturn(Optional.empty());
        when(childRepository.save(mockChild)).thenReturn(updatedChild);

        ChildFindOneResponse result = userService.updateUser(updateRequest);

        assertThat(result.getSn()).isEqualTo(mockChild.getSerialNum());
        assertThat(result.getName()).isEqualTo(mockChild.getName());
        assertThat(result.getPhoneNum()).isEqualTo(mockChild.getPhoneNum());
        assertThat(result.getBirthDate()).isEqualTo(mockChild.getBirthDate());
        assertThat(result.getProfileId()).isEqualTo(mockChild.getProfileId());
    }

    @Test
    public void updateUser_WhenPhoneNumExists_ShouldThrowInternalError() {
        UserUpdateRequest updateRequest = new UserUpdateRequest(
            "010-1234-5678",
            "홍길동",
            Date.valueOf("1990-01-01"),
            2
        );
        updateRequest.setSerialNum(childSn);
        when(childRepository.findByPhoneNum("010-1234-5678")).thenReturn(
            Optional.of(new Child()));

        assertThatThrownBy(() -> userService.updateUser(updateRequest))
            .isInstanceOf(InternalError.class)
            .hasMessage("이미 가입된 전화번호입니다.");
    }

    @Test
    public void connectFamily_ShouldCreateAndReturnInt() {
        FamilySaveRequest request = new FamilySaveRequest(
            childSn, mockParents.getPhoneNum(), "아빠");

        Family family = Family.builder()
            .id(1)
            .child(mockChild)
            .parents(mockParents)
            .parentsAlias("Dad")
            .build();

        when(childRepository.findBySerialNum(request.getSn())).thenReturn(Optional.of(mockChild));
        when(parentsRepository.findByPhoneNum(request.getPhoneNum())).thenReturn(
            Optional.of(mockParents));
        when(familyRepository.save(any(Family.class))).thenReturn(family);

        int familyId = userService.connectFamily(request);

        assertThat(familyId).isEqualTo(family.getId());
    }

    @Test
    public void connectFamily_WhenChildDoesNotExists_ShouldThrowNoSuchElementException() {
        FamilySaveRequest request = new FamilySaveRequest(
            childSn, mockParents.getPhoneNum(), "아빠");

        when(childRepository.findBySerialNum(request.getSn())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.connectFamily(request))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("사용자가 존재하지 않습니다.");
    }

    @Test
    public void connectFamily_WhenParentsDoesNotExists_ShouldThrowNoSuchElementException() {
        FamilySaveRequest request = new FamilySaveRequest(
            childSn, mockParents.getPhoneNum(), "아빠");

        when(parentsRepository.findByPhoneNum(request.getPhoneNum())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.connectFamily(request))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("부모 사용자가 존재하지 않습니다.");
    }

    @Test
    public void disconnectFamily_WhenFamilyExists_ShouldDeleteFamily() {
        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.of(mockChild));
        when(parentsRepository.findBySerialNum(parentsSn)).thenReturn(Optional.of(mockParents));

        Family mockFamily = new Family();
        when(familyRepository.findByChildAndParents(mockChild, mockParents)).thenReturn(
            Optional.of(mockFamily));

        doNothing().when(familyRepository).delete(mockFamily.getId());

        userService.disconnectFamily(childSn, parentsSn);

        verify(familyRepository, times(1)).delete(mockFamily.getId());
    }

    @Test
    public void disconnectFamily_WhenChildDoesNotExist_ShouldThrowNoSuchElementException() {
        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.of(mockChild));
        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.disconnectFamily(childSn, parentsSn))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("사용자가 존재하지 않습니다.");
    }

    @Test
    public void disconnectFamily_WhenParentsDoesNotExist_ShouldThrowNoSuchElementException() {
        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.of(mockChild));
        when(parentsRepository.findBySerialNum(parentsSn)).thenReturn(Optional.of(mockParents));
        when(parentsRepository.findBySerialNum(parentsSn)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.disconnectFamily(childSn, parentsSn))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("부모 사용자가 존재하지 않습니다.");
    }

    @Test
    public void disconnectFamily_WhenFamilyDoesNotExist_ShouldThrowNoSuchElementException() {
        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.of(mockChild));
        when(parentsRepository.findBySerialNum(parentsSn)).thenReturn(Optional.of(mockParents));
        when(familyRepository.findByChildAndParents(mockChild, mockParents)).thenReturn(
            Optional.empty());

        assertThatThrownBy(() -> userService.disconnectFamily(childSn, parentsSn))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("가족 관계가 존재하지 않습니다.");
    }

    @Test
    public void isFamily_FamilyExists_ShouldReturnTrue() {
        int familyId = 1;
        when(familyRepository.findById(familyId)).thenReturn(Optional.of(new Family()));

        boolean result = userService.isFamily(familyId);

        assertThat(result).isTrue();
    }

    @Test
    public void isFamily_FamilyDoseNotExists_ShouldReturnFalse() {
        int familyId = 1;
        when(familyRepository.findById(familyId)).thenReturn(Optional.empty());

        boolean result = userService.isFamily(familyId);

        assertThat(result).isFalse();
    }

    @Test
    public void getChildManage_WhenChildManageExists_ShouldReturnChildManageFindOneResponse() {
        ChildManage mockChildManage = ChildManage.builder().child(mockChild).build();
        when(childManageRepository.findByChild(mockChild)).thenReturn(Optional.of(mockChildManage));

        ChildManageFindOneResponse result = userService.getChildManage(childSn);

        assertThat(result.getBaseRate()).isEqualTo(mockChildManage.getBaseRate());
        assertThat(result.getInvestLimit()).isEqualTo(mockChildManage.getInvestLimit());
        assertThat(result.getLoanLimit()).isEqualTo(mockChildManage.getLoanLimit());
    }

    @Test
    public void getChildManage_WhenChildDoesNotExist_ShouldThrowNoSuchElementException() {
        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getChildManage(childSn))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("아이 사용자가 존재하지 않습니다.");
    }

    @Test
    public void getContacts_ShouldReturnListOfContactsFindOneInterface() {
        ContactsFindOneInterface contact1 = mock(ContactsFindOneInterface.class);
        ContactsFindOneInterface contact2 = mock(ContactsFindOneInterface.class);

        when(parentsRepository.findAllContacts()).thenReturn(List.of(contact1, contact2));

        List<ContactsFindOneInterface> result = userService.getContacts();

        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(contact1);
        assertThat(result.get(1)).isEqualTo(contact2);
    }

    @Test
    public void updateScore_ShouldReturnInt() {
        int change = 5;
        ScoreUpdateRequest request = new ScoreUpdateRequest(childSn, change);

        Child updatedChild = Child.builder()
            .serialNum(childSn)
            .score(mockChild.getScore() + change)
            .build();

        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.of(mockChild));
        when(childRepository.save(any(Child.class))).thenReturn(updatedChild);

        int result = userService.updateScore(request);

        assertThat(result).isEqualTo(updatedChild.getScore());
    }

    @Test
    public void updateScore_WhenChildDoesNotExist_ShouldThrowNoSuchElementException() {
        int change = 5;
        ScoreUpdateRequest request = new ScoreUpdateRequest(childSn, change);

        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateScore(request))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("사용자가 존재하지 않습니다.");
    }

    @Test
    public void join_ShouldCreateAndReturnParentsFindOneResponse() {
        JoinInfoSaveRequest joinRequest = new JoinInfoSaveRequest(
            "010-0000-0000", "아이",
            Date.valueOf("2010-01-01"), "000000", 1);

        when(childRepository.generateSerialNum()).thenReturn(childSn);
        when(childRepository.save(any(Child.class))).thenReturn(mockChild);

        ChildFindOneResponse result = userService.join(joinRequest);

        assertThat(result.getSn()).isEqualTo(mockChild.getSerialNum());
        assertThat(result.getName()).isEqualTo(mockChild.getName());
        assertThat(result.getPhoneNum()).isEqualTo(mockChild.getPhoneNum());
        assertThat(result.getBirthDate()).isEqualTo(mockChild.getBirthDate());
        assertThat(result.getProfileId()).isEqualTo(mockChild.getProfileId());
    }

    @Test
    public void checkPhone_WhenPhoneNumNotExists_ShouldReturnTrue() {
        PhoneFindRequest phoneFindRequest = new PhoneFindRequest("010-0000-0000");

        when(childRepository.findByPhoneNum("010-0000-0000")).thenReturn(Optional.empty());

        boolean result = userService.checkPhone(phoneFindRequest);
        assertThat(result).isTrue();
    }

    @Test
    public void checkPhone_WhenPhoneNumExists_ShouldReturnFalse() {
        PhoneFindRequest phoneFindRequest = new PhoneFindRequest("010-0000-0000");

        when(childRepository.findByPhoneNum("010-0000-0000")).thenReturn(Optional.of(mockChild));

        boolean result = userService.checkPhone(phoneFindRequest);
        assertThat(result).isFalse();
    }

    @Test
    public void login_WhenCredentialsAreValid_ShouldReturnParentsFindOneResponse()
        throws AuthException {
        LoginInfoFindRequest loginRequest = new LoginInfoFindRequest("010-0000-0000", "000000");

        when(childRepository.findByPhoneNum("010-0000-0000")).thenReturn(Optional.of(mockChild));
        when(passwordEncoder.matches("000000", mockChild.getAccountInfo())).thenReturn(true);

        ChildFindOneResponse result = userService.login(loginRequest);

        assertThat(result.getSn()).isEqualTo(mockChild.getSerialNum());
        assertThat(result.getName()).isEqualTo(mockChild.getName());
        assertThat(result.getPhoneNum()).isEqualTo(mockChild.getPhoneNum());
        assertThat(result.getBirthDate()).isEqualTo(mockChild.getBirthDate());
        assertThat(result.getProfileId()).isEqualTo(mockChild.getProfileId());
    }

    @Test
    public void login_WhenCredentialsAreInvalid_ShouldThrowAuthException() {
        LoginInfoFindRequest loginRequest = new LoginInfoFindRequest("010-0000-0000", "111111");

        when(childRepository.findByPhoneNum("010-0000-0000")).thenReturn(Optional.of(mockChild));
        when(passwordEncoder.matches("111111", mockChild.getAccountInfo())).thenReturn(false);

        assertThatThrownBy(() -> userService.login(loginRequest))
            .isInstanceOf(AuthException.class)
            .hasMessage("INVALID_CREDENTIALS");
    }

    @Test
    public void login_WhenUserDoesNotExist_ShouldThrowAuthException() {
        LoginInfoFindRequest loginRequest = new LoginInfoFindRequest("010-0000-0000", "000000");

        when(childRepository.findByPhoneNum("010-0000-0000")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login(loginRequest))
            .isInstanceOf(AuthException.class)
            .hasMessage("사용자가 존재하지 않습니다.");
    }

    @Test
    public void getFamilyInfo_ShouldReturnListOfFamilyInfoResponse() {
        FamilyInfoInterface mockFamilyInfo1 = new FamilyInfoInterface() {
            @Override
            public long getSn() {
                return 1000000000;
            }

            @Override
            public int getProfileId() {
                return 1;
            }

            @Override
            public String getName() {
                return "부모";
            }
        };

        FamilyInfoInterface mockFamilyInfo2 = new FamilyInfoInterface() {
            @Override
            public long getSn() {
                return 1000000010;
            }

            @Override
            public int getProfileId() {
                return 2;
            }

            @Override
            public String getName() {
                return "부모2";
            }
        };

        when(familyRepository.findParentsInfo(childSn)).thenReturn(
            List.of(mockFamilyInfo1, mockFamilyInfo2));

        List<FamilyInfoResponse> result = userService.getFamilyInfo(childSn);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getSn()).isEqualTo(mockFamilyInfo1.getSn());
        assertThat(result.get(0).getProfileId()).isEqualTo(mockFamilyInfo1.getProfileId());
        assertThat(result.get(0).getName()).isEqualTo(mockFamilyInfo1.getName());
        assertThat(result.get(1).getSn()).isEqualTo(mockFamilyInfo2.getSn());
        assertThat(result.get(1).getProfileId()).isEqualTo(mockFamilyInfo2.getProfileId());
        assertThat(result.get(1).getName()).isEqualTo(mockFamilyInfo2.getName());
    }

    @Test
    public void getParentsAlias_ShouldReturnString() {
        String alias = "아빠";

        Family family = Family.builder()
            .child(mockChild)
            .parents(mockParents)
            .parentsAlias(alias)
            .build();

        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.of(mockChild));
        when(parentsRepository.findBySerialNum(parentsSn)).thenReturn(Optional.of(mockParents));
        when(familyRepository.findByChildAndParents(mockChild, mockParents)).thenReturn(
            Optional.of(family));

        String result = userService.getParentsAlias(childSn, parentsSn);

        assertThat(result).isEqualTo(alias);
    }

    @Test
    public void getParentsAlias_WhenChildDoesNotExist_ShouldThrowNoSuchElementException() {
        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getParentsAlias(childSn, parentsSn))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("아이 사용자가 존재하지 않습니다.");
    }

    @Test
    public void getParentsAlias_WhenParentsDoesNotExist_ShouldThrowNoSuchElementException() {
        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.of(mockChild));
        when(parentsRepository.findBySerialNum(parentsSn)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getParentsAlias(childSn, parentsSn))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("부모 사용자가 존재하지 않습니다.");
    }

    @Test
    public void getParentsAlias_WhenFamilyDoesNotExist_ShouldThrowNoSuchElementException() {
        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.of(mockChild));
        when(parentsRepository.findBySerialNum(parentsSn)).thenReturn(Optional.of(mockParents));
        when(familyRepository.findByChildAndParents(mockChild, mockParents)).thenReturn(
            Optional.empty());

        assertThatThrownBy(() -> userService.getParentsAlias(childSn, parentsSn))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("가족 관계가 존재하지 않습니다.");
    }

    @Test
    public void getScore_ShouldReturnInt() {
        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.of(mockChild));

        int result = userService.getScore(childSn);

        assertThat(result).isEqualTo(mockChild.getScore());
    }
}
