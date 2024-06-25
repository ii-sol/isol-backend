package shinhan.server_parent.domain.loan.service;

import java.util.List;
import org.springframework.stereotype.Service;
import shinhan.server_parent.domain.loan.dto.LoanDto;
import shinhan.server_parent.domain.loan.repository.LoanRepository;

@Service
public class LoanService {

    LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<LoanDto> getLoanByChildId(Long childId) {
        return loanRepository.findByChildId(childId);
    }

    public void acceptLoan(int loanId) {
        loanRepository.acceptLoan(loanId, 3);
    }

    public void refuseLoan(int loanId) {
        loanRepository.refuseLoan(loanId, 5);
    }

    public LoanDto findOne(int loanId) {
        return loanRepository.findLoanById(loanId);
    }
}
