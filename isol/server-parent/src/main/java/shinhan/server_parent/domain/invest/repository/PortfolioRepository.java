package shinhan.server_parent.domain.invest.repository;


import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_parent.domain.invest.entity.Portfolio;

//@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio,Integer> {
    Optional<Portfolio> findByAccountNumAndTicker(String accountNum, String ticker);
    List<Portfolio> findByAccountNum(String accountNum);
}
