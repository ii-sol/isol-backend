package shinhan.server_common.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shinhan.server_common.domain.account.entity.AccountHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Integer>, AccountHistoryRepositoryCustom{

    @Query("SELECT a FROM AccountHistory a " +
            "WHERE (a.senderAccountNum = :accountNum OR a.receiverAccountNum = :accountNum) " +
            "AND a.createDate BETWEEN :start AND :end")
    List<AccountHistory> findByAccountAndCreateDateBetween(
            @Param("accountNum") String accountNum,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
