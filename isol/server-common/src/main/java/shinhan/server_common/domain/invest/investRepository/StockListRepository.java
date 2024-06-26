package shinhan.server_common.domain.invest.investRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shinhan.server_common.domain.invest.investEntity.MyStockList;

@Repository
public interface StockListRepository extends JpaRepository<MyStockList,Long> {
    List<MyStockList> findAllByUserSn(Long integers);
    void deleteByUserSnAndTicker(Long userSn, String ticker);
    Optional<MyStockList> findAllByUserSnAndTicker(Long userSn, String ticker);
}
