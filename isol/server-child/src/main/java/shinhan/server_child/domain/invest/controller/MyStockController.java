package shinhan.server_child.domain.invest.controller;

import static shinhan.server_common.global.utils.ApiUtils.success;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shinhan.server_child.domain.invest.dto.MyStockListResponse;
import shinhan.server_child.domain.invest.service.MyStockService;
import shinhan.server_common.global.utils.ApiResult;
import shinhan.server_common.global.utils.ApiUtils;
@RestController
@RequestMapping("/my-stocks")
public class MyStockController {
    MyStockService myStockService;
    MyStockController(MyStockService myStockService){
        this.myStockService = myStockService;
    }
    //전체 종목 조회하기

    //거래 가능 종목 리스트 조회(부모)
    @GetMapping("/{co}")
    public ApiResult getMyChildStock(){
        return ApiResult.responseSuccess("asdf");
    }
    //거래 가능 종목 리스트 조회(아이)
    @GetMapping("")
    public ApiUtils.ApiResult getMyStock(){
        MyStockListResponse result = myStockService.findMyStocks(123123L);
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

    //거래 가능 종목 리스트 삭제(부모)
    @DeleteMapping("")
    public ApiUtils.ApiResult deleteMyStock(@RequestParam("ticker") String ticker){
        myStockService.delete(123123L,ticker);
        return success("삭제 성공");
    }
}
