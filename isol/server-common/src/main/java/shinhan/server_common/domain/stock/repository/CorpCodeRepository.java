package shinhan.server_common.domain.stock.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_common.domain.stock.entity.CorpCode;


public interface CorpCodeRepository extends JpaRepository<CorpCode, Long> {
    List<CorpCode> findByCorpNameContaining(String corpName, Pageable pageable);
    Page<CorpCode> findAll(Pageable pageable);
    Optional<CorpCode> findByStockCode(int ticker);
    Optional<CorpCode> findByCorpCode(int ticker);
}
