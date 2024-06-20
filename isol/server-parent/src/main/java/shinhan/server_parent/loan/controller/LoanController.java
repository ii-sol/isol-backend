package shinhan.server_parent.loan.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_parent.loan.dto.LoanDto;
import shinhan.server_parent.loan.service.LoanService;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_common.global.utils.ApiUtils.ApiResult;

@RestController
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/loan")
    public ApiUtils.ApiResult<List<LoanDto>> getChildLoan() {
        int childId = 1;
        List<LoanDto> loans = loanService.getLoanByChildId(childId);

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
    public ApiUtils.ApiResult<String> createChildLoan(@RequestBody LoanDto loan) {
        loan.setChildId(1);
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
