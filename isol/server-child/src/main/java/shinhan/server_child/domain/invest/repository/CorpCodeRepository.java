package shinhan.server_child.domain.invest.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_child.domain.invest.entity.CorpCode;



public interface CorpCodeRepository extends JpaRepository<CorpCode, Long> {
    List<CorpCode> findByCorpNameContaining(String corpName);
    Optional<CorpCode> findByStockCode(int ticker);
}
