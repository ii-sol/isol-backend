package shinhan.server_common.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_common.domain.account.entity.AccountHistory;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Integer>, AccountHistoryRepositoryCustom{

}