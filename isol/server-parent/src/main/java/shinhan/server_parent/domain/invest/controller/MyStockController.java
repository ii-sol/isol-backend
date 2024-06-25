package shinhan.server_parent.domain.invest.controller;

import org.springframework.web.bind.annotation.*;
import shinhan.server_common.domain.invest.dto.MyStockListResponse;
import shinhan.server_common.domain.invest.service.MyStockService;
import shinhan.server_common.global.exception.AuthException;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.exception.ErrorCode;
import shinhan.server_common.global.security.JwtService;
import shinhan.server_common.global.utils.ApiUtils;
import shinhan.server_parent.domain.invest.service.MyStockServiceParent;

import static shinhan.server_common.global.utils.ApiUtils.success;

@RestController
@RequestMapping("/api/my-stocks")
public class MyStockController {

    MyStockService myStockService;
    JwtService jwtService;
    MyStockServiceParent stockServiceParent;

    MyStockController(MyStockService myStockService, JwtService jwtService,
                      MyStockServiceParent myStockServiceParent) {
        this.myStockService = myStockService;
        this.jwtService = jwtService;
        this.stockServiceParent = myStockServiceParent;
    }

    //거래 가능 종목 리스트 조회(아이)
    @GetMapping("")
    public ApiUtils.ApiResult getMyStock(@RequestParam("csn") Long csn) throws AuthException {
        if (!jwtService.isMyFamily(csn)) {
            throw new CustomException(ErrorCode.FAILED_NO_CHILD);
        }
        MyStockListResponse result = myStockService.findMyStocks(csn);
        return success(result);
    }

    //거래 가능 종목 리스트 추가(부모)
    @PostMapping("")
    public ApiUtils.ApiResult setMyStocks(@RequestParam("csn") Long csn,
                                          @RequestParam("ticker") String ticker) throws AuthException {
        if (!jwtService.isMyFamily(csn)) {
            throw new CustomException(ErrorCode.FAILED_NO_CHILD);
        }
        stockServiceParent.saveStockList(csn, ticker);
        return success("sad");
    }

    @DeleteMapping("")
    public ApiUtils.ApiResult deleteMyStock(@RequestParam("ticker") String ticker,
                                            @RequestParam("csn") Long csn)
            throws AuthException {
        //자기 자식인지 확인
        if (!jwtService.isMyFamily(csn)) {
            throw new CustomException(ErrorCode.FAILED_NO_CHILD);
        }
        myStockService.delete(csn, ticker);
        return success("삭제 성공");    
    }
}
