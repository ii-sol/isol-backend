package shinhan.server_parent.domain.loan.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.UserInfoResponse;
import shinhan.server_parent.domain.loan.dto.LoanDto;
import shinhan.server_parent.domain.loan.service.LoanService;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_common.global.utils.ApiUtils.ApiResult;

@RestController
public class LoanController {

    private final LoanService loanService;
    private final JwtService jwtService;

    public LoanController(LoanService loanService, JwtService jwtService) {
        this.loanService = loanService;
        this.jwtService = jwtService;
    }

    @GetMapping("/loan")
    public ApiUtils.ApiResult<List<LoanDto>> getChildLoan() throws AuthException {
        UserInfoResponse userInfoResponse = jwtService.getUserInfo();

        Long ChildId = userInfoResponse.getSn();

        List<LoanDto> loans = loanService.getLoanByChildId(ChildId);

        for(LoanDto loan : loans) {
            loan.setParentName("엄마");
        }
        return ApiUtils.success(loans);
    }

    @GetMapping("/")
    public String Hello(){
        return "hello";
    }

    @PostMapping("/child/loan/create")
    public ApiUtils.ApiResult<String> createChildLoan(@RequestBody LoanDto loan)
        throws AuthException {
        UserInfoResponse userInfoResponse = jwtService.getUserInfo();

        Long ChildId = userInfoResponse.getSn();
        loan.setChildId(ChildId);

        loan.setInterestRate(4.5F);
        loan.setStatus(1);
        loan.setParentName("엄마");

        loanService.saveLoan(loan);

        return ApiUtils.success("Loan created successfully");
    }

    @PostMapping("/loan/accept")
    public ApiUtils.ApiResult<String> acceptChildLoan(@RequestParam int loanId) {
        loanService.acceptLoan(loanId);

        return ApiUtils.success("Loan accepted successfully");
    }

    @PostMapping("/loan/refuse")
    public ApiUtils.ApiResult<String> refuseChildLoan(@RequestParam int loanId) {
        loanService.refuseLoan(loanId);

        return ApiUtils.success("Loan refused successfully");
    }

    @GetMapping("/loan/detail/{loanId}")
    public ApiResult<LoanDto> getChildLoan(@PathVariable int loanId) {
        LoanDto loanDto = loanService.findOne(loanId);

        return ApiUtils.success(loanDto);
    }
}
