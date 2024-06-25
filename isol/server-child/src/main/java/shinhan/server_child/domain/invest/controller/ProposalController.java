package shinhan.server_child.domain.invest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shinhan.server_child.domain.invest.service.InvestProposalServiceChild;
import shinhan.server_child.domain.user.service.UserService;
import shinhan.server_common.domain.invest.dto.InvestProposalDetailResponse;
import shinhan.server_common.domain.invest.dto.InvestProposalHistoryResponse;
import shinhan.server_common.domain.invest.dto.InvestProposalSaveRequest;
import shinhan.server_common.domain.invest.investEntity.InvestProposal;
import shinhan.server_common.domain.invest.investEntity.InvestProposalResponse;
import shinhan.server_common.domain.invest.service.InvestProposalService;
import shinhan.server_common.domain.invest.dto.StockFindDetailResponse;
import shinhan.server_common.domain.invest.service.StockService;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.FamilyInfoResponse;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_common.global.utils.ApiUtils.ApiResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/proposal")
public class ProposalController {

    InvestProposalService investProposalService;
    InvestProposalServiceChild investProposalServiceChild;
    UserService userService;
    StockService stockService;
    JwtService jwtService;

    @Autowired
    ProposalController(InvestProposalService investProposalService, UserService userService,
                       JwtService jwtService, StockService stockService, InvestProposalServiceChild investProposalServiceChild) {
        this.investProposalService = investProposalService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.stockService = stockService;
        this.investProposalServiceChild = investProposalServiceChild;
    }

    //투자 제안 내역 달변 조회하기(아이)
    @GetMapping("/invest/history/{status}")
    public ApiResult getInvestProposal(@RequestParam("year") int year,
                                       @RequestParam("month") int month, @PathVariable("status") short status
    ) throws AuthException {
        long userSn = jwtService.getUserInfo().getSn();
        Map<Long, String> family = new HashMap<>();
        for (FamilyInfoResponse familyInfoResponse : jwtService.getUserInfo().getFamilyInfo()) {
            family.put(familyInfoResponse.getSn(), familyInfoResponse.getName());
        }
        List<InvestProposalHistoryResponse> result = investProposalServiceChild.getProposalInvestHistory(
                userSn, year, month, status);
        for (InvestProposalHistoryResponse investProposalHistoryResponse : result) {
            investProposalHistoryResponse.setRecieverName(
                    family.get(Long.parseLong(investProposalHistoryResponse.getRecieverName())));
        }
        return ApiUtils.success(result);
    }

    //투자 제안 디테일
    @GetMapping("/invest/{proposalId}/{year}")
    public ApiResult getInvestProposalDetail(@PathVariable("proposalId") int proposalId,
                                             @PathVariable("year") String yaer)
            throws AuthException {
        long userSn = jwtService.getUserInfo().getSn();
        InvestProposal proposalInvestDetail = investProposalServiceChild.getProposalInvestDetail(
                proposalId, userSn);
        StockFindDetailResponse stockDetail = stockService.getStockDetail2(
                proposalInvestDetail.getTicker(), yaer);
        InvestProposalResponse investProposalResponse = null;
        if (proposalInvestDetail.getStatus() == 5) {
            investProposalResponse = investProposalService.getInvestProposalResponse(proposalId);
        }
        return ApiUtils.success(InvestProposalDetailResponse.builder().companyInfo(stockDetail)
                .requestProposal(proposalInvestDetail).responseProposal(investProposalResponse)
                .build());
    }

    //투자 제안하기
    @PostMapping("/invest/{psn}")
    public ApiResult proposeInvest(@PathVariable("psn") Long parentSn, @RequestBody
    InvestProposalSaveRequest investProposalSaveRequest) throws AuthException {
        if(!jwtService.isMyFamily(parentSn)){
            throw new CustomException(ErrorCode.FAILED_NO_PARENT);
        }
        Long result = investProposalServiceChild.proposalInvest(jwtService.getUserInfo().getSn(), parentSn,
            investProposalSaveRequest);
        return ApiUtils.success("성공했습니다.");
    }
}