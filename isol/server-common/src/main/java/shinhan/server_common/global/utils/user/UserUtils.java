package shinhan.server_common.global.utils.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import shinhan.server_common.domain.user.entity.Child;
import shinhan.server_common.domain.user.entity.Family;
import shinhan.server_common.domain.user.entity.Parents;
import shinhan.server_common.domain.user.repository.ChildRepository;
import shinhan.server_common.domain.user.repository.FamilyRepository;
import shinhan.server_common.domain.user.repository.ParentsRepository;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserUtils {

    private final ChildRepository childRepository;
    private final ParentsRepository parentsRepository;
    private final FamilyRepository familyRepository;

    //serialNumber로 이름 조회
    //TODO:Refactoring 필요
    public String getNameBySerialNumber(Long serialNumber) {
        Optional<Child> childOpt = childRepository.findBySerialNum(serialNumber);
        Optional<Parents> parentsOpt = parentsRepository.findBySerialNum(serialNumber);
        if (childOpt.isPresent()) {
            return childOpt.get().getName();
        } else {
            return parentsOpt.get().getName();
        }
    }

    //serailNumber로 사용자 조회
    public Child getChildBySerialNumber(Long serialNumber){
        return childRepository.findBySerialNum(serialNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }

    public Parents getParentsBySerialNumber(Long serialNumber){
        return parentsRepository.findBySerialNum(serialNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
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

    //test
}