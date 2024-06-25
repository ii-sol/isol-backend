package shinhan.server_parent.domain.invest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_common.domain.account.entity.Account;
import shinhan.server_common.domain.invest.dto.InvestProposalDetailResponse;
import shinhan.server_common.domain.invest.dto.InvestProposalHistoryResponse;
import shinhan.server_common.domain.invest.dto.InvestStockRequest;
import shinhan.server_common.domain.invest.dto.StockFindDetailResponse;
import shinhan.server_common.domain.invest.investEntity.InvestProposal;
import shinhan.server_common.domain.invest.investEntity.InvestProposalResponse;
import shinhan.server_common.domain.invest.service.InvestProposalService;
import shinhan.server_common.domain.invest.service.InvestService;
import shinhan.server_common.domain.invest.service.StockService;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.security.dto.FamilyInfoResponse;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_common.global.utils.ApiUtils.ApiResult;
import shinhan.server_common.global.utils.account.AccountUtils;
import shinhan.server_parent.domain.invest.dto.ResponseInvestProposal;
import shinhan.server_parent.domain.invest.service.InvestProposalServiceParent;
import shinhan.server_parent.domain.user.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/proposal")
public class ProposalController {

    InvestProposalService investProposalService;
    UserService userService;
    StockService stockService;
    JwtService jwtService;
    InvestProposalServiceParent investProposalServiceParent;
    InvestService investService;
    AccountUtils accountUtils;

    @Autowired
    ProposalController(InvestProposalService investProposalService, UserService userService,
        JwtService jwtService, StockService stockService,
        InvestProposalServiceParent investProposalServiceParent,
        InvestService investService, AccountUtils accountUtils
    ) {
        this.investProposalService = investProposalService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.stockService = stockService;
        this.investProposalServiceParent = investProposalServiceParent;
        this.investService = investService;
        this.accountUtils = accountUtils;
    }

    //투자 제안 내역 달변 조회하기
    @GetMapping("/invest/history/{status}")
    public ApiResult getInvestProposal(@RequestParam("year") int year,
        @RequestParam("month") int month, @PathVariable("status") short status,
        @RequestParam("csn") Long csn
    ) throws AuthException, AuthException {
        Map<Long, String> family = new HashMap<>();
        for (FamilyInfoResponse familyInfoResponse : jwtService.getUserInfo().getFamilyInfo()) {
            family.put(familyInfoResponse.getSn(), familyInfoResponse.getName());
        }
        String string = family.get(csn);
        if (string == null) {
            throw new CustomException(ErrorCode.FAILED_NO_CHILD);
        }
        List<InvestProposalHistoryResponse> result = investProposalServiceParent.getProposalInvestHistory(
            jwtService.getUserInfo().getSn(), csn, year, month, status);
        for (InvestProposalHistoryResponse investProposalHistoryResponse : result) {
            investProposalHistoryResponse.setRecieverName(string);
        }
        return ApiUtils.success(result);
    }

    //투자제안 상세보기
    @GetMapping("/invest/{proposalId}/{year}")
    public ApiResult getInvestProposalDetail(@PathVariable("proposalId") int proposalId,
        @PathVariable("year") String year)
        throws AuthException {
        Long psn = jwtService.getUserInfo().getSn();
        InvestProposal proposalInvestDetail = investProposalServiceParent.getProposalInvestDetail(
            proposalId, psn);
        StockFindDetailResponse stockDetail = stockService.getStockDetail2(
            proposalInvestDetail.getTicker(), year);
        InvestProposalResponse investProposalResponse = null;
        if (proposalInvestDetail.getStatus() == 5) {
            investProposalResponse = investProposalService.getInvestProposalResponse(proposalId);
        }
        return ApiUtils.success(InvestProposalDetailResponse.builder().companyInfo(stockDetail)
            .requestProposal(proposalInvestDetail).responseProposal(investProposalResponse)
            .build());
    }

    //투자 수락 및 거절하기
    @PostMapping("/invest/{proposalId}")
    public ApiUtils.ApiResult responseProposal(@PathVariable("proposalId") int proposalId,
        @RequestParam("csn") Long csn,
        @RequestBody ResponseInvestProposal responseInvestProposal)
        throws AuthException {
        Long psn = jwtService.getUserInfo().getSn();
        Account account = accountUtils.getAccountByUserSerialNumberAndStatus(csn, 2);
        InvestProposal result = investProposalServiceParent.setInvestProposalServiceParent(psn,
            proposalId,
            responseInvestProposal);
        try {
            if (responseInvestProposal.isAccept()) {
                investService.investStock(account.getAccountNum(),
                    InvestStockRequest.builder().quantity(
                            result.getQuantity()).ticker(result.getTicker())
                        .trading(result.getTradingCode())
                        .build());
            }
        } catch (CustomException e) {
            investProposalServiceParent.setInvestProposalImpossible(result);
        }
        return ApiUtils.success(true);
    }

    @GetMapping("/invest/no-approve")
    public ApiUtils.ApiResult getNoApprove(@RequestParam("csn") Long csn) throws AuthException {
        if (!jwtService.isMyFamily(csn)) {
            throw new CustomException(ErrorCode.FAILED_NO_CHILD);
        }
        Long parentSn = jwtService.getUserInfo().getSn();
        return ApiUtils.success(
            investProposalServiceParent.getInvestProposalNoApproved(parentSn, csn));
    }
}