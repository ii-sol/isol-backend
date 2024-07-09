package shinhan.server_parent.domain.allowance.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_common.domain.account.entity.Account;
import shinhan.server_common.domain.allowance.dto.parents.MonthlyAllowanceFindAllResponse;
import shinhan.server_common.domain.allowance.dto.parents.TemporalAllowanceFindAllResponse;
import shinhan.server_common.domain.allowance.dto.parents.TotalAllowanceFindAllResponse;
import shinhan.server_common.domain.allowance.entity.MonthlyAllowance;
import shinhan.server_common.domain.allowance.entity.TemporalAllowance;
import shinhan.server_common.domain.allowance.repository.MonthlyAllowanceRepository;
import shinhan.server_common.domain.allowance.repository.TemporalAllowanceRepository;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;
import shinhan.server_common.global.scheduler.dto.MonthlyAllowanceScheduleChangeOneRequest;
import shinhan.server_common.global.scheduler.dto.MonthlyAllowanceScheduleSaveOneRequest;
import shinhan.server_common.global.utils.account.AccountUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AllowanceService {

    private final MonthlyAllowanceRepository monthlyAllowanceRepository;
    private final TemporalAllowanceRepository temporalAllowanceRepository;
    private final AccountUtils accountUtils;

    // 부모 용돈 내역 조회하기
    public List<TotalAllowanceFindAllResponse> findTotalAllowances(Long userSerialNumber, Integer year, Integer month, Long childSerialNumber) {

        List<TotalAllowanceFindAllResponse> response = new ArrayList<>();

        List<TemporalAllowance> temporalAllowances = temporalAllowanceRepository.findByUserSerialNumberAndCreateDate(userSerialNumber, year, month, childSerialNumber);
        response.addAll(temporalAllowances.stream()
                .map(TotalAllowanceFindAllResponse::from)
                .toList());

        List<MonthlyAllowance> monthlyAllowances = monthlyAllowanceRepository.findByUserSerialNumberAndCreateDate(userSerialNumber, year, month, childSerialNumber);
        response.addAll(monthlyAllowances.stream()
                .map(TotalAllowanceFindAllResponse::from)
                .toList());

        return response;

    }

    //용돈 조르기 수락/거절
    public void handleAllowanceAcception(Integer temporalAllowanceId ,Boolean accept) {
        // 해당되는 용돈 조르기 가져옴
        TemporalAllowance temporalAllowance = temporalAllowanceRepository.findById(temporalAllowanceId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_TEMPORAL_ALLOWANCE));

        // 수락했으면, 계좌 이체하기 + temporalAllowance의 status 변경
        if(accept){
            Account parentsAccount = accountUtils.getAccountByUserSerialNumberAndStatus(temporalAllowance.getParentsSerialNumber(), 3);
            Account childAccount = accountUtils.getAccountByUserSerialNumberAndStatus(temporalAllowance.getChildSerialNumber(),1);
            accountUtils.transferMoneyByAccount(parentsAccount, childAccount, temporalAllowance.getPrice(), 3);
            temporalAllowance.setStatus(4);// 완료 상태로 바꾸기
        }else{
            temporalAllowance.setStatus(5);// 거절 상태로 바꾸기
        }
    }

    //용돈 조르기 내역 조회하기(아직 승인 안한) - 용돈 페이지
    public List<TemporalAllowanceFindAllResponse> findTemporalAllowances(Long userSerialNumber, Long childSerialNumber) {

        return temporalAllowanceRepository.findByParentsSerialNumberAndChildrenSerialNumberAndStatus(userSerialNumber, childSerialNumber, 1)
                .stream().map(allowance -> {
                    return TemporalAllowanceFindAllResponse.from(allowance);
                })
                .toList();
    }

    //현재 진행중인 정기 용돈 조회하기
    public List<MonthlyAllowanceFindAllResponse> findMonthlyAllowances(Long userSerialNumber, Long childSerialNumber) {
        return monthlyAllowanceRepository.findByParentsSerialNumberAndChildrenSerialNumberAndStatus(userSerialNumber, childSerialNumber, 3)
                .stream().map(allowance -> {
                    return MonthlyAllowanceFindAllResponse.of(allowance, getMonthsBetween(allowance.getCreateDate(), allowance.getDueDate()));
                })
                .toList();
    }

    // 새로운 정기 용돈 repository에 저장하기
    public void createMonthlyAllowance(Long loginUserSerialNumber, LocalDateTime createDate , MonthlyAllowanceScheduleSaveOneRequest request) {
        MonthlyAllowance newMonthlyAllowance = MonthlyAllowance.builder()
                .price(request.getAmount())
                .parentsSerialNumber(loginUserSerialNumber)
                .childSerialNumber(request.getChildSerialNumber())
                .createDate(createDate)
                .dueDate(createDate.plusMonths(request.getPeriod()))
                .status(3)
                .build();
        monthlyAllowanceRepository.save(newMonthlyAllowance);
    }

    public void transmitMoneyforSchedule(Long parentsSerialNumber, Long childSerialNumber, Integer amount){
        System.out.println("scheduling 테스트 중");
        Account parentsAccount = accountUtils.getAccountByUserSerialNumberAndStatus(parentsSerialNumber, 3);
        Account childAccount = accountUtils.getAccountByUserSerialNumberAndStatus(childSerialNumber, 1);
//        System.out.println(parentsAccount);
//        System.out.println(childAccount);

        System.out.println("이체시작");
        accountUtils.transferMoneyByAccount(parentsAccount, childAccount, amount, 4);
    }

    public void changeMonthlyAllowance(Long loginUserSerialNumber, MonthlyAllowanceScheduleChangeOneRequest request) {

        monthlyAllowanceRepository.deleteById(request.getIdBeforeChange());

        MonthlyAllowance changedMonthlyAllowance = MonthlyAllowance.builder()
                .price(request.getAmount())
                .parentsSerialNumber(loginUserSerialNumber)
                .childSerialNumber(request.getChildSerialNumber())
                .createDate(request.getDateBeforeChange())
                .dueDate(request.getDateBeforeChange().plusMonths(request.getPeriod()))
                .status(3)
                .build();
        monthlyAllowanceRepository.save(changedMonthlyAllowance);
    }

    public void deleteMonthlyAllowance(Integer monthlyId) {
        monthlyAllowanceRepository.deleteById(monthlyId);

    }

    // duedate와 startdate의 개월수 차이 구하기
    private Integer getMonthsBetween(LocalDateTime startDateTime, LocalDateTime dueDateTime) {
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = dueDateTime.toLocalDate();
        return Period.between(startDate, endDate).getMonths();
    }
}
