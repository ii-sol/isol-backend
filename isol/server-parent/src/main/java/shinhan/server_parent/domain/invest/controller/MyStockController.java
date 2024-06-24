package shinhan.server_parent.domain.invest.controller;

import static shinhan.server_common.global.utils.ApiUtils.success;

import jakarta.security.auth.message.AuthException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_common.domain.invest.dto.MyStockListResponse;
import shinhan.server_common.domain.invest.service.MyStockService;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_parent.domain.invest.service.MyStockServiceParent;

@RestController
@RequestMapping("my-stocks")
public class MyStockController {

    MyStockService myStockService;
    JwtService jwtService;
    MyStockServiceParent stockServiceParent;

    MyStockController(MyStockService myStockService, JwtService jwtService,MyStockServiceParent myStockServiceParent) {
        this.myStockService = myStockService;
        this.jwtService = jwtService;
        this.stockServiceParent = myStockServiceParent;
    }

    //거래 가능 종목 리스트 조회(아이)
    @GetMapping("")
    public ApiUtils.ApiResult getMyStock(@RequestParam("csn") Long csn) throws AuthException {
        //자기 자식인지 확인
        Long loginSn = jwtService.getUserInfo().getSn();
        MyStockListResponse result = myStockService.findMyStocks(csn);
        return success(result);
    }

    //거래 가능 종목 리스트 추가(부모)
    @PostMapping("")
    public ApiUtils.ApiResult setMyStocks(@RequestParam("csn") Long csn,
        @RequestParam("ticker") String ticker) {
        //자기 자식인지 확인
        stockServiceParent.saveStockList(csn, ticker);
        return success("sad");
    }

    @DeleteMapping("")
    public ApiUtils.ApiResult deleteMyStock(@RequestParam("ticker") String ticker)
        throws AuthException {
        //자기 자식인지 확인
        long userSn = jwtService.getUserInfo().getSn();
        myStockService.delete(userSn, ticker);
        return success("삭제 성공");
    }
}
