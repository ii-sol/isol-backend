package shinhan.server_child.domain.loan.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_child.domain.loan.dto.LoanDto;
import shinhan.server_child.domain.loan.service.LoanService;
import shinhan.server_child.domain.user.service.UserService;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.UserInfoResponse;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_common.global.utils.ApiUtils.ApiResult;

@RestController
public class LoanController {

    private final LoanService loanService;
    private final JwtService jwtService;
    private final UserService userService;

    public LoanController(LoanService loanService, JwtService jwtService, UserService userService) {
        this.loanService = loanService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("/loan")
    public ApiUtils.ApiResult<List<LoanDto>> getChildLoan() throws AuthException {

        Long childId = jwtService.getUserInfo().getSn();

        List<LoanDto> loans = loanService.getLoanByChildId(childId);


        for(LoanDto loan : loans) {
            String parentName = userService.getParentsAlias(childId, loan.getParentId());
            loan.setParentName(parentName);
        }
        return ApiUtils.success(loans);
    }

    @PostMapping("/child/loan/create")
    public ApiUtils.ApiResult<String> createChildLoan(@RequestBody LoanDto loan)
        throws AuthException {

        UserInfoResponse userInfoResponse = jwtService.getUserInfo();

        Long ChildId = userInfoResponse.getSn();
        loan.setChildId(ChildId);


        loan.setStatus(1);
        loan.setParentName("엄마");

        loanService.saveLoan(loan);

        return ApiUtils.success("Loan created successfully");
    }


    @GetMapping("/loan/detail/{loanId}")
    public ApiResult<LoanDto> getChildLoan(@PathVariable int loanId) {


        LoanDto loanDto = loanService.findOne(loanId);

        loanDto.setParentName(userService.getParentsAlias(loanDto.getChildId(), loanDto.getParentId()));
        return ApiUtils.success(loanDto);
    }

    @GetMapping("loan/credit")
    public ApiResult<Integer> getChildCredit() throws AuthException {

        long childId = jwtService.getUserInfo().getSn();

        int childScore =userService.getChild(childId).getScore();

        return ApiUtils.success(childScore);
    }
}
