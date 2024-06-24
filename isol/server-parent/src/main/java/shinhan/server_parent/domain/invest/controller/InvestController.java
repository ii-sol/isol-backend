package shinhan.server_parent.domain.invest.controller;

import static shinhan.server_common.global.utils.ApiUtils.success;

import jakarta.security.auth.message.AuthException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_common.domain.account.entity.Account;
import shinhan.server_common.domain.invest.dto.PortfolioResponse;
import shinhan.server_common.domain.invest.service.InvestService;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_common.global.utils.account.AccountUtils;

@RestController
@RequestMapping("/invest")
public class InvestController {


        InvestService investService;
        JwtService jwtService;
        AccountUtils accountUtils;
        InvestController(InvestService investService,JwtService jwtService,AccountUtils accountUtils){
                this.accountUtils = accountUtils;
                this.investService = investService;
                this.jwtService = jwtService;
        }
        @GetMapping("/history/{status}")
        public ApiUtils.ApiResult getInvestHistory(@PathVariable("status") short status, @RequestParam("year") int year,
            @RequestParam("month") int month,@RequestParam("csn")Long csn)
            throws AuthException {
                if(!jwtService.isMyFamily(csn)){
                        throw new CustomException(ErrorCode.FAILED_NO_CHILD);
                }
                Account accountByUserSerialNumberAndStatus = accountUtils.getAccountByUserSerialNumberAndStatus(
                    csn, 2);
                return success(investService.getStockHistory(accountByUserSerialNumberAndStatus.getAccountNum(),status,year,month));
        }

        //포트폴리오 조회하기(부모)
        @GetMapping("/portfolio")
        public ApiUtils.ApiResult getInvestPortfolio(@RequestParam("csn")Long csn) throws AuthException {
                if(!jwtService.isMyFamily(csn)){
                        throw new CustomException(ErrorCode.FAILED_NO_CHILD);
                }
                Long loginUserSerialNumber = jwtService.getUserInfo().getSn();
                Account accountByUserSerialNumberAndStatus = accountUtils.getAccountByUserSerialNumberAndStatus(
                    csn, 2);
                PortfolioResponse result = investService.getPortfolio(accountByUserSerialNumberAndStatus.getAccountNum());
                return success(result);
        }
}
