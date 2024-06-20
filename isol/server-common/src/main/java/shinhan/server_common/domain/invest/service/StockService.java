package shinhan.server_common.domain.invest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import shinhan.server_common.domain.invest.dto.StockFindCurrentResponseDTO;
import shinhan.server_common.domain.invest.dto.StockFindDetailResponseDTO;
import shinhan.server_common.domain.invest.entity.StockDivideOutput;
import shinhan.server_common.domain.invest.entity.StockDuraionPriceOutput;
import shinhan.server_common.domain.invest.entity.StockFianceResponseOutput;
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

        public StockFindDetailResponseDTO getStockDetail(String ticker, String year) {
            StockDuraionPriceOutput stockDuraionPriceOutput = stockRepository.getApiCurrentPrice(ticker, year);
            StockDivideOutput stockDivideOutputMono = stockRepository.getApiDivide(ticker);
            StockFianceResponseOutput stockFianceResponseOutput = stockRepository.getApiFiance(ticker);
            System.out.println(stockFianceResponseOutput);
            System.out.println(stockDuraionPriceOutput);
            System.out.println(stockDivideOutputMono);
            return StockFindDetailResponseDTO.builder()
                .charts(stockDuraionPriceOutput.getOutput2())
                .ROE(stockFianceResponseOutput.getOutput().get(0).getRoe())
                .PBR(stockDuraionPriceOutput.getOutput1().getPbr())
                .PSR(getPSR(stockDuraionPriceOutput,stockFianceResponseOutput))
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

        public StockFindCurrentResponseDTO getStockCurrent(String ticker) {
            StockDuraionPriceOutput stockDuraionPriceOutput = stockRepository.getApiCurrentPrice(ticker, "0");
            return StockFindCurrentResponseDTO.builder()
                .currentPrice(String.valueOf(stockDuraionPriceOutput.getOutput1().getCurrentPrice()))
                .changePrice(stockDuraionPriceOutput.getOutput1().getChangePrice())
                .changeRate(stockDuraionPriceOutput.getOutput1().getChangeRate())
                .changeSign(stockDuraionPriceOutput.getOutput1().getChangeSign())
                .companyName(stockDuraionPriceOutput.getOutput1().getCompanyName())
                .ticker(ticker)
                .build();
        }
}
