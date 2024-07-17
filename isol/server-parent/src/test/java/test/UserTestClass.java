package test;

import java.sql.Date;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import shinhan.ServerParentApplication;
import shinhan.server_common.domain.user.dto.*;
import shinhan.server_common.domain.user.entity.Child;
import shinhan.server_common.domain.user.entity.ChildManage;
import shinhan.server_common.domain.user.entity.Family;
import shinhan.server_common.domain.user.entity.Parents;
import shinhan.server_common.domain.user.repository.*;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.dto.FamilyInfoResponse;
import shinhan.server_parent.domain.user.service.UserService;

import java.util.Optional;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ServerParentApplication.class)
public class UserTestClass {

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

    @Test
    public void contextLoads() {
        assertThat(userService).isNotNull();
    }

    @Test
    public void getParents_WhenUserExists_ShouldReturnParentsFindOneResponse() {
        long sn = 1000000000;
        Parents mockParents = Parents.builder()
            .serialNum(sn)
            .name("부모")
            .phoneNum("010-0000-0000")
            .birthDate(Date.valueOf("1990-01-01"))
            .profileId(1)
            .build();
        when(parentsRepository.findBySerialNum(sn)).thenReturn(Optional.of(mockParents));

        ParentsFindOneResponse result = userService.getParents(sn);

        assertThat(result.getSn()).isEqualTo(mockParents.getSerialNum());
        assertThat(result.getName()).isEqualTo(mockParents.getName());
        assertThat(result.getPhoneNum()).isEqualTo(mockParents.getPhoneNum());
        assertThat(result.getBirthDate()).isEqualTo(mockParents.getBirthDate());
        assertThat(result.getProfileId()).isEqualTo(mockParents.getProfileId());
    }

    @Test
    public void getParents_WhenUserDoesNotExist_ShouldThrowNoSuchElementException() {
        long sn = 1000000000;
        when(parentsRepository.findBySerialNum(sn)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getParents(sn))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("사용자가 존재하지 않습니다.");
    }

    @Test
    public void getChild_WhenUserExists_ShouldReturnChildFindOneResponse() {
        long sn = 1000000000;
        Child mockChild = Child.builder()
            .serialNum(sn)
            .name("아이")
            .phoneNum("010-0000-0000")
            .birthDate(Date.valueOf("2012-01-01"))
            .profileId(1)
            .score(50)
            .build();
        when(childRepository.findBySerialNum(sn)).thenReturn(Optional.of(mockChild));

        ChildFindOneResponse result = userService.getChild(sn);

        assertThat(result.getSn()).isEqualTo(mockChild.getSerialNum());
        assertThat(result.getName()).isEqualTo(mockChild.getName());
        assertThat(result.getPhoneNum()).isEqualTo(mockChild.getPhoneNum());
        assertThat(result.getBirthDate()).isEqualTo(mockChild.getBirthDate());
        assertThat(result.getProfileId()).isEqualTo(mockChild.getProfileId());
    }

    @Test
    public void getChild_WhenUserDoesNotExist_ShouldThrowNoSuchElementException() {
        long sn = 1000000000;
        when(childRepository.findBySerialNum(sn)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getChild(sn))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("아이 사용자가 존재하지 않습니다.");
    }

    @Test
    public void updateUser_WhenUserExists_ShouldUpdateAndReturnParentsFindOneResponse() {
        long sn = 1000000000;
        Parents mockParents = Parents.builder()
            .serialNum(sn)
            .name("부모")
            .phoneNum("010-0000-0000")
            .birthDate(Date.valueOf("1990-01-01"))
            .profileId(1)
            .build();
        when(parentsRepository.findBySerialNum(sn)).thenReturn(Optional.of(mockParents));

        UserUpdateRequest updateRequest = new UserUpdateRequest(
            "010-1234-5678",
            "홍길동",
            Date.valueOf("1990-01-01"),
            2
        );
        updateRequest.setSerialNum(sn);

        Parents updatedParents = Parents.builder()
            .serialNum(sn)
            .name("홍길동")
            .phoneNum("010-1234-5678")
            .birthDate(Date.valueOf("1990-01-01"))
            .profileId(2)
            .build();
        when(parentsRepository.findByPhoneNum("010-1234-5678")).thenReturn(Optional.empty());
        when(parentsRepository.save(mockParents)).thenReturn(updatedParents);

        ParentsFindOneResponse result = userService.updateUser(updateRequest);

        assertThat(result.getSn()).isEqualTo(mockParents.getSerialNum());
        assertThat(result.getName()).isEqualTo(mockParents.getName());
        assertThat(result.getPhoneNum()).isEqualTo(mockParents.getPhoneNum());
        assertThat(result.getBirthDate()).isEqualTo(mockParents.getBirthDate());
        assertThat(result.getProfileId()).isEqualTo(mockParents.getProfileId());
    }

    @Test
    public void updateUser_WhenPhoneNumExists_ShouldThrowInternalError() {
        long sn = 1000000000;
        Parents mockParents = Parents.builder()
            .serialNum(sn)
            .name("부모")
            .phoneNum("010-0000-0000")
            .birthDate(Date.valueOf("1990-01-01"))
            .profileId(1)
            .build();
        when(parentsRepository.findBySerialNum(sn)).thenReturn(Optional.of(mockParents));

        UserUpdateRequest updateRequest = new UserUpdateRequest(
            "010-1234-5678",
            "홍길동",
            Date.valueOf("1990-01-01"),
            2
        );
        updateRequest.setSerialNum(sn);
        when(parentsRepository.findByPhoneNum("010-1234-5678")).thenReturn(
            Optional.of(new Parents()));

        assertThatThrownBy(() -> userService.updateUser(updateRequest))
            .isInstanceOf(InternalError.class)
            .hasMessage("이미 가입된 전화번호입니다.");
    }

    @Test
    public void disconnectFamily_WhenFamilyExists_ShouldDeleteFamily() {
        long sn = 1000000000;
        long childSn = 1000000001;

        Parents mockParents = new Parents();
        when(parentsRepository.findBySerialNum(sn)).thenReturn(Optional.of(mockParents));

        Child mockChild = new Child();
        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.of(mockChild));

        Family mockFamily = new Family();
        when(familyRepository.findByChildAndParents(mockChild, mockParents)).thenReturn(
            Optional.of(mockFamily));

        doNothing().when(familyRepository).delete(mockFamily.getId());

        userService.disconnectFamily(sn, childSn);

        verify(familyRepository, times(1)).delete(mockFamily.getId());
    }

    @Test
    public void disconnectFamily_WhenFamilyDoesNotExist_ShouldThrowNoSuchElementException() {
        long sn = 1000000000;
        long childSn = 1000000001;

        Parents mockParents = new Parents();
        when(parentsRepository.findBySerialNum(sn)).thenReturn(Optional.of(mockParents));

        Child mockChild = new Child();
        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.of(mockChild));

        when(familyRepository.findByChildAndParents(mockChild, mockParents)).thenReturn(
            Optional.empty());

        assertThatThrownBy(() -> userService.disconnectFamily(sn, childSn))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("가족 관계가 존재하지 않습니다.");
    }

    @Test
    public void getChildManage_WhenChildManageExists_ShouldReturnChildManageFindOneResponse() {
        long childSn = 1000000001;

        Child mockChild = new Child();
        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.of(mockChild));

        ChildManage mockChildManage = ChildManage.builder().child(mockChild).build();
        when(childManageRepository.findByChild(mockChild)).thenReturn(Optional.of(mockChildManage));

        ChildManageFindOneResponse result = userService.getChildManage(childSn);

        assertThat(result.getBaseRate()).isEqualTo(mockChildManage.getBaseRate());
        assertThat(result.getInvestLimit()).isEqualTo(mockChildManage.getInvestLimit());
        assertThat(result.getLoanLimit()).isEqualTo(mockChildManage.getLoanLimit());
    }

    @Test
    public void getChildManage_WhenChildDoesNotExist_ShouldThrowNoSuchElementException() {
        long childSn = 1000000001;

        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getChildManage(childSn))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("아이 사용자가 존재하지 않습니다.");
    }

    @Test
    public void updateChildManage_WhenChildManageExists_ShouldUpdateAndReturnChildManageFindOneResponse() {
        long childSn = 1000000001;
        ChildManageUpdateRequest updateRequest = new ChildManageUpdateRequest(childSn, 3, 500, 300);

        Child mockChild = new Child();
        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.of(mockChild));

        ChildManage mockChildManage = ChildManage.builder().child(mockChild).build();
        when(childManageRepository.findByChild(mockChild)).thenReturn(Optional.of(mockChildManage));

        ChildManage updatedChildManage = ChildManage.builder().child(mockChild).build();
        updatedChildManage.setBaseRate(3);
        updatedChildManage.setInvestLimit(500);
        updatedChildManage.setLoanLimit(300);

        when(childManageRepository.save(mockChildManage)).thenReturn(updatedChildManage);

        ChildManageFindOneResponse result = userService.updateChildManage(updateRequest);

        assertThat(result.getBaseRate()).isEqualTo(mockChildManage.getBaseRate());
        assertThat(result.getInvestLimit()).isEqualTo(mockChildManage.getInvestLimit());
        assertThat(result.getLoanLimit()).isEqualTo(mockChildManage.getLoanLimit());
    }

    @Test
    public void updateChildManage_WhenChildDoesNotExist_ShouldThrowNoSuchElementException() {
        long childSn = 1000000001;

        ChildManageUpdateRequest updateRequest = new ChildManageUpdateRequest();

        when(childRepository.findBySerialNum(childSn)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateChildManage(updateRequest))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("아이 사용자가 존재하지 않습니다.");
    }

    @Test
    public void join_ShouldCreateAndReturnParentsFindOneResponse() {
        JoinInfoSaveRequest joinRequest = new JoinInfoSaveRequest("010-0000-0000", "부모",
            Date.valueOf("1990-01-01"), "000000", 1);

        long generatedSerialNum = 1000000000;
        when(parentsRepository.generateSerialNum()).thenReturn(generatedSerialNum);

        Parents mockParents = Parents.builder()
            .serialNum(generatedSerialNum)
            .name("부모")
            .phoneNum("010-0000-0000")
            .birthDate(Date.valueOf("1990-01-01"))
            .profileId(1)
            .build();
        when(parentsRepository.save(any(Parents.class))).thenReturn(mockParents);

        ParentsFindOneResponse result = userService.join(joinRequest);

        assertThat(result.getSn()).isEqualTo(mockParents.getSerialNum());
        assertThat(result.getName()).isEqualTo(mockParents.getName());
        assertThat(result.getPhoneNum()).isEqualTo(mockParents.getPhoneNum());
        assertThat(result.getBirthDate()).isEqualTo(mockParents.getBirthDate());
        assertThat(result.getProfileId()).isEqualTo(mockParents.getProfileId());
    }

    @Test
    public void checkPhone_WhenPhoneNumNotExists_ShouldReturnTrue() {
        PhoneFindRequest phoneFindRequest = new PhoneFindRequest("010-0000-0000");

        when(parentsRepository.findByPhoneNum("010-0000-0000")).thenReturn(Optional.empty());

        boolean result = userService.checkPhone(phoneFindRequest);
        assertThat(result).isTrue();
    }

    @Test
    public void checkPhone_WhenPhoneNumExists_ShouldReturnFalse() {
        PhoneFindRequest phoneFindRequest = new PhoneFindRequest("010-0000-0000");

        when(parentsRepository.findByPhoneNum("010-0000-0000")).thenReturn(
            Optional.of(new Parents()));

        boolean result = userService.checkPhone(phoneFindRequest);
        assertThat(result).isFalse();
    }

    @Test
    public void login_WhenCredentialsAreValid_ShouldReturnParentsFindOneResponse()
        throws AuthException {
        long sn = 1000000000;
        Parents mockParents = Parents.builder()
            .serialNum(sn)
            .name("부모")
            .phoneNum("010-0000-0000")
            .birthDate(Date.valueOf("1990-01-01"))
            .profileId(1)
            .accountInfo("$2a$10$D9yY91eRRO.GH9j2Ll1UPe/AXgYQYqv9UNuBlgNYL5.5dGJ19j06e")
            .build();

        LoginInfoFindRequest loginRequest = new LoginInfoFindRequest("010-0000-0000", "000000");

        when(parentsRepository.findByPhoneNum("010-0000-0000")).thenReturn(
            Optional.of(mockParents));
        when(passwordEncoder.matches("000000", mockParents.getAccountInfo())).thenReturn(true);

        ParentsFindOneResponse result = userService.login(loginRequest);

        assertThat(result.getSn()).isEqualTo(mockParents.getSerialNum());
        assertThat(result.getName()).isEqualTo(mockParents.getName());
        assertThat(result.getPhoneNum()).isEqualTo(mockParents.getPhoneNum());
        assertThat(result.getBirthDate()).isEqualTo(mockParents.getBirthDate());
        assertThat(result.getProfileId()).isEqualTo(mockParents.getProfileId());
    }

    @Test
    public void login_WhenCredentialsAreInvalid_ShouldThrowAuthException() {
        long sn = 1000000000;
        Parents mockParents = Parents.builder()
            .serialNum(sn)
            .name("부모")
            .phoneNum("010-0000-0000")
            .birthDate(Date.valueOf("1990-01-01"))
            .profileId(1)
            .accountInfo("$2a$10$D9yY91eRRO.GH9j2Ll1UPe/AXgYQYqv9UNuBlgNYL5.5dGJ19j06e")
            .build();

        LoginInfoFindRequest loginRequest = new LoginInfoFindRequest("010-0000-0000", "000000");

        when(parentsRepository.findByPhoneNum("010-0000-0000")).thenReturn(Optional.of(mockParents));
        when(passwordEncoder.matches("111111", mockParents.getAccountInfo())).thenReturn(false);

        assertThatThrownBy(() -> userService.login(loginRequest))
            .isInstanceOf(AuthException.class)
            .hasMessage("INVALID_CREDENTIALS");
    }

    @Test
    public void login_WhenUserDoesNotExist_ShouldThrowAuthException() {
        LoginInfoFindRequest loginRequest = new LoginInfoFindRequest("010-0000-0000", "000000");


        when(parentsRepository.findByPhoneNum("010-0000-0000")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login(loginRequest))
            .isInstanceOf(AuthException.class)
            .hasMessage("사용자가 존재하지 않습니다.");
    }

//    @Test
//    public void getFamilyInfo_ShouldReturnListOfFamilyInfoResponse() {
//        long sn = 1000000000;
//
//        Family mockFamily1 = Family.builder()
//            .sn(1000000001)
//            .profileId(1)
//            .name("아이")
//            .build();
//
//        Family mockFamily2 = Family.builder()
//            .sn(1000000002)
//            .profileId(1)
//            .name("아이2")
//            .build();
//
//        when(familyRepository.findChildInfo(sn)).thenReturn(List.of(mockFamily1, mockFamily2));
//
//        List<FamilyInfoResponse> result = userService.getFamilyInfo(sn);
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).getSn()).isEqualTo(mockFamily1.getSn());
//        assertThat(result.get(0).getProfileId()).isEqualTo(mockFamily1.getProfileId());
//        assertThat(result.get(0).getName()).isEqualTo(mockFamily1.getName());
//        assertThat(result.get(1).getSn()).isEqualTo(mockFamily2.getSn());
//        assertThat(result.get(1).getProfileId()).isEqualTo(mockFamily2.getProfileId());
//        assertThat(result.get(1).getName()).isEqualTo(mockFamily2.getName());
//    }
}
