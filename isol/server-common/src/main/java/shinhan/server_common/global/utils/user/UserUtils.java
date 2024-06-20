package shinhan.server_common.global.utils.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import shinhan.server_common.domain.entity.TempUser;
import shinhan.server_common.domain.entity.TempUserRepository;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserUtils {

    private final TempUserRepository tempUserRepository;

    //serailNumber로 사용자 조회
    public TempUser getUserBySerialNumber(Long serialNumber){
        return tempUserRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }
}