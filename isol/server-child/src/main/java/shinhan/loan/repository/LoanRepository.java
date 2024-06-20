package shinhan.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sinhan.server2.loan.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long>, LoanCustomRepository {
}
