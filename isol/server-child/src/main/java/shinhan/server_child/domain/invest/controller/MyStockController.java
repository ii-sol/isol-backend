package shinhan.server_child.domain.invest.controller;

import org.springframework.web.bind.annotation.*;
import shinhan.server_common.domain.invest.dto.MyStockListResponse;
import shinhan.server_common.domain.invest.service.MyStockService;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiUtils;

import static shinhan.server_common.global.utils.ApiUtils.success;

@RestController
@RequestMapping("/api/my-stocks")
public class MyStockController {
    MyStockService myStockService;
    JwtService jwtService;

    MyStockController(MyStockService myStockService, JwtService jwtService) {
        this.myStockService = myStockService;
        this.jwtService = jwtService;
    }

    //거래 가능 종목 리스트 조회(아이)
    @GetMapping("")
    public ApiUtils.ApiResult getMyStock() throws AuthException {
        Long loginSn = jwtService.getUserInfo().getSn();
        MyStockListResponse result = myStockService.findMyStocks(loginSn);
        return success(result);
    }

    @DeleteMapping("")
    public ApiUtils.ApiResult deleteMyStock(@RequestParam("ticker") String ticker)
            throws AuthException {
        long userSn = jwtService.getUserInfo().getSn();
        myStockService.delete(userSn, ticker);
        return success("삭제 성공");
    }
}
