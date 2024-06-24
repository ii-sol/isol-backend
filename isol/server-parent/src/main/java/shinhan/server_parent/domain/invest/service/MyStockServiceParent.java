package shinhan.server_parent.domain.invest.service;

import org.springframework.stereotype.Service;
import shinhan.server_common.domain.invest.investEntity.MyStockList;
import shinhan.server_common.domain.invest.repository.StockListRepository;

@Service
public class MyStockServiceParent {
    StockListRepository stockListRepository;
    MyStockServiceParent(StockListRepository stockListRepository){
        this.stockListRepository = stockListRepository;
    }
    //부모
    public void saveStockList(Long child_sn,String ticker){
        MyStockList stockList = new MyStockList(child_sn,ticker);
        stockListRepository.save(stockList);
    }
}
