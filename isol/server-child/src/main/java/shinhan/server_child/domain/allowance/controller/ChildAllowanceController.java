package shinhan.server_child.domain.allowance.controller;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shinhan.server_child.domain.allowance.dto.MonthlyAllowanceFindOneResponse;
import shinhan.server_child.domain.allowance.dto.TemporalAllowanceSaveOneRequest;
import shinhan.server_child.domain.allowance.dto.TemporalChildAllowanceFindAllResponse;
import shinhan.server_child.domain.allowance.dto.UnAcceptTemporalAllowanceFindAllResponse;
import shinhan.server_child.domain.allowance.service.ChildAllowanceService;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiUtils;

import java.util.List;

import static shinhan.server_common.global.utils.ApiUtils.success;


@RestController
@RequestMapping("allowance")
@Slf4j
@RequiredArgsConstructor
public class ChildAllowanceController {

    private final ChildAllowanceService childAllowanceService;
    private final JwtService jwtService;

    //아이 - 용돈 조르기
    @PostMapping("temporal")
    public ApiUtils.ApiResult saveTemporalAllowance(@RequestParam("psn") Long psn, @RequestBody TemporalAllowanceSaveOneRequest request) throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        childAllowanceService.saveTemporalAllowance(loginUserSerialNumber, psn, request);
        return success(null);
    }

    //아이 - 용돈 조르기 취소
    @PostMapping("temporal/{temporalAllowanceId}")
    public ApiUtils.ApiResult cancleTemporalAllowance( @PathVariable("temporalAllowanceId") Integer temporalAllowanceId){
        childAllowanceService.cancleTemporalAllowance(temporalAllowanceId);
        return success(null);
    }

    //아이 - 부 모 모두에게서 용돈 조르기 내역 조회(과거, 이미 끝남 - 완료, 취소)
    @GetMapping("temporal/history")
    public ApiUtils.ApiResult findTemporalAllowances(@RequestParam("year") Integer year, @RequestParam("month") Integer month) throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        List<TemporalChildAllowanceFindAllResponse> response = childAllowanceService.findChildTemporalAllowances(loginUserSerialNumber, year, month);
        return success(response);
    }

    //아이 - 용돈 조르기 내역 조회 (미승인)
    @GetMapping("temporal")
    public ApiUtils.ApiResult findUnacceptTemporalAllowances() throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        List<UnAcceptTemporalAllowanceFindAllResponse> response = childAllowanceService.findUnacceptTemporalAllowances(loginUserSerialNumber);
        return success(response);
    }

    //아이 - 정기 용돈 조회 (현재)
    @GetMapping("monthly")
    public ApiUtils.ApiResult findMonthlyAllowance() throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        List<MonthlyAllowanceFindOneResponse> response = childAllowanceService.findChildMonthlyAllowances(loginUserSerialNumber);
        return success(response);
    }
}
