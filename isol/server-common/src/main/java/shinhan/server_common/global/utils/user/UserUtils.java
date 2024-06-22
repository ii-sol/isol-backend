package shinhan.server_common.global.utils.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import shinhan.server_common.domain.user.entity.Child;
import shinhan.server_common.domain.user.entity.Parents;
import shinhan.server_common.domain.user.repository.ChildRepository;
import shinhan.server_common.domain.user.repository.ParentsRepository;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserUtils {

    private final ChildRepository childRepository;
    private final ParentsRepository parentsRepository;

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



}