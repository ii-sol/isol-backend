package shinhan.server_child.domain.loan.repository;

import java.util.List;
import shinhan.server_child.domain.loan.dto.LoanDto;

public interface LoanCustomRepository {
    List<LoanDto> findByChildID(Long childId);

    void acceptLoan(int loanId);

    void refuseLoan(int loanId);

    LoanDto findLoanById(int loanId);

    int findCompleteLoanCount(long childId);
}
