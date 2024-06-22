package shinhan.server_common.domain.account.repository;

import shinhan.server_common.domain.account.entity.Account;
import shinhan.server_common.domain.account.entity.AccountHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountHistoryRepositoryCustom {
    List<AccountHistory> findByAccountAndCreateDateBetween(Account account , LocalDateTime start, LocalDateTime end);
}