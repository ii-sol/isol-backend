package shinhan.server_child.domain.invest.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_child.domain.invest.dto.MyStockListResDTO;
import shinhan.server_child.domain.invest.entity.MyStockList;
import shinhan.server_child.domain.invest.repository.StockListRepository;
import shinhan.server_common.domain.invest.dto.StockFindCurrentResponseDTO;
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

    public MyStockListResDTO findMyStocks(Long userSn){
        List<MyStockList> result = stockListRepository.findAllByUserSn(userSn);
        List<StockFindCurrentResponseDTO> stockFindCurrentResponseDTOList = new ArrayList<>();
        for(int i=0;i<result.size();i++){
            StockFindCurrentResponseDTO stockFindCurrentResponseDTO = stockService.getStockCurrent(result.get(i).getTicker());
            System.out.println("여기요여기");
            System.out.println(stockFindCurrentResponseDTO);
            stockFindCurrentResponseDTOList.add(stockFindCurrentResponseDTO);
        }
        MyStockListResDTO myStockListResDTO = new MyStockListResDTO(stockFindCurrentResponseDTOList);
        return myStockListResDTO;
    }

    @Transactional
    public boolean delete(Long userSn,String ticker){
        stockListRepository.deleteByUserSnAndTicker(userSn,ticker);
        return true;
    }


    public void saveStockList(Long child_sn,String ticker){
        MyStockList stockList = new MyStockList(child_sn,ticker);
        stockListRepository.save(stockList);
    }
}
