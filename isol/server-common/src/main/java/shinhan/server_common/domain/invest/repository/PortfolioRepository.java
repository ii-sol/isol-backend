package shinhan.server_common.domain.invest.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_common.domain.invest.investEntity.Portfolio;


//@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {
    Optional<Portfolio> findByAccountNumAndTicker(String accountNum, String ticker);
    List<Portfolio> findByAccountNum(String accountNum);
}

