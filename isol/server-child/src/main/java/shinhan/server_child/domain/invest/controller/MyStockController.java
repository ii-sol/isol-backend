package shinhan.server_child.domain.invest.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_child.domain.invest.dto.MyStockListResDTO;
import shinhan.server_child.domain.invest.service.MyStockService;
import shinhan.server_common.global.utils.ApiResult;

@RestController
public class MyStockController {
    MyStockService myStockService;
    MyStockController(MyStockService myStockService){
        this.myStockService = myStockService;
    }
    //전체 종목 조회하기

    //거래 가능 종목 리스트 조회(부모)
    @GetMapping("/my-stock/{co}")
    public ApiResult getMyChildStock(){
        return ApiResult.responseSuccess("asdf");
    }
    //거래 가능 종목 리스트 조회(아이)
    @GetMapping("/my-stocks")
    public ApiResult getMyStock(){
        MyStockListResDTO result = myStockService.findMyStocks(123123L);
        return ApiResult.responseSuccess(result);
    }
    //거래 가능 종목 리스트 추가(부모)
    @PostMapping("/my-stocks/{csn}")
    public ApiResult setMyStocks(@PathVariable("csn") Long csn, @RequestParam("ticker") String ticker){
        myStockService.saveStockList(csn,ticker);
        return ApiResult.responseSuccess("sad");
    }
    //거래 가능 종목 리스트 삭제
    @DeleteMapping("/my-stocks/{co}")
    public ApiResult deleteMyChildStock(@PathVariable("co") int childOrder, @RequestParam("ticker") String ticker) {
        return ApiResult.responseSuccess(childOrder);
    }

    //거래 가능 종목 리스트 삭제(부모)
    @DeleteMapping("/my-stocks")
    public ApiResult deleteMyStock(@RequestParam("ticker") String ticker){
        myStockService.delete(123123L,ticker);
        return ApiResult.responseSuccess("삭제 성공");
    }
}
