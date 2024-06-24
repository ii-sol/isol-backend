package shinhan.server_child.domain.invest.controller;

import static shinhan.server_common.global.utils.ApiUtils.success;

import jakarta.security.auth.message.AuthException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_child.domain.invest.dto.MyStockListResponse;
import shinhan.server_child.domain.invest.service.MyStockService;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiResult;
import shinhan.server_common.global.utils.ApiUtils;
@RestController
@RequestMapping("/my-stocks")
public class MyStockController {
    MyStockService myStockService;
    JwtService jwtService;
    MyStockController(MyStockService myStockService,JwtService jwtService){
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
    //거래 가능 종목 리스트 추가(부모)
    @PostMapping("/{csn}")
    public ApiUtils.ApiResult setMyStocks(@PathVariable("csn") Long csn, @RequestParam("ticker") String ticker){
        myStockService.saveStockList(csn,ticker);
        return success("sad");
    }
    //거래 가능 종목 리스트 삭제
    @DeleteMapping("/{co}")
    public ApiResult deleteMyChildStock(@PathVariable("co") int childOrder, @RequestParam("ticker") String ticker) {
        return ApiResult.responseSuccess(childOrder);
    }

    @DeleteMapping("")
    public ApiUtils.ApiResult deleteMyStock(@RequestParam("ticker") String ticker)
        throws AuthException {
        long userSn = jwtService.getUserInfo().getSn();
        myStockService.delete(userSn,ticker);
        return success("삭제 성공");
    }
}
