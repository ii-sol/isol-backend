package shinhan.server_child.domain.invest.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_child.domain.invest.entity.StockHistory;

public interface StockHistoryRepository extends JpaRepository<StockHistory,Long> {
    List<StockHistory> findByAccountNumAndTradingCode(String accountNum,short status);
    List<StockHistory> findByAccountNum(String accountNum);
}
