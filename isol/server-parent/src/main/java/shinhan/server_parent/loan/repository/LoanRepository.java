package shinhan.server_parent.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_parent.loan.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long>, LoanCustomRepository {
}
