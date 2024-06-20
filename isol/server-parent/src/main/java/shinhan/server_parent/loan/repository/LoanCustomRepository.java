package shinhan.server_parent.loan.repository;

import java.util.List;
import shinhan.server_parent.loan.dto.LoanDto;

public interface LoanCustomRepository {
    List<LoanDto> findByChildID(int childId);

    void acceptLoan(int loanId);

    void refuseLoan(int loanId);

    LoanDto findLoanById(int loanId);
}
