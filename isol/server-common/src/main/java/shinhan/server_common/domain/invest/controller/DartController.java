package shinhan.server_common.domain.invest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_common.domain.invest.dto.StockFindCurrentResponse;
import shinhan.server_common.domain.invest.dto.StockFindDetailResponse;
import shinhan.server_common.domain.invest.service.StockService;
import shinhan.server_common.global.utils.ApiResult;
import shinhan.server_common.global.utils.ApiUtils;

import static shinhan.server_common.global.utils.ApiUtils.success;
@RestController
@RequestMapping("/stocks")
public class DartController {
    StockService stockService;
    @Autowired
    DartController(StockService stockService){
        this.stockService = stockService;
    }
    @GetMapping("")
    public ApiUtils.ApiResult getStocks(){
        return success("asd");
    }

    //개별 종목 조회하기
    @GetMapping("/{ticker}")
    public ApiUtils.ApiResult getStock(@PathVariable("ticker") String ticker){
        System.out.println(ticker);
        StockFindCurrentResponse result = stockService.getStockCurrent(ticker);
        return success(result);
    }
    @GetMapping("/{ticker}/{year}")
    public ApiUtils.ApiResult getStock(@PathVariable("ticker") String ticker,@PathVariable("year") String year){
        StockFindDetailResponse result = stockService.getStockDetail(ticker,year);
            return success(result);
        }


}
