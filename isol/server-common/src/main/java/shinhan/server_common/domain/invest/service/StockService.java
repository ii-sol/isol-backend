package shinhan.server_common.domain.invest.service;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import shinhan.server_common.domain.invest.dto.StockFindCurrentResponse;
import shinhan.server_common.domain.invest.dto.StockFindDetailResponse;
import shinhan.server_common.domain.invest.entity.StockDartPoten;
import shinhan.server_common.domain.invest.entity.StockDartProfit;
import shinhan.server_common.domain.invest.entity.StockDivideOutput;
import shinhan.server_common.domain.invest.entity.StockDuraionPriceOutput;
import shinhan.server_common.domain.invest.entity.StockFianceResponseOutput;
import shinhan.server_common.domain.invest.entity.StockNaverDuraion;
import shinhan.server_common.domain.invest.entity.StockNaverIntegration;
import shinhan.server_common.domain.invest.repository.StockRepository;

@Service
public class StockService {
    StockRepository stockRepository;

    @Autowired
        WebClient webClientP;
    @Autowired
        WebClient webClientF;
    @Autowired
        WebClient webDartClient;


    StockService(StockRepository stockRepository){
        this.stockRepository = stockRepository;
    }

    public StockFindDetailResponse getStockDuration(String ticker, String year){
        StockNaverDuraion[] stockNaverDuraions = stockRepository.getApiDuraion(ticker,year);
        return StockFindDetailResponse.builder()
            .charts(List.of(stockNaverDuraions)).build();

    }

    public StockFindDetailResponse getStockDetail2(String ticker,String year){
        StockNaverDuraion[] stockNaverDuraions = stockRepository.getApiDuraion(ticker,year);
        StockNaverIntegration stockNaverIntegration = stockRepository.getApiIntegration(ticker);
        StockDartPoten stockDartPoten = stockRepository.getApiDartPoten("00126380");
        StockDartProfit stockDartProfit = stockRepository.getApiDartProfit("00126380");
        double currentPrice = Arrays.stream(stockNaverDuraions).toList().get(stockNaverDuraions.length-1).getClosePrice();
        double prePrice = Arrays.stream(stockNaverDuraions).toList().get(stockNaverDuraions.length-2).getClosePrice();
        double changePrice =
            (currentPrice
            - prePrice) *
            100;
        short changeSign;
        double changeRate = (double) changePrice /prePrice;
        if(changePrice<0){
            changeSign=5;
        }else if(changePrice==0){
            changeSign=3;
        }else{
            changeSign=1;
        }
        return StockFindDetailResponse.builder()
            .charts(List.of(stockNaverDuraions))
            .changePrice(changePrice+"")
            .changeRate(changeRate+"")
            .changeSign(changeSign+"")
            .companyName(stockNaverIntegration.getStockName())
            .currentPrice(
                (int) Arrays.stream(stockNaverDuraions).toList().get(stockNaverDuraions.length-1).getClosePrice())
            .dividendYield(stockNaverIntegration.getTotalInfos().get(16).getValue())
            .marketCapitalization(stockNaverIntegration.getTotalInfos().get(6).getValue())
            .PER(stockNaverIntegration.getTotalInfos().get(10).getValue())
            .PBR(stockNaverIntegration.getTotalInfos().get(14).getValue())
            .ROE(stockDartProfit.getList().get(5).getIdx_val())
            .profitGrowth(stockDartPoten.getList().get(2).getIdx_val())
            .ticker(ticker)
            .build();
    }

    public StockFindCurrentResponse getStockCurrent2(String ticker) {
        StockNaverDuraion[] stockDuraionPriceOutput = stockRepository.getApiCurrentDuraion(ticker);
        List<StockNaverDuraion> stockNaverDuraionList = Arrays.stream(stockDuraionPriceOutput).toList();
        int size = stockDuraionPriceOutput.length;
        System.out.println(size);
        double changePrice = stockNaverDuraionList.get(size-1).getClosePrice()-stockNaverDuraionList.get(size-2).getClosePrice();
        String changeSign;
        if(changePrice<0){
            changeSign = "4";
        }else if(changePrice==0){
            changeSign = "3";
        }else{
            changeSign = "2";
        }
        return StockFindCurrentResponse.builder()
            .currentPrice(String.valueOf(stockNaverDuraionList.get(1).getClosePrice()*100))
            .changePrice(String.valueOf(changePrice))
            .changeRate(String.valueOf(changePrice/stockNaverDuraionList.get(0).getClosePrice()))
            .changeSign(changeSign)
            //companyName 나중에 db 만들고 바꾸기~!!!!
            .companyName(ticker)
            .ticker(ticker)
            .build();
    }
        public StockFindDetailResponse getStockDetail(String ticker, String year) {
            StockDuraionPriceOutput stockDuraionPriceOutput = stockRepository.getApiCurrentPrice(ticker, year);
            StockDivideOutput stockDivideOutputMono = stockRepository.getApiDivide(ticker);
            StockFianceResponseOutput stockFianceResponseOutput = stockRepository.getApiFiance(ticker);
            return StockFindDetailResponse.builder()
//                .charts(stockDuraionPriceOutput.getOutput2())
                .ROE(stockFianceResponseOutput.getOutput().get(0).getRoe())
                .PBR(stockDuraionPriceOutput.getOutput1().getPbr())
//                .PSR(getPSR(stockDuraionPriceOutput,stockFianceResponseOutput))
                .changeSign(stockDuraionPriceOutput.getOutput1().getChangeSign())
                .changePrice(stockDuraionPriceOutput.getOutput1().getChangePrice())
                .changeRate(stockDuraionPriceOutput.getOutput1().getChangeRate())
                .marketCapitalization(stockDuraionPriceOutput.getOutput1().getMarketCapitalization())
                .dividendYield(stockDivideOutputMono.getList().get(8).getDividendYield())
                .companyName(stockDuraionPriceOutput.getOutput1().getCompanyName())
                .currentPrice(stockDuraionPriceOutput.getOutput1().getCurrentPrice())
                .PER(stockDuraionPriceOutput.getOutput1().getPer())
                .ticker(ticker)
                .build();
        }
        public String getPSR(StockDuraionPriceOutput stockDuraionPriceOutput, StockFianceResponseOutput stockFianceResponseOutput){
            return stockDuraionPriceOutput.getOutput1().getCurrentPrice()*stockFianceResponseOutput.getOutput().get(0).getSps() + "";
        }

        public StockFindCurrentResponse getStockCurrent(String ticker) {
            StockDuraionPriceOutput stockDuraionPriceOutput = stockRepository.getApiCurrentPrice(ticker, "0");
            return StockFindCurrentResponse.builder()
                .currentPrice(String.valueOf(stockDuraionPriceOutput.getOutput1().getCurrentPrice()))
                .changePrice(stockDuraionPriceOutput.getOutput1().getChangePrice())
                .changeRate(stockDuraionPriceOutput.getOutput1().getChangeRate())
                .changeSign(stockDuraionPriceOutput.getOutput1().getChangeSign())
                .companyName(stockDuraionPriceOutput.getOutput1().getCompanyName())
                .ticker(ticker)
                .build();
        }
}
