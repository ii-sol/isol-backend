package shinhan.server_child.domain.invest.controller;


import jakarta.security.auth.message.AuthException;
import java.util.HashMap;
import java.util.List;
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
import shinhan.server_child.domain.invest.dto.InvestProposalDetailResponse;
import shinhan.server_child.domain.invest.dto.InvestProposalHistoryResponse;
import shinhan.server_child.domain.invest.dto.InvestProposalSaveRequest;
import shinhan.server_child.domain.invest.entity.InvestProposal;
import shinhan.server_child.domain.invest.entity.InvestProposalResponse;
import shinhan.server_child.domain.invest.service.InvestProposalService;
import shinhan.server_child.domain.user.service.UserService;
import shinhan.server_common.domain.invest.dto.StockFindDetailResponse;
import shinhan.server_common.domain.invest.service.StockService;
import shinhan.server_common.domain.user.dto.ParentsFindOneResponse;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.FamilyInfoResponse;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_common.global.utils.ApiUtils.ApiResult;

@RestController
@Slf4j
@RequestMapping("/proposal")
public class ProposalController {

    InvestProposalService investProposalService;
    UserService userService;
    StockService stockService;
    JwtService jwtService;
    @Autowired
    ProposalController(InvestProposalService investProposalService,UserService userService, JwtService jwtService,StockService stockService){
        this.investProposalService = investProposalService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.stockService = stockService;
    }
//    //투자 제안 내역 달변 조회하기(부모)
//    @GetMapping("/invest/history/{co}")
//    public ApiResult getChildInvestProposal(@RequestParam("year") int year,
//        @RequestParam("month") int month, @PathVariable("co") int childOrder) {
//        ApiResult<Integer> stringApiResult = ApiResult.responseSuccess(childOrder);
//        return stringApiResult;
//    }

    //투자 제안 내역 달변 조회하기(아이)
    @GetMapping("/invest/history/{status}")
    public ApiResult getInvestProposal(@RequestParam("year") int year,
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

    @GetMapping("/invest/{proposalId}/{year}")
    public ApiResult getInvestProposalDetail(@PathVariable("proposalId") int proposalId,@PathVariable("year") String yaer)
        throws AuthException {
        long userSn = jwtService.getUserInfo().getSn();
        InvestProposal proposalInvestDetail = investProposalService.getProposalInvestDetail(
            proposalId, userSn);

        StockFindDetailResponse stockDetail = stockService.getStockDetail2(
            proposalInvestDetail.getTicker(), yaer);
        InvestProposalResponse investProposalResponse = null;
        if(proposalInvestDetail.getStatus()==5){
            investProposalResponse = investProposalService.getInvestProposalResponse(proposalId);
        }
        return ApiUtils.success(InvestProposalDetailResponse.builder().companyInfo(stockDetail).requestProposal(proposalInvestDetail).responseProposal(investProposalResponse).build());
    }

    //투자 제안하기
    @PostMapping("/invest/{psn}")
    public ApiResult proposeInvest(@PathVariable("psn") Long parentSn,@RequestBody
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
