package shinhan.server_child.domain.invest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_child.domain.invest.dto.InvestStockReqDTO;
import shinhan.server_child.domain.invest.dto.PortfolioResDTO;
import shinhan.server_child.domain.invest.service.InvestService;
import shinhan.server_common.global.utils.ApiResult;

@RestController
public class InvestController {

    InvestService investService;

    @Autowired
    InvestController(InvestService investService) {
        this.investService = investService;
    }

    //투자 거래 내역 조회하기(부모)
    @GetMapping("/invest/history/{co}")
    public ApiResult getInvestHistory(@PathVariable("co") int childOrder) {
        return null;
    }

    //투자 거래 내역 조회하기(아이)
    @GetMapping("/invest/history")
    public ApiResult getInvestHistory() {
        String getUserAccount = "01012345678";
        return ApiResult.responseSuccess(investService.getStockHisttory(getUserAccount));
    }

    //포트폴리오 조회하기(부모)
    @GetMapping("/invest/portfolio/{co}")
    public ApiResult getInvestPortfolio(@PathVariable("co") int childOrder) {
        return null;
    }

    //포트폴리오 조회하기(아이)
    @GetMapping("/invest/portfolio")
    public ApiResult getInvestPortfolio() {
        String getUserAccountNum = "01012345678";
        PortfolioResDTO result = investService.getPortfolio(getUserAccountNum);
        return ApiResult.responseSuccess(result);
    }

    //종목 판매/구매하기
    @PostMapping("/invest")
    public ApiResult invest(@RequestBody InvestStockReqDTO investStockReqDTO) {
        String getUserAccountNum = "01012345678";
        boolean result = investService.investStock(getUserAccountNum, investStockReqDTO);
        if (result) {
            return ApiResult.responseSuccess("거래 성공하였습니다.");
        } else {
            return ApiResult.responseError("확인 필요");
        }
    }
//    @GetMapping("/invest/temp")
//    public ApiResult temp(){
//        String result = userService.getTemp(1);
//        return ApiResult.responseSuccess(result);
//    }

//    @GetMapping("/invest/insert")
//    public ApiResult tempInsert(){
//        userService.save(112312L);
//        return ApiResult.responseError("asd");
//    }
}
