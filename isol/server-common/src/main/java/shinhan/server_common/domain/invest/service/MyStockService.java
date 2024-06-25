package shinhan.server_common.domain.invest.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_common.domain.invest.dto.MyStockListResponse;
import shinhan.server_common.domain.invest.investEntity.MyStockList;
import shinhan.server_common.domain.invest.repository.CorpCodeRepository;
import shinhan.server_common.domain.invest.investRepository.StockListRepository;
import shinhan.server_common.domain.invest.dto.StockFindCurrentResponse;

@Service
@Transactional
public class MyStockService {

    StockListRepository stockListRepository;
    StockService stockService;
    CorpCodeRepository corpCodeRepository;

    @Autowired
    MyStockService(StockListRepository stockListRepository, StockService stockService,
        CorpCodeRepository corpCodeRepository) {

        this.stockListRepository = stockListRepository;
        this.stockService = stockService;
        this.corpCodeRepository = corpCodeRepository;
    }

    //공통
    public MyStockListResponse findMyStocks(Long userSn) {
        List<MyStockList> result = stockListRepository.findAllByUserSn(userSn);
        List<StockFindCurrentResponse> stockFindCurrentResponseList = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            StockFindCurrentResponse stockFindCurrentResponse = stockService.getStockCurrent2(
                result.get(i).getTicker());
            stockFindCurrentResponse.setCompanyName(
                corpCodeRepository.findByStockCode(
                    Integer.parseInt(result.get(i).getTicker())).get().getCorpName()
            );
                    stockFindCurrentResponseList.add(stockFindCurrentResponse);
                }
                MyStockListResponse myStockListResponse = new MyStockListResponse(
                    stockFindCurrentResponseList);
                return myStockListResponse;
            }

            //공통
            public void delete (Long userSn, String ticker){
                stockListRepository.deleteByUserSnAndTicker(userSn, ticker);
                }
            }
