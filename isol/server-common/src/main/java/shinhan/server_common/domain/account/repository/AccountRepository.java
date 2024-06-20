package shinhan.server_common.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_common.domain.account.entity.Account;
import shinhan.server_common.domain.entity.TempUser;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUserAndStatus(TempUser tempUser, Integer status);

    Optional<Account> findByAccountNum(String accountNum);
}