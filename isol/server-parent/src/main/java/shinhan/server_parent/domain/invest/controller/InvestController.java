package shinhan.server_parent.domain.invest.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_common.domain.invest.service.InvestService;
import shinhan.server_common.global.utils.ApiUtils;

@RestController
@RequestMapping("/invest")
public class InvestController {


        InvestService investService;
        InvestController(InvestService investService){
                this.investService = investService;
        }
        @PostMapping("/{ticker}")
        public ApiUtils.ApiResult postMyChildStockList(@PathVariable("ticker") String ticker){
                return null;
        }
}
