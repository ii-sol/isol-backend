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
    private ParentsRepository parentsRepository;
    private ChildRepository childRepository;
    private FamilyRepository familyRepository;
    private ChildManageRepository childManageRepository;

    public ParentsFindOneResponse getParents(long sn) {
        Parents parents = parentsRepository.findBySerialNum(sn)
                .orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다."));

        return parents.convertToUserFindOneResponse();
    }

    public ChildFindOneResponse getChild(long sn) {
        Child child = childRepository.findBySerialNum(sn)
                .orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다."));

        return child.convertToUserFindOneResponse();
    }

    public ParentsFindOneResponse updateUser(ParentsUpdateRequest parentsUpdateRequest) {
        Parents parents = parentsRepository.findBySerialNum(parentsUpdateRequest.getSerialNum())
                .orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다."));

        parents.setPhoneNum(parentsUpdateRequest.getPhoneNum());
        parents.setName(parentsUpdateRequest.getName());
        parents.setBirthDate(parentsUpdateRequest.getBirthDate());
        parents.setProfileId(parentsUpdateRequest.getProfileId());

        Parents updatedParents = parentsRepository.save(parents);

        if (isUpdated(parentsUpdateRequest, updatedParents)) {
            return updatedParents.convertToUserFindOneResponse();
        } else {
            throw new InternalError("회원 정보 변경이 실패하였습니다.");
        }
    }

    private boolean isUpdated(ParentsUpdateRequest parentsUpdateRequest, Parents updatedParents) {
        return updatedParents.getPhoneNum().equals(parentsUpdateRequest.getPhoneNum())
                && updatedParents.getName().equals(parentsUpdateRequest.getName())
                && updatedParents.getBirthDate().equals(parentsUpdateRequest.getBirthDate())
                && updatedParents.getProfileId() == parentsUpdateRequest.getProfileId();
    }

    public int disconnectFamily(long sn, long childSn) {
        Parents parents = parentsRepository.findBySerialNum(sn)
                .orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다."));

        Child child = childRepository.findBySerialNum(childSn)
                .orElseThrow(() -> new NoSuchElementException("아이 사용자가 존재하지 않습니다."));

        Family family = familyRepository
                .findByChildAndParents(child, parents)
                .orElseThrow(() -> new NoSuchElementException("가족 관계가 존재하지 않습니다."));

        familyRepository.delete(family.getId());

        return family.getId();
    }

    public boolean isFamily(int id) {
        return familyRepository.findById(id).isPresent();
    }

    public ChildManageFindOneResponse getChildManage(long childSn) {
        Child child = childRepository.findBySerialNum(childSn)
                .orElseThrow(() -> new NoSuchElementException("아이 사용자가 존재하지 않습니다."));

        ChildManage childManage = childManageRepository.findByChild(child)
                .orElseGet(() -> childManageRepository.save(new ChildManage(child)));

        return childManage.convertToChildManageFIndOneResponse();
    }

    public ChildManageFindOneResponse updateChildManage(ChildManageUpdateRequest childManageUpdateRequest) {
        Child child = childRepository.findBySerialNum(childManageUpdateRequest.getChildSn())
                .orElseThrow(() -> new NoSuchElementException("아이 사용자가 존재하지 않습니다."));

        ChildManage childManage = childManageRepository.findByChild(child)
                .orElseGet(() -> childManageRepository.save(new ChildManage(child)));

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
                .map(myFamily -> new FamilyInfoResponse(myFamily.getSn(), myFamily.getName()))
                .collect(Collectors.toList());
    }
}
