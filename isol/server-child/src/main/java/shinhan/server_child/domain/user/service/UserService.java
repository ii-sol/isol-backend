package shinhan.server_child.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shinhan.server_common.domain.user.dto.*;
import shinhan.server_common.domain.user.entity.Child;
import shinhan.server_common.domain.user.entity.ChildManage;
import shinhan.server_common.domain.user.entity.Family;
import shinhan.server_common.domain.user.entity.Parents;
import shinhan.server_common.domain.user.repository.ChildManageRepository;
import shinhan.server_common.domain.user.repository.ChildRepository;
import shinhan.server_common.domain.user.repository.FamilyRepository;
import shinhan.server_common.domain.user.repository.ParentsRepository;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.FamilyInfoResponse;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final ChildRepository childRepository;
    private final ParentsRepository parentsRepository;
    private final FamilyRepository familyRepository;
    private final ChildManageRepository childManageRepository;
    private final JwtService jwtService;

    public ChildFindOneResponse getChild(long sn) {
        Child child = childRepository.findBySerialNum(sn)
                .orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다."));

        return child.convertToUserFindOneResponse();
    }

    public ParentsFindOneResponse getParents(long sn) {
        Parents parents = parentsRepository.findBySerialNum(sn)
                .orElseThrow(() -> new NoSuchElementException("부모 사용자가 존재하지 않습니다."));

        return parents.convertToUserFindOneResponse();
    }

    public ChildFindOneResponse updateUser(UserUpdateRequest userUpdateRequest) {
        Child child = childRepository.findBySerialNum(userUpdateRequest.getSerialNum())
                .orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다."));

        if (childRepository.findByPhoneNum(userUpdateRequest.getPhoneNum()).isEmpty()) {
            child.setPhoneNum(userUpdateRequest.getPhoneNum());
            child.setName(userUpdateRequest.getName());
            child.setBirthDate(userUpdateRequest.getBirthDate());
            child.setProfileId(userUpdateRequest.getProfileId());

            Child updatedChild = childRepository.save(child);

            if (isUpdated(userUpdateRequest, updatedChild)) {
                return updatedChild.convertToUserFindOneResponse();
            } else {
                throw new InternalError("회원 정보 변경이 실패하였습니다.");
            }
        } else {
            throw new InternalError("이미 가입된 전화번호입니다.");
        }
    }

    private boolean isUpdated(UserUpdateRequest userUpdateRequest, Child updatedChild) {
        return updatedChild.getPhoneNum().equals(userUpdateRequest.getPhoneNum())
                && updatedChild.getName().equals(userUpdateRequest.getName())
                && updatedChild.getBirthDate().equals(userUpdateRequest.getBirthDate())
                && updatedChild.getProfileId() == userUpdateRequest.getProfileId();
    }

    public int connectFamily(FamilySaveRequest familySaveRequest) {
        Child child = childRepository.findBySerialNum(familySaveRequest.getSn())
                .orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다."));

        Parents parents = parentsRepository.findByPhoneNum(familySaveRequest.getPhoneNum())
                .orElseThrow(() -> new NoSuchElementException("부모 사용자가 존재하지 않습니다."));

        Family family = familyRepository.save(new Family(child, parents, familySaveRequest.getParentsAlias()));

        return family.getId();
    }

    public void disconnectFamily(long sn, long parentsSn) {
        Child child = childRepository.findBySerialNum(sn)
                .orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다."));

        Parents parents = parentsRepository.findBySerialNum(parentsSn)
                .orElseThrow(() -> new NoSuchElementException("부모 사용자가 존재하지 않습니다."));

        Family family = familyRepository
                .findByChildAndParents(child, parents)
                .orElseThrow(() -> new NoSuchElementException("가족 관계가 존재하지 않습니다."));

        familyRepository.delete(family.getId());
    }

    public boolean isFamily(int id) {
        return familyRepository.findById(id).isPresent();
    }

    public ChildManageFindOneResponse getChildManage(long childSn) {
        Child child = childRepository.findBySerialNum(childSn)
                .orElseThrow(() -> new NoSuchElementException("아이 사용자가 존재하지 않습니다."));

        ChildManage childManage = childManageRepository.findByChild(child)
                .orElseGet(() -> childManageRepository.save(ChildManage.builder().child(child).build()));

        return childManage.convertToChildManageFIndOneResponse();
    }


    public List<ContactsFindOneInterface> getContacts() {
        return parentsRepository.findAllContacts();
    }

    public int updateScore(ScoreUpdateRequest scoreUpdateRequest) {
        Child child = childRepository.findBySerialNum(scoreUpdateRequest.getSn())
                .orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다."));

        child.setScore(child.getScore() + scoreUpdateRequest.getChange());
        Child updatedChild = childRepository.save(child);

        return updatedChild.getScore();
    }

    public ChildFindOneResponse join(JoinInfoSaveRequest joinInfoSaveRequest) {
        long serialNum = childRepository.generateSerialNum();
        log.info("Generated serial number={}", serialNum);
        Child child = childRepository.save(joinInfoSaveRequest.convertToChild(serialNum, passwordEncoder));

        return child.convertToUserFindOneResponse();
    }

    public boolean checkPhone(PhoneFindRequest phoneFindRequest) {
        return childRepository.findByPhoneNum(phoneFindRequest.getPhoneNum()).isEmpty();
    }

    public ChildFindOneResponse login(LoginInfoFindRequest loginInfoFindRequest) throws AuthException {
        Child child = childRepository.findByPhoneNum(loginInfoFindRequest.getPhoneNum()).orElseThrow(() -> new AuthException("사용자가 존재하지 않습니다."));

        if (!passwordEncoder.matches(loginInfoFindRequest.getAccountInfo(), child.getAccountInfo())) {
            throw new AuthException("INVALID_CREDENTIALS");
        }

        return child.convertToUserFindOneResponse();
    }

    public List<FamilyInfoResponse> getFamilyInfo(long sn) {
        return familyRepository.findParentsInfo(sn)
                .stream()
                .map(myFamily -> new FamilyInfoResponse(myFamily.getSn(), myFamily.getProfileId(), myFamily.getName()))
                .collect(Collectors.toList());
    }

    public String getParentsAlias(long childSn, long parentsSn) {
        Child child = childRepository.findBySerialNum(childSn)
                .orElseThrow(() -> new NoSuchElementException("아이 사용자가 존재하지 않습니다."));

        Parents parents = parentsRepository.findBySerialNum(parentsSn)
                .orElseThrow(() -> new NoSuchElementException("부모 사용자가 존재하지 않습니다."));

        Family family = familyRepository
                .findByChildAndParents(child, parents)
                .orElseThrow(() -> new NoSuchElementException("가족 관계가 존재하지 않습니다."));

        return family.getParentsAlias();
    }

    public int getScore(long childSn) {

        ChildFindOneResponse user = getChild(childSn);

        int score = user.getScore();

        return score;
    }
}
