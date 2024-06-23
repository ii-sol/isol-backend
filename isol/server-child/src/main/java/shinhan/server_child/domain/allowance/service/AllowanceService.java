package shinhan.server_child.domain.allowance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_child.domain.allowance.dto.MonthlyAllowanceFindOneResponse;
import shinhan.server_child.domain.allowance.dto.TemporalAllowanceSaveOneRequest;
import shinhan.server_child.domain.allowance.dto.TemporalChildAllowanceFindAllResponse;
import shinhan.server_child.domain.allowance.dto.UnAcceptTemporalAllowanceFindAllResponse;
import shinhan.server_child.domain.allowance.entity.TemporalAllowance;
import shinhan.server_child.domain.allowance.repository.MonthlyAllowanceRepository;
import shinhan.server_child.domain.allowance.repository.TemporalAllowanceRepository;
import shinhan.server_common.domain.user.entity.Child;
import shinhan.server_common.domain.user.entity.Parents;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;
import shinhan.server_common.global.utils.user.UserUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AllowanceService {

    private final TemporalAllowanceRepository temporalAllowanceRepository;
    private final MonthlyAllowanceRepository monthlyAllowanceRepository;
    private final UserUtils userUtils;

    //자식 - 용돈 조르기 신청 여기서 tempUser = 자식
    public void saveTemporalAllowance(Long userSerialNumber, Long psn, TemporalAllowanceSaveOneRequest request) {
        // TemporalAllowance 객체 생성
        TemporalAllowance temporalAllowance = TemporalAllowance.builder()
                .parentsSerialNumber(psn)
                .childSerialNumber(userSerialNumber)
                .content(request.getContent())
                .price(request.getAmount())
                .createDate(LocalDateTime.now())
                .status(1)
                .build();

        temporalAllowanceRepository.save(temporalAllowance);

    }

    //자식 - 용돈 조르기 취소하기
    public void cancleTemporalAllowance(Integer temporalAllowanceId) {
        TemporalAllowance findTemporalAllowance = temporalAllowanceRepository.findById(temporalAllowanceId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_TEMPORAL_ALLOWANCE));

        findTemporalAllowance.setStatus(6);
        temporalAllowanceRepository.save(findTemporalAllowance);
    }

    //자식 용돈 조르기 내역 조회 (과거)
    public List<TemporalChildAllowanceFindAllResponse> findChildTemporalAllowances(Long userSerialNumber, Integer year, Integer month) {

        return temporalAllowanceRepository.findByChildSerialNumberAndCreateDateAndStatus(userSerialNumber, year, month)
                .stream().map(allowance ->{
                    String parentsName = userUtils.getParentsBySerialNumber(allowance.getParentsSerialNumber()).getName();
                    return TemporalChildAllowanceFindAllResponse.of(allowance, parentsName);
                })
                .toList();
    }

    //미승인 용돈 조르기 내역 조회
    public List<UnAcceptTemporalAllowanceFindAllResponse> findUnacceptTemporalAllowances(Long userSerialNumber) {

        return temporalAllowanceRepository.findByChildSerialNumberAndStatus(userSerialNumber, 1)
                .stream().map(allowance ->{
                    String parentsName = userUtils.getParentsBySerialNumber(allowance.getParentsSerialNumber()).getName();
                    return UnAcceptTemporalAllowanceFindAllResponse.of(allowance, parentsName);
                })
                .toList();
    }

    //정기 용돈 조회하기 ( 현재 )
    public List<MonthlyAllowanceFindOneResponse> findChildMonthlyAllowances(Long userSerialNumber) {
        //앞에 tempUser가 맞는지 확인하는거 코드 밑에 getUser사용한다던지
        return monthlyAllowanceRepository.findByChildSerialNumberAndStatus(userSerialNumber, 3)
                .stream().map(allowance ->{
                    String parentsName = userUtils.getParentsBySerialNumber(allowance.getParentsSerialNumber()).getName();
                    long period = ChronoUnit.MONTHS.between(allowance.getCreateDate(), allowance.getDueDate());
                    return MonthlyAllowanceFindOneResponse.of(allowance, (int)period, parentsName);
                })
                .toList();

    }
}
