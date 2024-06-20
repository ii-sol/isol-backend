package shinhan.server_child.domain.invest.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shinhan.server_child.domain.invest.entity.MyStockList;

@Repository
public interface StockListRepository extends JpaRepository<MyStockList,Integer> {

    List<MyStockList> findAllByUserSn(Long integers);
    void deleteByUserSnAndTicker(Long userSn, String ticker);
}
