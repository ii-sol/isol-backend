package shinhan.server_parent.domain.invest.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import shinhan.server_common.domain.invest.entity.StockDuraionPriceOutput;
import shinhan.server_common.domain.invest.repository.StockRepository;
import shinhan.server_parent.domain.invest.dto.InvestTradeDetailResponse;
import shinhan.server_parent.domain.invest.entity.Portfolio;
import shinhan.server_parent.domain.invest.entity.PortfolioResponse;
import shinhan.server_parent.domain.invest.repository.PortfolioRepository;

@Service
public class InvestService {
    PortfolioRepository portfolioRepository;
    StockRepository stockRepository;
    InvestService(PortfolioRepository portfolioRepository,StockRepository stockRepository){
        this.portfolioRepository = portfolioRepository;
        this.stockRepository = stockRepository;
    }


    public PortfolioResponse getPortfolio(String accountNum){
        List<Portfolio> portfolioList = portfolioRepository.findByAccountNum(accountNum);
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
}
