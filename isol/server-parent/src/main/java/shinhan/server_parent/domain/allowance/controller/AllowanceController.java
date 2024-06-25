package shinhan.server_parent.domain.allowance.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shinhan.server_common.domain.account.entity.Account;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;
import shinhan.server_common.global.scheduler.DynamicScheduler;
import shinhan.server_common.global.scheduler.dto.MonthlyAllowanceScheduleChangeOneRequest;
import shinhan.server_common.global.scheduler.dto.MonthlyAllowanceScheduleSaveOneRequest;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_common.global.utils.account.AccountUtils;
import shinhan.server_common.global.utils.scheduler.SchedulerUtils;
import shinhan.server_parent.domain.allowance.dto.MonthlyAllowanceFindAllResponse;
import shinhan.server_parent.domain.allowance.dto.TemporalAllowanceFindAllResponse;
import shinhan.server_parent.domain.allowance.dto.TotalAllowanceFindAllResponse;
import shinhan.server_parent.domain.allowance.service.AllowanceService;

import java.time.LocalDateTime;
import java.util.List;

import static shinhan.server_common.global.utils.ApiUtils.success;

@RestController
@RequestMapping("/api/allowance")
@Slf4j
@RequiredArgsConstructor
public class AllowanceController {

    private final AllowanceService allowanceService;
    private final JwtService jwtService;
    private final DynamicScheduler dynamicScheduler;
    private final SchedulerUtils schedulerUtils;
    private final AccountUtils accountUtils;

    //부모 - 전체 용돈 내역 조회하기
    @GetMapping("history")
    public ApiUtils.ApiResult findTotalAllowances(@RequestParam("year") Integer year, @RequestParam("month") Integer month, @RequestParam("csn") Long csn) throws AuthException, AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        List<TotalAllowanceFindAllResponse> response = allowanceService.findTotalAllowances(loginUserSerialNumber, year, month, csn);
        return success(response);
    }

    //부모 - 용돈 조르기 수락 거절
    @PostMapping("temporal")
    public ApiUtils.ApiResult handleAllowanceAcception(@RequestParam("tempId") Integer temporalAllowanceId, @RequestParam("accept") Boolean accept) throws AuthException {
        allowanceService.handleAllowanceAcception(temporalAllowanceId, accept);
        return success(null);
    }

    //부모 - 용돈 조르기 내역 조회하기 (미승인된)
    @GetMapping("history/temporal/{csn}")
    public ApiUtils.ApiResult findTemporalAllowances(@PathVariable("csn") Long csn) throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        List<TemporalAllowanceFindAllResponse> response = allowanceService.findTemporalAllowances(loginUserSerialNumber, csn);
        return success(response);
    }

    //부모 - 정기 용돈 조회하기 (현재 진행중인)
    @GetMapping("monthly/{csn}")
    public ApiUtils.ApiResult findMontlyAllowances(@PathVariable("csn") Long csn) throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        List<MonthlyAllowanceFindAllResponse> response = allowanceService.findMonthlyAllowances(loginUserSerialNumber, csn);
        return success(response);
    }

    //부모 정기 용돈 생성하기
    @PostMapping("monthly")
    public ApiUtils.ApiResult saveMonthlyAllowance(@RequestBody MonthlyAllowanceScheduleSaveOneRequest request) throws jakarta.security.auth.message.AuthException, AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        String taskId = makeTaskId(loginUserSerialNumber, request.getChildSerialNumber());

        LocalDateTime createDate = LocalDateTime.now();
        String cronExpression = schedulerUtils.generateTransmitCronExpression(createDate);

        //TODO: 리팩토링 필수
        Account parentsAccount = accountUtils.getAccountByUserSerialNumberAndStatus(loginUserSerialNumber, 3);
        if(parentsAccount.getBalance() < request.getAmount()){
            throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        allowanceService.createMonthlyAllowance(loginUserSerialNumber, createDate, request);
        dynamicScheduler.scheduleTask(taskId, cronExpression, request.getPeriod(),() -> {
            allowanceService.transmitMoneyforSchedule(loginUserSerialNumber, request.getChildSerialNumber(), request.getAmount());
        });
        return success(null);
    }

    //부모 정기 용돈 변경하기
    @PostMapping("monthly/change")
    public ApiUtils.ApiResult changeMonthlyAllowance(@RequestBody MonthlyAllowanceScheduleChangeOneRequest request) throws jakarta.security.auth.message.AuthException, AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        String taskId = makeTaskId(loginUserSerialNumber, request.getChildSerialNumber());

        String cronExpression = schedulerUtils.retestGenerateCronExpression(request.getDateBeforeChange());
        allowanceService.changeMonthlyAllowance(loginUserSerialNumber, request);

        dynamicScheduler.rescheduleTask(taskId, cronExpression, request.getPeriod(),() -> {
            allowanceService.transmitMoneyforSchedule(loginUserSerialNumber, request.getChildSerialNumber(), request.getAmount());
        });
        return success(null);
    }

    //부모 정기 용돈 해제하기
    @DeleteMapping()
    public ApiUtils.ApiResult deleteMonthlyAllowance(@RequestParam("monthlyId") Integer monthlyId, @RequestParam("csn") Long csn) throws jakarta.security.auth.message.AuthException, AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        String taskId = makeTaskId(loginUserSerialNumber, csn);

        allowanceService.deleteMonthlyAllowance(monthlyId);
        dynamicScheduler.stopScheduledTask(taskId);
        return success(null);
    }

    private static String makeTaskId(Long loginUserSerialNumber, Long childSerialNumber) {
        return loginUserSerialNumber.toString() + childSerialNumber.toString();
    }
}
