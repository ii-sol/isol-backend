package shinhan.server_child.domain.invest.service;

import static shinhan.server_common.global.exception.ErrorCode.FAILED_SHORTAGE_MONEY;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import shinhan.server_child.domain.invest.dto.InvestStockRequest;
import shinhan.server_child.domain.invest.dto.InvestTradeDetailResponse;
import shinhan.server_child.domain.invest.dto.PortfolioResponse;
import shinhan.server_child.domain.invest.dto.StockHistoryResponse;
import shinhan.server_child.domain.invest.entity.Portfolio;
import shinhan.server_child.domain.invest.entity.StockHistory;
import shinhan.server_child.domain.invest.repository.PortfolioRepository;
import shinhan.server_child.domain.invest.repository.StockHistoryRepository;
import shinhan.server_common.domain.account.entity.Account;
import shinhan.server_common.domain.invest.entity.StockDuraionPriceOutput;
import shinhan.server_common.domain.invest.repository.StockRepository;
import shinhan.server_common.domain.invest.service.StockService;
import shinhan.server_common.global.exception.CustomException;

@Service
public class InvestService {
    StockRepository stockRepository;
    StockService stockService;
    PortfolioRepository portfolioRepository;
    StockHistoryRepository stockHistoryRepository;
    InvestService(StockRepository stockRepository,PortfolioRepository portfolioRepository, StockHistoryRepository stockHistoryRepository
    ,StockService stockService
    ){
        this.portfolioRepository = portfolioRepository;
        this.stockRepository = stockRepository;
        this.stockHistoryRepository = stockHistoryRepository;
        this.stockService = stockService;
    }

    public List<StockHistoryResponse> getStockHisttory(String account){
        List<StockHistory> result = stockHistoryRepository.findByAccountNum(account);
        List<StockHistoryResponse> stockHistoryResponseList = new ArrayList<>();
        for(StockHistory data : result)
        {
            StockHistoryResponse stockHistoryResponse = StockHistoryResponse.builder()
                .stockPrice(data.getStockPrice())
                .tradingCode(data.getTradingCode())
                .companyName(data.getTicker())
                .quantity(data.getQuantity())
                .createDate(data.getCreateDate())
                .build();
            stockHistoryResponseList.add(stockHistoryResponse);
        }
        return stockHistoryResponseList;
    }

    public PortfolioResponse getPortfolio(String account){
        List<Portfolio> portfolioList = portfolioRepository.findByAccountNum(account);
        int totalEvaluationAmount = 0;
        int totalPurchaseAmount = 0;
        double totalProfit = 0;
        List<InvestTradeDetailResponse> investTradeDetailResponseList = new ArrayList<>();
        for(Portfolio data : portfolioList){
            System.out.println(data.getTicker());
            StockDuraionPriceOutput stockDuraionPriceOutput = stockRepository.getApiCurrentPrice(data.getTicker(), "0");
            int currentPrice = stockDuraionPriceOutput.getOutput1().getCurrentPrice();
            int averagePrice = data.getAveragePrice();
            String companyName = stockDuraionPriceOutput.getOutput1().getCompanyName();
            double profit = (double) currentPrice / averagePrice*100 -100;
            int profitAndLossAmount = (currentPrice-averagePrice) * data.getQuantity();

            investTradeDetailResponseList.add(
                InvestTradeDetailResponse.builder()
                    .CompanyName(companyName)
                    .evaluationAmount(currentPrice)
                    .profit(profit)
                    .quantity(data.getQuantity())
                    .profitAnsLossAmount(profitAndLossAmount)
                    .build()
            );
            totalEvaluationAmount+=currentPrice*data.getQuantity();
            totalPurchaseAmount=+averagePrice*data.getQuantity();
        }
        for(InvestTradeDetailResponse data : investTradeDetailResponseList){
            data.setHoldingRatio((double) (data.getEvaluationAmount() * data.getQuantity()) /totalEvaluationAmount);
        }
        return PortfolioResponse.builder()
            .totalProfit((double) totalEvaluationAmount /totalPurchaseAmount*100 -100)
            .totalEvaluationAmount(totalEvaluationAmount)
            .totalPurchaseAmount(totalPurchaseAmount)
            .investTradeList(investTradeDetailResponseList)
            .build();
    }

    public boolean investStock(String account_num, InvestStockRequest investStockRequest){
        if(investStockRequest.getTrading()==1) {
            //"주문 매도가 맞지 않습니다."
            return purchaseStock(account_num, investStockRequest);
        }else {
            return sellStock(account_num, investStockRequest);
        }
    }


    boolean purchaseStock(String account_num, InvestStockRequest investStockRequest){
        //출금
        //판매 로직
        StockDuraionPriceOutput stockDuraionPriceOutput = stockRepository.getApiCurrentPrice(
            investStockRequest.getTicker(),"0");
        int currentPrice = stockDuraionPriceOutput.getOutput1().getCurrentPrice();

        //구매 이력
        stockHistoryRepository.save(investStockRequest.toEntityHistory(account_num,currentPrice));
        Optional<Portfolio> prePortfolio = portfolioRepository.findByAccountNumAndTicker(account_num,
            investStockRequest.getTicker());
        if(prePortfolio.isEmpty()){
            portfolioRepository.save(investStockRequest.toEntityPortfolio(account_num,currentPrice));
            return true;
        }else{
            int tempSum = prePortfolio.get().getAveragePrice()*prePortfolio.get().getQuantity();
            short tempQuantity = prePortfolio.get().getQuantity();
            tempSum+= investStockRequest.getQuantity()*currentPrice;
            tempQuantity+= investStockRequest.getQuantity();
            int averagePrice = tempSum/tempQuantity;
            prePortfolio.get().setQuantity(tempQuantity);
            prePortfolio.get().setAveragePrice(averagePrice);
            portfolioRepository.save(prePortfolio.get());
            return true;
        }
//        portfolioRepository.save();
        //포트폴리오

    }
    boolean sellStock(String account_num, InvestStockRequest investStockRequest){
        StockDuraionPriceOutput stockDuraionPriceOutput = stockRepository.getApiCurrentPrice(
            investStockRequest.getTicker(),"0");
        int currentPrice = stockDuraionPriceOutput.getOutput1().getCurrentPrice();


        stockHistoryRepository.save(investStockRequest.toEntityHistory(account_num,currentPrice));
        Optional<Portfolio> prePortfolio = portfolioRepository.findByAccountNumAndTicker(account_num,
            investStockRequest.getTicker());
        if(prePortfolio.isEmpty()){
//            portfolioRepository.save(investStockReqDTO.toEntityPortfolio(account_num,currentPrice));
            throw new CustomException( FAILED_SHORTAGE_MONEY);
        } else if (prePortfolio.get().getQuantity()< investStockRequest.getQuantity()) {
            throw new CustomException( FAILED_SHORTAGE_MONEY);
        }
        else{
            short tempQuantity = prePortfolio.get().getQuantity();
            tempQuantity-= investStockRequest.getQuantity();
            prePortfolio.get().setQuantity(tempQuantity);
            portfolioRepository.save(prePortfolio.get());
            //입금 로직

            return true;
        }
    }
}
