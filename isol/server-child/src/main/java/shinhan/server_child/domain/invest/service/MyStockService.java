package shinhan.server_child.domain.invest.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_common.domain.invest.dto.MyStockListResponse;
import shinhan.server_common.domain.invest.entity.MyStockList;
import shinhan.server_common.domain.invest.repository.StockListRepository;
import shinhan.server_common.domain.invest.dto.StockFindCurrentResponse;
import shinhan.server_common.domain.invest.service.StockService;

@Service
public class MyStockService {
    StockListRepository stockListRepository;
    StockService stockService;
    @Autowired
    MyStockService(StockListRepository stockListRepository,StockService stockService){
        this.stockListRepository = stockListRepository;
        this.stockService = stockService;
    }

    //공통
    public MyStockListResponse findMyStocks(Long userSn){
        List<MyStockList> result = stockListRepository.findAllByUserSn(userSn);
        System.out.println(result.size());
        List<StockFindCurrentResponse> stockFindCurrentResponseList = new ArrayList<>();
        for(int i=0;i<result.size();i++){
            StockFindCurrentResponse stockFindCurrentResponse = stockService.getStockCurrent2(result.get(i).getTicker());
            stockFindCurrentResponseList.add(stockFindCurrentResponse);
        }
        MyStockListResponse myStockListResponse = new MyStockListResponse(
            stockFindCurrentResponseList);
        return myStockListResponse;
    }
    //공통
    @Transactional
    public boolean delete(Long userSn,String ticker){
        stockListRepository.deleteByUserSnAndTicker(userSn,ticker);
        return true;
    }

    //부모
    public void saveStockList(Long child_sn,String ticker){
        MyStockList stockList = new MyStockList(child_sn,ticker);
        stockListRepository.save(stockList);
    }
}
