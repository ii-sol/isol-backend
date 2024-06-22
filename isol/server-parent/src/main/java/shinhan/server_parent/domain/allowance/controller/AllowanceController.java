package shinhan.server_parent.domain.allowance.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
    //부모 - 전체 용돈 내역 조회하기
    @GetMapping("history")
    public ApiUtils.ApiResult findTotalAllowances(@RequestParam("usn") Long usn, @RequestParam("year") Integer year, @RequestParam("month") Integer month, @RequestParam("csn") Long csn){
        System.out.println("dnfalkfjlak");
        log.info("Received request with usn: {}, year: {}, month: {}, csn: {}", usn, year, month, csn);
        List<TotalAllowanceFindAllResponse> response = allowanceService.findTotalAllowances(usn, year, month, csn);
        return success(response);
    }

    //부모 - 용돈 조르기 수락 거절
    @PostMapping("temporal")
    public ApiUtils.ApiResult handleAllowanceAcception(@RequestParam("usn") Long usn, @RequestParam("temporalAllowanceId") Integer temporalAllowanceId, @RequestParam("accept") Boolean accept){
        allowanceService.handleAllowanceAcception(usn, temporalAllowanceId, accept);
        return success(null);
    }

    //부모 - 용돈 조르기 내역 조회하기 (미승인된)
    @GetMapping("history/temporal")
    public ApiUtils.ApiResult findTemporalAllowances(@RequestParam("usn") Long usn, @RequestParam("csn") Long csn){
        List<TemporalAllowanceFindAllResponse> response = allowanceService.findTemporalAllowances(usn, csn);
        return success(response);
    }

    //부모 - 정기 용돈 조회하기 (현재 진행중인)
    @GetMapping("monthly")
    public ApiUtils.ApiResult findMontlyAllowances(@RequestParam("usn") Long usn, @RequestParam("csn") Long csn){
        List<MonthlyAllowanceFindAllResponse> response = allowanceService.findMonthlyAllowances(usn, csn);
        return success(response);
    }
}
