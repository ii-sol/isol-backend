package shinhan.server_common.domain.invest.service;

import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shinhan.server_common.domain.invest.dto.CorpCodeResponse;
import shinhan.server_common.domain.invest.entity.CorpCode;
import shinhan.server_common.domain.invest.entity.StockNaverDuraion;
import shinhan.server_common.domain.invest.repository.CorpCodeRepository;
import shinhan.server_common.domain.invest.investRepository.StockListRepository;
import shinhan.server_common.domain.invest.repository.StockRepository;

@Service
public class CorpCodeService {
    CorpCodeRepository corpCodeRepository;
    StockListRepository stockListRepository;
    StockRepository stockRepository;
    CorpCodeService(CorpCodeRepository corpCodeRepository,StockListRepository stockListRepository,StockRepository stockRepository){
        this.corpCodeRepository = corpCodeRepository;
        this.stockListRepository = stockListRepository;
        this.stockRepository = stockRepository;
    }



    public List<CorpCodeResponse> getStockByName(Long userSn ,String corpName, Pageable pageable){
        List<CorpCode> corpCodeList = corpCodeRepository.findByCorpNameContaining(corpName,pageable);
        return corpCodeList.stream().map(x -> {
            String ticker = String.format("%06d",x.getStockCode());

            StockNaverDuraion[] apiCurrentDuraion = stockRepository.getApiCurrentDuraion(ticker);
            boolean canTranding = true;
            double currentPrice = 0;
            double changePrice = 0;
            double changeSign= 0;
            double changeRate= 0;
            if(Arrays.stream(apiCurrentDuraion).findAny().isEmpty()){
                canTranding =false;
            }else{
                currentPrice = Arrays.stream(apiCurrentDuraion).toList().get(apiCurrentDuraion.length-1).getClosePrice()*100;
                changePrice=currentPrice-Arrays.stream(apiCurrentDuraion).toList().get(apiCurrentDuraion.length-2).getClosePrice()*100;
                if(changePrice>0){
                    changeSign = 1;
                }
                else if(changePrice==0)
                    changeSign = 3;
                else changeSign =5;
                changeRate =  changePrice/Arrays.stream(apiCurrentDuraion).toList().get(apiCurrentDuraion.length-2).getClosePrice();
            }

            if (stockListRepository.findAllByUserSnAndTicker(userSn, ticker).isEmpty())
            {
                return CorpCodeResponse.builder().isMyStock(false).companyName(x.getCorpName()).canTrading(canTranding)
                    .ticker(ticker).currentPrice(
                        String.valueOf(currentPrice))
                    .changePrice(String.valueOf(changePrice))
                    .changeSign(String.valueOf(changeSign))
                    .changeRate(String.valueOf(changeRate))
                    .build();
            } else
                return CorpCodeResponse.builder().isMyStock(true).companyName(x.getCorpName()).canTrading(canTranding)
                    .ticker(ticker).currentPrice(
                        String.valueOf(currentPrice))
                    .changePrice(String.valueOf(changePrice))
                    .changeSign(String.valueOf(changeSign))
                    .changeRate(String.valueOf(changeRate))
                    .build();
        }).toList();
    }
    public List<CorpCodeResponse> getStock(Long userSn , Pageable pageable){
        Page<CorpCode> corpCodeList = corpCodeRepository.findAll(pageable);
        System.out.println("getStock");
        System.out.println(corpCodeList.get().toList().get(0).getCorpName());
        return corpCodeList.stream().map(x -> {
            String ticker = String.format("%06d",x.getStockCode());
            System.out.println(ticker);
            StockNaverDuraion[] apiCurrentDuraion = stockRepository.getApiCurrentDuraion(ticker);
            boolean canTranding = true;
            double currentPrice = 0;
            double changePrice = 0;
            double changeSign= 0;
            double changeRate= 0;
            if(Arrays.stream(apiCurrentDuraion).findAny().isEmpty()){
                canTranding =false;
            }else{
                currentPrice = Arrays.stream(apiCurrentDuraion).toList().get(apiCurrentDuraion.length-1).getClosePrice()*100;
                changePrice=currentPrice-Arrays.stream(apiCurrentDuraion).toList().get(apiCurrentDuraion.length-2).getClosePrice()*100;
                if(changePrice>0){
                    changeSign = 1;
                }
                else if(changePrice==0)
                    changeSign = 3;
                else changeSign =5;
                changeRate =  changePrice/Arrays.stream(apiCurrentDuraion).toList().get(apiCurrentDuraion.length-2).getClosePrice();
            }
            if (stockListRepository.findAllByUserSnAndTicker(userSn, String.format("%06d",x.getStockCode())).isEmpty())
            {
                return CorpCodeResponse.builder().isMyStock(false).companyName(x.getCorpName()).canTrading(canTranding)
                    .ticker(ticker).currentPrice(
                        String.valueOf(currentPrice))
                    .changePrice(String.valueOf(changePrice))
                    .changeSign(String.valueOf(changeSign))
                    .changeRate(String.valueOf(changeRate))
                    .build();
            } else
                return CorpCodeResponse.builder().isMyStock(true).companyName(x.getCorpName()).canTrading(canTranding)
                    .ticker(ticker).currentPrice(
                        String.valueOf(currentPrice))
                    .changePrice(String.valueOf(changePrice))
                    .changeSign(String.valueOf(changeSign))
                    .changeRate(String.valueOf(changeRate))
                    .build();
        }).toList();
    }
}
