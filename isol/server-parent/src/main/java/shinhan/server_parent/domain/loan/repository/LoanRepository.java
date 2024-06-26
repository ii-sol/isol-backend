package shinhan.server_parent.domain.loan.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import shinhan.server_parent.domain.loan.dto.LoanDto;
import shinhan.server_parent.domain.loan.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT new shinhan.server_parent.domain.loan.dto.LoanDto(" +
        "l.id, l.dueDate, l.createDate, l.period, l.childId, l.parentId, l.interestRate, " +
        "l.amount, l.balance, l.status, l.title, l.message) " +
        "FROM Loan l WHERE l.childId = :childId")
    List<LoanDto> findByChildId(@Param("childId") Long childId);

    @Modifying
    @Transactional
    @Query("UPDATE Loan l SET l.status = :status WHERE l.id = :id")
    void acceptLoan(@Param("id") int loanId, @Param("status") int status);

    @Modifying
    @Transactional
    @Query("UPDATE Loan l SET l.status = :status WHERE l.id = :id")
    void refuseLoan(@Param("id") int loanId, @Param("status") int status);

    @Query("SELECT new shinhan.server_parent.domain.loan.dto.LoanDto(" +
        "l.id, l.dueDate, l.createDate, l.period, l.childId, l.parentId, l.interestRate, " +
        "l.amount, l.balance, l.status, l.title, l.message) " +
        "FROM Loan l WHERE l.id = :id")
    LoanDto findLoanById(@Param("id") int loanId);

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.childId = :childId AND l.status = 5")
    int findCompleteLoanCount(@Param("childId") long childId);
}