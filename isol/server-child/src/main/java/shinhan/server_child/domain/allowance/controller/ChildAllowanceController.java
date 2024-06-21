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
import shinhan.server_common.global.utils.ApiUtils;

import java.util.List;

import static shinhan.server_common.global.utils.ApiUtils.success;


@RestController
@RequestMapping("allowance")
@Slf4j
@RequiredArgsConstructor
public class ChildAllowanceController {

    private final ChildAllowanceService childAllowanceService;
//    //아이 - 용돈 조르기
//    @PostMapping("temporal/create/{psn}")
//    public ApiUtils.ApiResult saveTemporalAllowance(TempUser tempUser, @RequestParam Long psn, @RequestBody TemporalAllowanceSaveOneRequest request){
//        allowanceService.saveTemporalAllowance(tempUser, psn, request);
//        return success(null);
//    }
//
//    //아이 - 용돈 조르기 취소
//    @PostMapping("temporal/{temporalAllowanceId}")
//    public ApiUtils.ApiResult cancleTemporalAllowance(TempUser tempUser, @RequestParam Integer temporalAllowanceId){
//        allowanceService.cancleTemporalAllowance(tempUser, temporalAllowanceId);
//        return success(null);
//    }
//
//    //아이 - 부 모 모두에게서 용돈 조르기 내역 조회(과거, 이미 끝남 - 완료, 취소)
//    @GetMapping("temporal/history")
//    public ApiUtils.ApiResult findTemporalAllowances(TempUser tempUser, @RequestParam Integer year, @RequestParam Integer month){
//        List<TemporalChildAllowanceFindAllResponse> response = allowanceService.findChildTemporalAllowances(tempUser, year, month);
//        return success(response);
//    }
//
//    //아이 - 용돈 조르기 내역 조회 (미승인)
//    @GetMapping("temporal")
//    public ApiUtils.ApiResult findUnacceptTemporalAllowances(TempUser tempUser){
//        List<UnAcceptTemporalAllowanceFindAllResponse> response = allowanceService.findUnacceptTemporalAllowances(tempUser);
//        return success(response);
//    }
//
//    //아이 - 정기 용돈 조회 (현재)
//    @GetMapping("monthly")
//    public ApiUtils.ApiResult findMonthlyAllowance(TempUser tempUser){
//        List<MonthlyAllowanceFindOneResponse> response = allowanceService.findChildMonthlyAllowances(tempUser);
//        return success(response);
//    }

    //아이 - 용돈 조르기
    @PostMapping("temporal")
    public ApiUtils.ApiResult saveTemporalAllowance(@RequestParam("usn") Long usn, @RequestParam("psn") Long psn, @RequestBody TemporalAllowanceSaveOneRequest request) throws AuthException {
        childAllowanceService.saveTemporalAllowance(usn, psn, request);
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
    public ApiUtils.ApiResult findTemporalAllowances(@RequestParam("usn") Long usn, @RequestParam("year") Integer year, @RequestParam("month") Integer month){
        List<TemporalChildAllowanceFindAllResponse> response = childAllowanceService.findChildTemporalAllowances(usn, year, month);
        return success(response);
    }

    //아이 - 용돈 조르기 내역 조회 (미승인)
    @GetMapping("temporal")
    public ApiUtils.ApiResult findUnacceptTemporalAllowances(@RequestParam("usn") Long usn){
        List<UnAcceptTemporalAllowanceFindAllResponse> response = childAllowanceService.findUnacceptTemporalAllowances(usn);
        return success(response);
    }

    //아이 - 정기 용돈 조회 (현재)
    @GetMapping("monthly")
    public ApiUtils.ApiResult findMonthlyAllowance(@RequestParam("usn") Long usn){
        List<MonthlyAllowanceFindOneResponse> response = childAllowanceService.findChildMonthlyAllowances(usn);
        return success(response);
    }
}