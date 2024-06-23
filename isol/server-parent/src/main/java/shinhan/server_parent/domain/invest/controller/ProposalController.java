package shinhan.server_parent.domain.invest.controller;

import jakarta.security.auth.message.AuthException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.FamilyInfoResponse;
import shinhan.server_common.global.utils.ApiResult;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_parent.domain.user.service.UserService;

@Slf4j
@RequestMapping("/proposal")
@RestController
public class ProposalController {

    InvestProposalService investProposalService;
    UserService userService;

    JwtService jwtService;
    @Autowired
    ProposalController(InvestProposalService investProposalService,UserService userService, JwtService jwtService){
        this.investProposalService = investProposalService;
        this.userService = userService;
        this.jwtService = jwtService;
    }
    //투자 제안 내역 달변 조회하기(아이)
    @GetMapping("/invest/history/{status}")
    public ApiUtils.ApiResult getInvestProposal(@RequestParam("year") int year,
        @RequestParam("month") int month,@PathVariable("status") short status
    ) throws AuthException {
        long userSn = jwtService.getUserInfo().getSn();
        Map<Long,String> family = new HashMap<>();
        for(FamilyInfoResponse familyInfoResponse : jwtService.getUserInfo().getFamilyInfo())
        {
            family.put(familyInfoResponse.getSn(),familyInfoResponse.getName());
        }
        List<InvestProposalHistoryResponse> result = investProposalService.getProposalInvestHistory(userSn,year,month,status);
        for(InvestProposalHistoryResponse investProposalHistoryResponse : result){
            investProposalHistoryResponse.setParentAlias(family.get(Long.parseLong(investProposalHistoryResponse.getParentAlias())));
        }
        return ApiUtils.success(result);
    }

    //전체 제안 내역 달별 조회하기(아이)
    @GetMapping("/history")
    public ApiResult getProposal(@RequestParam("year") int year, @RequestParam("month") int month) {
        return ApiResult.responseSuccess(year);
    }


    //투자 제안하기
    @PostMapping("/invest/{psn}")
    public ApiUtils.ApiResult proposeInvest(@PathVariable("psn") Long parentSn,@RequestBody
    InvestProposalSaveRequest investProposalSaveRequest) throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        boolean checkParent = false;
        for (FamilyInfoResponse familyInfoResponse : jwtService.getUserInfo().getFamilyInfo()) {
            System.out.println(familyInfoResponse.getName());
            if (familyInfoResponse.getSn() == parentSn) {
                checkParent = true;
                break;
            }
        }
        if(!checkParent)
        {
            throw new CustomException(ErrorCode.FAILED_NO_PARENT);
        }
        Long result = investProposalService.proposalInvest(loginUserSerialNumber,parentSn,investProposalSaveRequest);
        return ApiUtils.success("성공했습니다.");
    }
}
