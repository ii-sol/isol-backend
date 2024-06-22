package shinhan.server_child.domain.invest.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shinhan.server_child.domain.invest.dto.CorpCodeResponse;
import shinhan.server_child.domain.invest.entity.CorpCode;
import shinhan.server_child.domain.invest.entity.MyStockList;
import shinhan.server_child.domain.invest.repository.CorpCodeRepository;
import shinhan.server_child.domain.invest.repository.StockListRepository;

@Service
public class CorpCodeService {
    CorpCodeRepository corpCodeRepository;
    StockListRepository stockListRepository;
    CorpCodeService(CorpCodeRepository corpCodeRepository,StockListRepository stockListRepository){
        this.corpCodeRepository = corpCodeRepository;
        this.stockListRepository = stockListRepository;
    }

    public List<CorpCodeResponse> getStockByName(Long userSn ,String corpName, Pageable pageable){
        List<CorpCode> corpCodeList = corpCodeRepository.findByCorpNameContaining(corpName,pageable);
        return corpCodeList.stream().map(x -> {
            if (stockListRepository.findAllByUserSnAndTicker(userSn, String.format("%06d",x.getStockCode())).isEmpty())
            {
                return CorpCodeResponse.builder().isMyStock(false).companyName(x.getCorpName())
                    .build();
            } else
                return CorpCodeResponse.builder().isMyStock(true).companyName(x.getCorpName())
                    .build();
        }).toList();
    }
    public List<CorpCodeResponse> getStock(Long userSn , Pageable pageable){
        Page<CorpCode> corpCodeList = corpCodeRepository.findAll(pageable);
        return corpCodeList.stream().map(x -> {
            if (stockListRepository.findAllByUserSnAndTicker(userSn, String.format("%06d",x.getStockCode())).isEmpty())
            {
                return CorpCodeResponse.builder().isMyStock(false).companyName(x.getCorpName())
                    .build();
            } else
                return CorpCodeResponse.builder().isMyStock(true).companyName(x.getCorpName())
                    .build();
        }).toList();
    }
}
