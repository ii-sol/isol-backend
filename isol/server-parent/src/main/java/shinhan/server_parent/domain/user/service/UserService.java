package shinhan.server_parent.domain.user.service;

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
    private final ParentsRepository parentsRepository;
    private final ChildRepository childRepository;
    private final FamilyRepository familyRepository;
    private final ChildManageRepository childManageRepository;

    public ParentsFindOneResponse getParents(long sn) {
        Parents parents = parentsRepository.findBySerialNum(sn)
                .orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다."));

        return parents.convertToUserFindOneResponse();
    }

    public ChildFindOneResponse getChild(long sn) {
        Child child = childRepository.findBySerialNum(sn)
                .orElseThrow(() -> new NoSuchElementException("아이 사용자가 존재하지 않습니다."));

        return child.convertToUserFindOneResponse();
    }

    public ParentsFindOneResponse updateUser(UserUpdateRequest userUpdateRequest) {
        Parents parents = parentsRepository.findBySerialNum(userUpdateRequest.getSerialNum())
                .orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다."));

        if (parentsRepository.findByPhoneNum(userUpdateRequest.getPhoneNum()).isEmpty()) {
            parents.setPhoneNum(userUpdateRequest.getPhoneNum());
            parents.setName(userUpdateRequest.getName());
            parents.setBirthDate(userUpdateRequest.getBirthDate());
            parents.setProfileId(userUpdateRequest.getProfileId());

            Parents updatedParents = parentsRepository.save(parents);

            if (isUpdated(userUpdateRequest, updatedParents)) {
                return updatedParents.convertToUserFindOneResponse();
            } else {
                throw new InternalError("회원 정보 변경이 실패하였습니다.");
            }
        } else {
            throw new InternalError("이미 가입된 전화번호입니다.");
        }
    }

    private boolean isUpdated(UserUpdateRequest userUpdateRequest, Parents updatedParents) {
        return updatedParents.getPhoneNum().equals(userUpdateRequest.getPhoneNum())
                && updatedParents.getName().equals(userUpdateRequest.getName())
                && updatedParents.getBirthDate().equals(userUpdateRequest.getBirthDate())
                && updatedParents.getProfileId() == userUpdateRequest.getProfileId();
    }

    public void disconnectFamily(long sn, long childSn) {
        Parents parents = parentsRepository.findBySerialNum(sn)
                .orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다."));

        Child child = childRepository.findBySerialNum(childSn)
                .orElseThrow(() -> new NoSuchElementException("아이 사용자가 존재하지 않습니다."));

        Family family = familyRepository
                .findByChildAndParents(child, parents)
                .orElseThrow(() -> new NoSuchElementException("가족 관계가 존재하지 않습니다."));

        familyRepository.delete(family.getId());
    }

    public ChildManageFindOneResponse getChildManage(long childSn) {
        Child child = childRepository.findBySerialNum(childSn)
                .orElseThrow(() -> new NoSuchElementException("아이 사용자가 존재하지 않습니다."));

        ChildManage childManage = childManageRepository.findByChild(child)
                .orElseGet(() -> childManageRepository.save(ChildManage.builder().child(child).build()));

        return childManage.convertToChildManageFIndOneResponse();
    }

    public ChildManageFindOneResponse updateChildManage(ChildManageUpdateRequest childManageUpdateRequest) {
        Child child = childRepository.findBySerialNum(childManageUpdateRequest.getChildSn())
                .orElseThrow(() -> new NoSuchElementException("아이 사용자가 존재하지 않습니다."));

        ChildManage childManage = childManageRepository.findByChild(child)
                .orElseGet(() -> childManageRepository.save(ChildManage.builder().child(child).build()));

        childManage.setBaseRate(childManageUpdateRequest.getBaseRate());
        childManage.setInvestLimit(childManageUpdateRequest.getInvestLimit());
        childManage.setLoanLimit(childManageUpdateRequest.getLoanLimit());

        ChildManage updatedChildManage = childManageRepository.save(childManage);

        if (isChildManageUpdated(childManageUpdateRequest, updatedChildManage)) {
            return updatedChildManage.convertToChildManageFIndOneResponse();
        } else {
            throw new InternalError("아이 관리 정보 변경이 실패하였습니다.");
        }
    }

    private boolean isChildManageUpdated(ChildManageUpdateRequest childManageUpdateRequest, ChildManage updatedChildManage) {
        return childManageUpdateRequest.getBaseRate() == updatedChildManage.getBaseRate()
                && childManageUpdateRequest.getInvestLimit() == updatedChildManage.getInvestLimit()
                && childManageUpdateRequest.getLoanLimit() == updatedChildManage.getLoanLimit();
    }

    public ParentsFindOneResponse join(JoinInfoSaveRequest joinInfoSaveRequest) {
        long serialNum = parentsRepository.generateSerialNum();
        log.info("Generated serial number={}", serialNum);
        Parents parents = parentsRepository.save(joinInfoSaveRequest.convertToParents(serialNum, passwordEncoder));

        return parents.convertToUserFindOneResponse();
    }

    public boolean checkPhone(PhoneFindRequest phoneFindRequest) {
        return parentsRepository.findByPhoneNum(phoneFindRequest.getPhoneNum()).isEmpty();
    }

    public ParentsFindOneResponse login(LoginInfoFindRequest loginInfoFindRequest) throws AuthException {
        Parents parents = parentsRepository.findByPhoneNum(loginInfoFindRequest.getPhoneNum()).orElseThrow(() -> new AuthException("사용자가 존재하지 않습니다."));

        if (!passwordEncoder.matches(loginInfoFindRequest.getAccountInfo(), parents.getAccountInfo())) {
            throw new AuthException("INVALID_CREDENTIALS");
        }

        return parents.convertToUserFindOneResponse();
    }

    public List<FamilyInfoResponse> getFamilyInfo(long sn) {
        return familyRepository.findChildInfo(sn)
                .stream()
                .map(myFamily -> FamilyInfoResponse.builder()
                    .sn(myFamily.getSn())
                    .profileId(myFamily.getProfileId())
                    .name(myFamily.getName())
                    .build())
                .collect(Collectors.toList());
    }
}
