package shinhan.server_child.domain.allowance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_child.domain.allowance.dto.MonthlyAllowanceFindOneResponse;
import shinhan.server_child.domain.allowance.dto.TemporalAllowanceSaveOneRequest;
import shinhan.server_child.domain.allowance.dto.TemporalChildAllowanceFindAllResponse;
import shinhan.server_child.domain.allowance.dto.UnAcceptTemporalAllowanceFindAllResponse;
import shinhan.server_child.domain.allowance.entity.ChildTemporalAllowance;
import shinhan.server_child.domain.allowance.repository.ChildMonthlyAllowanceRepository;
import shinhan.server_child.domain.allowance.repository.ChildTemporalAllowanceRepository;
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
public class ChildAllowanceService {

    private final ChildTemporalAllowanceRepository childTemporalAllowanceRepository;
    private final ChildMonthlyAllowanceRepository childMonthlyAllowanceRepository;
    private final UserUtils userUtils;

    //자식 - 용돈 조르기 신청 여기서 tempUser = 자식
    public void saveTemporalAllowance(Long userSerialNumber, Long psn, TemporalAllowanceSaveOneRequest request) {
        Parents parents = userUtils.getParentsBySerialNumber(psn);
        Child child = userUtils.getChildBySerialNumber(userSerialNumber);
        // TemporalAllowance 객체 생성
        ChildTemporalAllowance childTemporalAllowance = ChildTemporalAllowance.builder()
                .parents(parents)
                .child(child)
                .content(request.getContent())
                .price(request.getAmount())
                .createDate(LocalDateTime.now())
                .status(1)
                .build();

        childTemporalAllowanceRepository.save(childTemporalAllowance);

    }

    //자식 - 용돈 조르기 취소하기
    public void cancleTemporalAllowance(Integer temporalAllowanceId) {
        ChildTemporalAllowance findChildTemporalAllowance = childTemporalAllowanceRepository.findById(temporalAllowanceId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_TEMPORAL_ALLOWANCE));

        findChildTemporalAllowance.setStatus(6);
        childTemporalAllowanceRepository.save(findChildTemporalAllowance);
    }

    //자식 용돈 조르기 내역 조회 (과거)
    public List<TemporalChildAllowanceFindAllResponse> findChildTemporalAllowances(Long userSerialNumber, Integer year, Integer month) {

        return childTemporalAllowanceRepository.findByChildSerialNumberAndCreateDateAndStatus(userSerialNumber, year, month)
                .stream().map(allowance ->{
                    return TemporalChildAllowanceFindAllResponse.of(allowance, allowance.getParents().getName());
                })
                .toList();
    }

    //미승인 용돈 조르기 내역 조회
    public List<UnAcceptTemporalAllowanceFindAllResponse> findUnacceptTemporalAllowances(Long userSerialNumber) {
        return childTemporalAllowanceRepository.findByChildSerialNumAndStatus(userSerialNumber, 1)
                .stream().map(allowance ->{
                    return UnAcceptTemporalAllowanceFindAllResponse.of(allowance, allowance.getParents().getName());
                })
                .toList();
    }

    //정기 용돈 조회하기 ( 현재 )
    public List<MonthlyAllowanceFindOneResponse> findChildMonthlyAllowances(Long userSerialNumber) {
        //앞에 tempUser가 맞는지 확인하는거 코드 밑에 getUser사용한다던지
        return childMonthlyAllowanceRepository.findByChildSerialNumAndStatus(userSerialNumber, 3)
                .stream().map(allowance ->{
                    long period = ChronoUnit.MONTHS.between(allowance.getCreateDate(), allowance.getDueDate());
                    return MonthlyAllowanceFindOneResponse.of(allowance, (int)period);
                })
                .toList();

    }
}
