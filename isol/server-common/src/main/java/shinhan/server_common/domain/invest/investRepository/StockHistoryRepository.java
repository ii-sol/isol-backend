package shinhan.server_common.domain.invest.investRepository;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_common.domain.invest.investEntity.StockHistory;

public interface StockHistoryRepository extends JpaRepository<StockHistory,Long> {
    List<StockHistory> findByAccountNumAndTradingCodeAndCreateDateBetween(String accountNum,short tradingCode,
        Timestamp startDate,Timestamp endDate);
    List<StockHistory> findByAccountNumAndCreateDateBetween(String accountNum,
    Timestamp startDate,Timestamp endDate);
    List<StockHistory> findByAccountNum(String accountNum);
}
