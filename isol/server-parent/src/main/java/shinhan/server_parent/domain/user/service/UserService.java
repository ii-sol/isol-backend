package shinhan.server_parent.domain.user.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shinhan.server_parent.domain.user.dto.*;
import shinhan.server_parent.domain.user.entity.Child;
import shinhan.server_parent.domain.user.entity.Family;
import shinhan.server_parent.domain.user.entity.Parents;
import shinhan.server_parent.domain.user.repository.ChildRepository;
import shinhan.server_parent.domain.user.repository.FamilyRepository;
import shinhan.server_parent.domain.user.repository.ParentsRepository;
import shinhan.server_common.global.security.dto.FamilyInfoResponse;
import shinhan.server_common.global.exception.AuthException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private ParentsRepository parentsRepository;
    private ChildRepository childRepository;
    private FamilyRepository familyRepository;

    @Transactional
    public ParentsFindOneResponse getUser(long sn) {
        Parents parents = parentsRepository.findBySerialNum(sn)
                .orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다."));

        return parents.convertToUserFindOneResponse();
    }

    @Transactional
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

    private static boolean isUpdated(ParentsUpdateRequest parentsUpdateRequest, Parents updatedParents) {
        return updatedParents.getPhoneNum().equals(parentsUpdateRequest.getPhoneNum())
                && updatedParents.getName().equals(parentsUpdateRequest.getName())
                && updatedParents.getBirthDate().equals(parentsUpdateRequest.getBirthDate())
                && updatedParents.getProfileId() == parentsUpdateRequest.getProfileId();
    }

    @Transactional
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

    @Transactional
    public boolean isFamily(int deletedId) {
        return familyRepository.findById(deletedId).isPresent();
    }

    @Transactional
    public List<String> getPhones() {
        return parentsRepository.findAllPhones();
    }

    @Transactional
    public ParentsFindOneResponse join(JoinInfoSaveRequest joinInfoSaveRequest) {
        long serialNum = parentsRepository.generateSerialNum();
        log.info("Generated serial number={}", serialNum);
        Parents parents = parentsRepository.save(joinInfoSaveRequest.convertToUser(serialNum, passwordEncoder));

        return parents.convertToUserFindOneResponse();
    }

    @Transactional
    public boolean checkPhone(PhoneFindRequest phoneFindRequest) {
        return parentsRepository.findByPhoneNum(phoneFindRequest.getPhoneNum()).isEmpty();
    }

    @Transactional
    public ParentsFindOneResponse login(@Valid LoginInfoFindRequest loginInfoFindRequest) throws AuthException {
        Parents parents = parentsRepository.findByPhoneNum(loginInfoFindRequest.getPhoneNum()).orElseThrow(() -> new AuthException("사용자가 존재하지 않습니다."));

        if (!passwordEncoder.matches(loginInfoFindRequest.getAccountInfo(), parents.getAccountInfo())) {
            throw new AuthException("INVALID_CREDENTIALS");
        }

        return parents.convertToUserFindOneResponse();
    }

    @Transactional()
    public List<FamilyInfoResponse> getFamilyInfo(long sn) {
        return familyRepository.findMyFamilyInfo(sn)
                .stream()
                .map(myFamily -> new FamilyInfoResponse(myFamily.getSn(), myFamily.getName()))
                .collect(Collectors.toList());
    }
}
