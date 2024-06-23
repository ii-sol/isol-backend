package shinhan.server_child.domain.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shinhan.server_child.domain.loan.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long>, LoanCustomRepository {
}
