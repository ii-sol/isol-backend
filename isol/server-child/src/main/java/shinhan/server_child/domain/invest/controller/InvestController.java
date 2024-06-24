package shinhan.server_child.domain.invest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shinhan.server_child.domain.invest.service.InvestProposalServiceChild;
import shinhan.server_common.domain.account.entity.Account;
import shinhan.server_common.domain.invest.dto.InvestStockRequest;
import shinhan.server_common.domain.invest.dto.PortfolioResponse;
import shinhan.server_common.domain.invest.service.InvestProposalService;
import shinhan.server_common.domain.invest.service.InvestService;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_common.global.utils.account.AccountUtils;

import static shinhan.server_common.global.utils.ApiUtils.success;

@RestController
@RequestMapping("/invest")
public class InvestController {

    InvestService investService;
    InvestProposalService investProposalService;
    JwtService jwtService;
    AccountUtils accountUtils;
    InvestProposalServiceChild investProposalServiceChild;

    @Autowired
    InvestController(InvestService investService, InvestProposalService investProposalService, JwtService jwtService, AccountUtils accountUtils,
                     InvestProposalServiceChild investProposalServiceChild) {
        this.accountUtils = accountUtils;
        this.investService = investService;
        this.investProposalService = investProposalService;
        this.jwtService = jwtService;
        this.investProposalServiceChild = investProposalServiceChild;
    }

    //투자 거래 내역 조회하기(아이)
    @GetMapping("/history/{status}")
    public ApiUtils.ApiResult getInvestHistory(@PathVariable("status") short status, @RequestParam("year") int year,
                                               @RequestParam("month") int month)
            throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        Account accountByUserSerialNumberAndStatus = accountUtils.getAccountByUserSerialNumberAndStatus(
                loginUserSerialNumber, 2);
        return success(investService.getStockHistory(accountByUserSerialNumberAndStatus.getAccountNum(), status, year, month));
    }

    //포트폴리오 조회하기(아이)
    @GetMapping("/portfolio")
    public ApiUtils.ApiResult getInvestPortfolio() throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        Account accountByUserSerialNumberAndStatus = accountUtils.getAccountByUserSerialNumberAndStatus(
                loginUserSerialNumber, 2);
        PortfolioResponse result = investService.getPortfolio(accountByUserSerialNumberAndStatus.getAccountNum());
        return success(result);
    }

    //종목 판매/구매하기
    @PostMapping("")
    public ApiUtils.ApiResult invest(@RequestBody InvestStockRequest investStockRequest)
            throws AuthException {
        Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
        Account accountByUserSerialNumberAndStatus = accountUtils.getAccountByUserSerialNumberAndStatus(
                loginUserSerialNumber, 2);
        boolean result = investService.investStock(
                accountByUserSerialNumberAndStatus.getAccountNum(), investStockRequest);
        if (result) {
            return success("거래 성공하였습니다.");
        } else {
            return success("확인 필요");
        }
    }
}
