package shinhan.server_child.domain.allowance.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shinhan.server_child.domain.allowance.dto.MonthlyAllowanceFindOneResponse;
import shinhan.server_child.domain.allowance.dto.TemporalAllowanceSaveOneRequest;
import shinhan.server_child.domain.allowance.dto.TemporalChildAllowanceFindAllResponse;
import shinhan.server_child.domain.allowance.dto.UnAcceptTemporalAllowanceFindAllResponse;
import shinhan.server_child.domain.allowance.service.AllowanceService;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiUtils;

import java.util.List;

import static shinhan.server_common.global.utils.ApiUtils.success;


@RestController
@RequestMapping("allowance")
@Slf4j
@RequiredArgsConstructor
public class AllowanceController {

    private final AllowanceService allowanceService;
    private final JwtService jwtService;

    //아이 - 용돈 조르기
    @PostMapping("temporal/{psn}")
    public ApiUtils.ApiResult saveTemporalAllowance(@PathVariable("psn") Long psn, @RequestBody TemporalAllowanceSaveOneRequest request) throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        allowanceService.saveTemporalAllowance(loginUserSerialNumber, psn, request);
        return success(null);
    }

    //아이 - 용돈 조르기 취소
    @PostMapping("temporal/cancle/{tempId}")
    public ApiUtils.ApiResult cancleTemporalAllowance(@PathVariable("tempId") Integer temporalAllowanceId) {
        allowanceService.cancleTemporalAllowance(temporalAllowanceId);
        return success(null);
    }

    //아이 - 부 모 모두에게서 용돈 조르기 내역 조회(과거, 이미 끝남 - 완료, 취소)
    @GetMapping("temporal/history")
    public ApiUtils.ApiResult findTemporalAllowances(@RequestParam("year") Integer year, @RequestParam("month") Integer month) throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        List<TemporalChildAllowanceFindAllResponse> response = allowanceService.findChildTemporalAllowances(loginUserSerialNumber, year, month);
        return success(response);
    }

    //아이 - 용돈 조르기 내역 조회 (미승인)
    @GetMapping("temporal")
    public ApiUtils.ApiResult findUnacceptTemporalAllowances() throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        List<UnAcceptTemporalAllowanceFindAllResponse> response = allowanceService.findUnacceptTemporalAllowances(loginUserSerialNumber);
        return success(response);
    }

    //아이 - 정기 용돈 조회 (현재)
    @GetMapping("monthly")
    public ApiUtils.ApiResult findMonthlyAllowance() throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        List<MonthlyAllowanceFindOneResponse> response = allowanceService.findChildMonthlyAllowances(loginUserSerialNumber);
        return success(response);
    }
}
