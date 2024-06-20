package shinhan.server_child.domain.invest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_child.domain.invest.dto.InvestStockRequest;
import shinhan.server_child.domain.invest.dto.PortfolioResponse;
import shinhan.server_child.domain.invest.service.InvestProposalService;
import shinhan.server_child.domain.invest.service.InvestService;
import shinhan.server_common.global.utils.ApiResult;
import shinhan.server_common.global.utils.ApiUtils;
import static shinhan.server_common.global.utils.ApiUtils.success;
@RestController
@RequestMapping("/invest")
public class InvestController {

    InvestService investService;
    InvestProposalService investProposalService;
    @Autowired
    InvestController(InvestService investService,InvestProposalService investProposalService) {
        this.investService = investService;
        this.investProposalService = investProposalService;
    }

    //투자 거래 내역 조회하기(아이)
    @GetMapping("/history")
    public ApiUtils.ApiResult getInvestHistory() {
        String getUserAccount = "01012345678";
        return success(investService.getStockHisttory(getUserAccount));
    }

    //포트폴리오 조회하기(아이)
    @GetMapping("/portfolio")
    public ApiUtils.ApiResult getInvestPortfolio() {
        String getUserAccountNum = "01012345678";
        PortfolioResponse result = investService.getPortfolio(getUserAccountNum);
        return success(result);
    }

    //종목 판매/구매하기
    @PostMapping("")
    public ApiUtils.ApiResult invest(@RequestBody InvestStockRequest investStockRequest) {
        String getUserAccountNum = "01012345678";
        boolean result = investService.investStock(getUserAccountNum, investStockRequest);
        if (result) {
            return success("거래 성공하였습니다.");
        } else {
            return success("확인 필요");
        }
    }

}
