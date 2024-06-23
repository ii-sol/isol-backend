package shinhan.server_child.domain.loan.controller;

import jakarta.security.auth.message.AuthException;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_child.domain.loan.dto.LoanDto;
import shinhan.server_child.domain.loan.service.LoanService;
import shinhan.server_child.domain.user.service.UserService;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.UserInfoResponse;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_common.global.utils.ApiUtils.ApiResult;

@RestController
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService, JwtService jwtService, UserService userService) {
        this.loanService = loanService;
    }

    @GetMapping("/loan")
    public ApiUtils.ApiResult<List<LoanDto>> getChildLoan() throws AuthException {


        List<LoanDto> loans = loanService.getLoanByChildId(ChildId);

        for(LoanDto loan : loans) {
            loan.setParentName("엄마");
        }
        return ApiUtils.success(loans);
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


    @GetMapping("/loan/detail/{loanId}")
    public ApiResult<LoanDto> getChildLoan(@PathVariable int loanId) {
        LoanDto loanDto = loanService.findOne(loanId);

        return ApiUtils.success(loanDto);
    }
}
