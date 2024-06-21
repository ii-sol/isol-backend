package shinhan.server_common.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_common.domain.account.entity.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUserSerialNumberAndStatus(Long userSerialNumber, Integer status);

    Optional<Account> findByAccountNum(String accountNum);
}