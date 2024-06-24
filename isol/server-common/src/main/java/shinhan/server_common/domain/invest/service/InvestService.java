package shinhan.server_common.domain.invest.service;

import static shinhan.server_common.global.exception.ErrorCode.FAILED_SHORTAGE_MONEY;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shinhan.server_common.domain.account.entity.Account;
import shinhan.server_common.domain.invest.dto.InvestStockRequest;
import shinhan.server_common.domain.invest.dto.InvestTradeDetailResponse;
import shinhan.server_common.domain.invest.dto.PortfolioResponse;
import shinhan.server_common.domain.invest.dto.StockHistoryResponse;
import shinhan.server_common.domain.invest.entity.CorpCode;
import shinhan.server_common.domain.invest.entity.Portfolio;
import shinhan.server_common.domain.invest.entity.StockHistory;
import shinhan.server_common.domain.invest.repository.CorpCodeRepository;
import shinhan.server_common.domain.invest.dto.StockFindCurrentResponse;
import shinhan.server_common.domain.invest.repository.PortfolioRepository;
import shinhan.server_common.domain.invest.repository.StockHistoryRepository;
import shinhan.server_common.domain.invest.repository.StockRepository;
import shinhan.server_common.global.exception.CustomException;
import shinhan.server_common.global.utils.account.AccountUtils;

@Service
@Transactional
public class InvestService {
    StockRepository stockRepository;
    StockService stockService;
    PortfolioRepository portfolioRepository;
    StockHistoryRepository stockHistoryRepository;

    AccountUtils accountUtils;

    CorpCodeRepository corpCodeRepository;
    InvestService(StockRepository stockRepository,PortfolioRepository portfolioRepository, StockHistoryRepository stockHistoryRepository
    ,StockService stockService, CorpCodeRepository corpCodeRepository , AccountUtils accountUtils
    ){
        this.portfolioRepository = portfolioRepository;
        this.stockRepository = stockRepository;
        this.stockHistoryRepository = stockHistoryRepository;
        this.stockService = stockService;
        this.corpCodeRepository = corpCodeRepository;
        this.accountUtils = accountUtils;
    }

    public List<StockHistoryResponse> getStockHistory(String account,short status,int year,int month){
        List<StockHistory> result;
        LocalDateTime startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusMonths(1).minusSeconds(1);
        Timestamp startTimeStamp = Timestamp.valueOf(startDateTime);
        Timestamp endTimeStamp = Timestamp.valueOf(endDateTime);
        if(status == 0){
            result = stockHistoryRepository.findByAccountNumAndCreateDateBetween(account,startTimeStamp,endTimeStamp);
        }else
            result = stockHistoryRepository.findByAccountNumAndTradingCodeAndCreateDateBetween(account,status,startTimeStamp,endTimeStamp);
        List<StockHistoryResponse> stockHistoryResponseList = new ArrayList<>();
        for(StockHistory data : result)
        {
            String companyName = corpCodeRepository.findByStockCode(Integer.parseInt(data.getTicker())).get().getCorpName();
            StockHistoryResponse stockHistoryResponse = StockHistoryResponse.builder()
                .stockPrice(data.getStockPrice())
                .tradingCode(data.getTradingCode())
                .ticker(data.getTicker())
                .companyName(companyName)
                .quantity(data.getQuantity())
//                .createDate(data.getCreateDate())
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
            StockFindCurrentResponse stockCurrent2 = stockService.getStockCurrent2(
                data.getTicker());
            double currentPrice = Double.parseDouble(stockCurrent2.getCurrentPrice());
            int averagePrice = data.getAveragePrice();
            String companyName = stockCurrent2.getCompanyName();

            double profit = (double) currentPrice / averagePrice*100 -100;
            int profitAndLossAmount = (int) ((currentPrice-averagePrice) * data.getQuantity());
            Optional<CorpCode> byStockCode = corpCodeRepository.findByStockCode(
                Integer.parseInt(data.getTicker()));
            investTradeDetailResponseList.add(
                InvestTradeDetailResponse.builder()
                    .CompanyName(byStockCode.get().getCorpName())
                    .ticker(data.getTicker())
                    .evaluationAmount((int) currentPrice)
                    .profit(profit)
                    .quantity(data.getQuantity())
                    .profitAnsLossAmount(profitAndLossAmount)
                    .build()
            );
            totalEvaluationAmount+=currentPrice*data.getQuantity();
            totalPurchaseAmount+=averagePrice*data.getQuantity();
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
        Account userAccount = accountUtils.getAccountByAccountNum(account_num);
        Account systemInvestAccount = accountUtils.getAccountByAccountNum("300");

        //계좌 상한선에 맞춰서 + 메세지 코드 변경
        StockFindCurrentResponse stockFindCurrentResponse = stockService.getStockCurrent2(
            investStockRequest.getTicker());
        int currentPrice = (int) Double.parseDouble(stockFindCurrentResponse.getCurrentPrice());
        accountUtils.transferMoneyByAccount(userAccount,systemInvestAccount,currentPrice* investStockRequest.getQuantity(),1);
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
    }
    boolean sellStock(String account_num, InvestStockRequest investStockRequest){
        Account userAccount = accountUtils.getAccountByAccountNum(account_num);
        Account systempInvestAccount = accountUtils.getAccountByAccountNum("300");


        //계좌 상한선에 맞춰서 + 메세지 코드 변경

        StockFindCurrentResponse stockFindCurrentResponse = stockService.getStockCurrent2(
            investStockRequest.getTicker());
        int currentPrice = (int) Double.parseDouble(stockFindCurrentResponse.getCurrentPrice());
        stockHistoryRepository.save(investStockRequest.toEntityHistory(account_num,currentPrice));
        Optional<Portfolio> prePortfolio = Optional.ofNullable(
            portfolioRepository.findByAccountNumAndTicker(account_num,
                investStockRequest.getTicker()).orElseThrow(() -> {
                return new CustomException(FAILED_SHORTAGE_MONEY);
            }));
        if(prePortfolio.isEmpty()){
            throw new CustomException(FAILED_SHORTAGE_MONEY);
        } else if (prePortfolio.get().getQuantity()< investStockRequest.getQuantity()) {
            throw new CustomException(FAILED_SHORTAGE_MONEY);
        }
        else{
            short tempQuantity = prePortfolio.get().getQuantity();
            tempQuantity-= investStockRequest.getQuantity();
            Long id = prePortfolio.get().getId();
            if(tempQuantity==0)
                portfolioRepository.deleteById(id);
            else
            {
                prePortfolio.get().setQuantity(tempQuantity);
                portfolioRepository.save(prePortfolio.get());
            }
            accountUtils.transferMoneyByAccount(systempInvestAccount,userAccount,currentPrice*investStockRequest.getQuantity(),1);
            return true;
        }
    }
}
