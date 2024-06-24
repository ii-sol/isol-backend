package shinhan.server_parent.domain.allowance.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_parent.domain.allowance.dto.MonthlyAllowanceFindAllResponse;
import shinhan.server_parent.domain.allowance.dto.TemporalAllowanceFindAllResponse;
import shinhan.server_parent.domain.allowance.dto.TotalAllowanceFindAllResponse;
import shinhan.server_parent.domain.allowance.service.AllowanceService;

import java.util.List;

import static shinhan.server_common.global.utils.ApiUtils.success;

@RestController
@RequestMapping("allowance")
@Slf4j
@RequiredArgsConstructor
public class AllowanceController {

    private final AllowanceService allowanceService;
    private final JwtService jwtService;

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
}
