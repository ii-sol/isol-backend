package shinhan.server_parent.domain.loan.controller;

import org.springframework.web.bind.annotation.*;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.UserInfoResponse;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_common.global.utils.ApiUtils.ApiResult;
import shinhan.server_common.global.utils.user.UserUtils;
import shinhan.server_parent.domain.loan.dto.LoanDto;
import shinhan.server_parent.domain.loan.service.LoanService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LoanController {

    private final LoanService loanService;
    private final JwtService jwtService;
    private final UserUtils userUtils;

    public LoanController(LoanService loanService, JwtService jwtService, UserUtils userUtils) {
        this.loanService = loanService;
        this.jwtService = jwtService;
        this.userUtils = userUtils;
    }

    @GetMapping("/api/loan/{childSn}")
    public ApiUtils.ApiResult<List<LoanDto>> getChildLoan(@PathVariable long childSn) throws AuthException {

        List<LoanDto> loans = loanService.getLoanByChildId(childSn);

        for (LoanDto loan : loans) {
            loan.setChildName(userUtils.getNameBySerialNumber(childSn));
            loan.setParentName(userUtils.getNameBySerialNumber(loan.getParentId()));
        }
        return ApiUtils.success(loans);
    }

    @PostMapping("/api/loan/accept")
    public ApiUtils.ApiResult<String> acceptChildLoan(@RequestParam int loanId) {
        loanService.acceptLoan(loanId);
        return ApiUtils.success("Loan accepted successfully");
    }

    @PostMapping("/api/loan/refuse")
    public ApiUtils.ApiResult<String> refuseChildLoan(@RequestParam int loanId) {
        loanService.refuseLoan(loanId);
        return ApiUtils.success("Loan refused successfully");
    }

    @GetMapping("/api/loan/detail/{loanId}")
    public ApiResult<LoanDto> getChildLoan(@PathVariable int loanId) {
        LoanDto loanDto = loanService.findOne(loanId);
        return ApiUtils.success(loanDto);
    }
}
