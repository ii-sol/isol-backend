package shinhan.server_parent.domain.invest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_common.global.utils.ApiResult;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_parent.domain.invest.service.InvestService;

@RestController
@RequestMapping("/invest")
public class InvestController {

    InvestService investService;

    @Autowired
    InvestController(InvestService investService) {
        this.investService = investService;
    }

    //투자 거래 내역 조회하기(부모)
    @GetMapping("history/{csn}")
    public ApiUtils.ApiResult getInvestHistory(@PathVariable("csn") Long childSn) {
        String getAccountNum = "01012345678";
        return ApiUtils.success(investService.getPortfolio(getAccountNum));
    }


    //포트폴리오 조회하기(부모)
    @GetMapping("/portfolio/{csn}")
    public ApiResult getInvestPortfolio(@PathVariable("csn") Long childSn) {
        String getAccountNum = "01012345678";
        return null;
    }

}
